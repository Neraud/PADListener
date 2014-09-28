package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;

/**
 * ViewMonsterInfo fragment for the RefreshInfo tab
 *
 * @author Neraud
 */
public class ViewMonsterInfoRefreshDialogFragment extends DialogFragment {

	private static final String TAG_TASK_FRAGMENT = "view_monster_info_refresh_task";
	private final ViewMonsterInfoRefreshInfoTaskFragment.ProgressCallBacks mProgressCallbacks;
	private ViewMonsterInfoRefreshInfoTaskFragment mTaskFragment;
	private TextView mStatusText;
	private TextView mCurrentText;
	private ProgressBar mProgress;

	public ViewMonsterInfoRefreshDialogFragment() {
		mProgressCallbacks = new ViewMonsterInfoRefreshInfoTaskFragment.ProgressCallBacks() {

			@Override
			public void updateCallState(RestCallState callState, RestCallRunningStep callRunningStep, Throwable callErrorCause) {
				Log.d(getClass().getName(), "updateCallState : " + callState + ", " + callRunningStep);
				setCancelable(false);
				if (callState != null) {
					mProgress.setVisibility(View.VISIBLE);

					switch (callState) {
						case RUNNING:
							if (callRunningStep == null) {
								mProgress.setIndeterminate(true);
								mStatusText.setText(R.string.monster_info_refresh_info_fetching);
							} else {
								mProgress.setIndeterminate(false);
								mProgress.setMax(4);
								switch (callRunningStep) {
									case STARTED:
										mStatusText.setText(R.string.monster_info_refresh_info_calling);
										mProgress.setProgress(1);
										break;
									case RESPONSE_RECEIVED:
										mStatusText.setText(R.string.monster_info_refresh_info_parsing);
										mProgress.setProgress(2);
										break;
									case RESPONSE_PARSED:
										mStatusText.setText(R.string.monster_info_refresh_info_saving);
										mProgress.setProgress(3);
										break;
									default:
										break;
								}
							}
							break;
						case SUCCEEDED:
							setCancelable(true);
							mProgress.setIndeterminate(false);
							mProgress.setProgress(4);
							mProgress.setMax(4);
							mStatusText.setText(R.string.monster_info_refresh_info_fetching_done);
							refreshLastUpdate();
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									dismiss();
								}
							}, 1000);
							break;
						case FAILED:
							setCancelable(true);
							final String message = callErrorCause != null ? callErrorCause.getMessage() : "?";
							mStatusText.setText(getString(R.string.monster_info_refresh_info_fetching_failed, message));
						default:
							break;
					}
				} else {
					mProgress.setVisibility(View.GONE);
				}
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_refresh_info, container, false);
		getDialog().setTitle(R.string.monster_info_refresh_title);
		mStatusText = (TextView) view.findViewById(R.id.monster_info_refresh_info_status);
		mProgress = (ProgressBar) view.findViewById(R.id.monster_info_refresh_info_progress);
		mCurrentText = (TextView) view.findViewById(R.id.monster_info_refresh_info_current);

		refreshLastUpdate();

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ViewMonsterInfoRefreshInfoTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new ViewMonsterInfoRefreshInfoTaskFragment();
			mTaskFragment.setAutoStart(true);
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		}
		mTaskFragment.registerCallbacks(mProgressCallbacks);

		return view;
	}

	private void refreshLastUpdate() {
		final Date lastRefreshDate = new TechnicalSharedPreferencesHelper(getActivity()).getMonsterInfoRefreshDate();
		final String lastRefreshDateFormatted = SimpleDateFormat.getDateInstance().format(lastRefreshDate);
		mCurrentText.setText(getString(R.string.monster_info_last_refresh, lastRefreshDateFormatted));
	}
}
