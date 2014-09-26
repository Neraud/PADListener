package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;

/**
 * ViewMonsterInfo fragment for the RefreshInfo tab
 *
 * @author Neraud
 */
public class ViewMonsterInfoRefreshInfoFragment extends Fragment {

	private static final String TAG_TASK_FRAGMENT = "info_task_fragment";
	private final ViewMonsterInfoRefreshInfoTaskFragment.CallBacks callbacks;
	private ViewMonsterInfoRefreshInfoTaskFragment mTaskFragment;
	private TextView statusText;
	private TextView current;
	private ProgressBar progress;
	private Button startButton;

	public ViewMonsterInfoRefreshInfoFragment() {
		callbacks = new ViewMonsterInfoRefreshInfoTaskFragment.CallBacks() {

			@Override
			public void updateState(RestCallState state, RestCallRunningStep runningStep, Throwable errorCause) {
				Log.d(getClass().getName(), "updateState");
				if (state != null) {
					startButton.setEnabled(false);
					progress.setVisibility(View.VISIBLE);

					switch (state) {
						case RUNNING:
							if (runningStep == null) {
								progress.setIndeterminate(true);
								statusText.setText(R.string.monster_info_fetch_info_fetching);
							} else {
								progress.setIndeterminate(false);
								progress.setMax(4);
								switch (runningStep) {
									case STARTED:
										statusText.setText(R.string.monster_info_fetch_info_calling);
										progress.setProgress(1);
										break;
									case RESPONSE_RECEIVED:
										statusText.setText(R.string.monster_info_fetch_info_parsing);
										progress.setProgress(2);
										break;
									case RESPONSE_PARSED:
										statusText.setText(R.string.monster_info_fetch_info_saving);
										progress.setProgress(3);
										break;
									default:
										break;
								}
							}
							break;
						case SUCCEEDED:
							progress.setIndeterminate(false);
							progress.setProgress(4);
							progress.setMax(4);
							statusText.setText(R.string.monster_info_fetch_info_fetching_done);
							refreshLastUpdate();
							break;
						case FAILED:
							final String message = errorCause != null ? errorCause.getMessage() : "?";
							statusText.setText(getString(R.string.monster_info_fetch_info_fetching_failed, message));
						default:
							break;
					}
				} else {
					progress.setVisibility(View.GONE);
				}
			}

		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_refresh_info, container, false);

		statusText = (TextView) view.findViewById(R.id.monster_info_fetch_info_status);
		progress = (ProgressBar) view.findViewById(R.id.monster_info_fetch_info_progress);
		current = (TextView) view.findViewById(R.id.monster_info_fetch_info_current);
		startButton = (Button) view.findViewById(R.id.monster_info_fetch_info_button);

		refreshLastUpdate();

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ViewMonsterInfoRefreshInfoTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new ViewMonsterInfoRefreshInfoTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		}
		mTaskFragment.registerCallbacks(callbacks);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				mTaskFragment.startFetchInfoService();
			}
		});

		return view;
	}

	private void refreshLastUpdate() {
		final String lastRefreshDate = SimpleDateFormat.getDateInstance().format(
				new TechnicalSharedPreferencesHelper(getActivity()).getMonsterInfoRefreshDate());
		current.setText(getString(R.string.monster_info_fetch_info_current, lastRefreshDate));
	}
}
