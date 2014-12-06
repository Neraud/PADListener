package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;

/**
 * DialogFragment used to refresh the monster information
 *
 * @author Neraud
 */
public class MonsterInfoRefreshDialogFragment extends DialogFragment {

	private static final String TAG_TASK_FRAGMENT = "view_monster_info_refresh_task";
	private final MonsterInfoRefreshInfoTaskFragment.ProgressCallBacks mProgressCallbacks;

	@InjectView(R.id.monster_info_refresh_info_status)
	TextView mStatusText;
	@InjectView(R.id.monster_info_refresh_info_current)
	TextView mCurrentText;
	@InjectView(R.id.monster_info_refresh_info_progress)
	ProgressBar mProgress;

	public MonsterInfoRefreshDialogFragment() {
		mProgressCallbacks = new MonsterInfoRefreshInfoTaskFragment.ProgressCallBacks() {

			@Override
			public void updateCallState(RestCallState callState, RestCallRunningStep callRunningStep, Throwable callErrorCause) {
				MyLog.entry(callState + ", " + callRunningStep);

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
				MyLog.exit();
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_refresh_info, container, false);
		ButterKnife.inject(this, view);

		getDialog().setTitle(R.string.monster_info_refresh_title);

		refreshLastUpdate();

		final FragmentManager fm = getFragmentManager();
		MonsterInfoRefreshInfoTaskFragment taskFragment = (MonsterInfoRefreshInfoTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (taskFragment == null) {
			taskFragment = new MonsterInfoRefreshInfoTaskFragment();
			taskFragment.setAutoStart(true);
			fm.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
		}
		taskFragment.registerCallbacks(mProgressCallbacks);

		MyLog.exit();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	private void refreshLastUpdate() {
		final Date lastRefreshDate = new TechnicalSharedPreferencesHelper(getActivity()).getMonsterInfoRefreshDate();
		if(lastRefreshDate.getTime() > 0) {
			final String lastRefreshDateFormatted = SimpleDateFormat.getDateInstance().format(lastRefreshDate);
			mCurrentText.setText(getString(R.string.monster_info_last_refresh_date, lastRefreshDateFormatted));
		} else {
			mCurrentText.setText(getString(R.string.monster_info_last_refresh_never));
		}
	}
}
