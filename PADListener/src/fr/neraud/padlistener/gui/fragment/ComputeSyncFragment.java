
package fr.neraud.padlistener.gui.fragment;

import java.text.DateFormat;

import org.apache.commons.lang3.StringUtils;

import android.graphics.Color;
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
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.AbstractPADListenerActivity;
import fr.neraud.padlistener.gui.constant.GuiScreen;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.model.SyncComputeResultModel;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;

public class ComputeSyncFragment extends Fragment {

	private static final String TAG_TASK_FRAGMENT = "compute_sync_task_fragment";
	private ComputeSyncTaskFragment mTaskFragment;

	private Button startButton;
	private ProgressBar progress;
	private TextView status;
	private final ComputeSyncTaskFragment.CallBacks callbacks = new ComputeSyncTaskFragment.CallBacks() {

		@Override
		public void updateState(RestCallState state, RestCallRunningStep runningStep, SyncComputeResultModel syncResult,
		        String errorMessage) {
			Log.d(getClass().getName(), "updateState");
			if (state != null) {
				startButton.setEnabled(false);
				progress.setVisibility(View.VISIBLE);
				status.setVisibility(View.VISIBLE);
				progress.setMax(4);

				switch (state) {
				case RUNNING:
					if (runningStep == null) {
						progress.setIndeterminate(true);
						status.setText(R.string.monster_info_fetch_info_fetching);
					} else {
						progress.setIndeterminate(false);
						switch (runningStep) {
						case STARTED:
							status.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_calling)));
							progress.setProgress(1);
							break;
						case RESPONSE_RECEIVED:
							status.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_parsing)));
							progress.setProgress(2);
							break;
						case RESPONSE_PARSED:
							status.setText(getString(R.string.compute_sync_status,
							        getString(R.string.compute_sync_status_computing)));
							progress.setProgress(3);
							break;
						default:
							break;
						}
					}
					break;
				case SUCCESSED:
					progress.setIndeterminate(false);
					status.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_finished)));
					progress.setProgress(4);

					final Bundle extras = new Bundle();
					extras.putSerializable(ChooseSyncFragment.EXTRA_SYNC_RESULT_NAME, syncResult);
					((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.CHOOSE_SYNC, extras);

					break;
				case FAILED:
					progress.setIndeterminate(false);
					status.setText(getString(R.string.compute_sync_status,
					        getString(R.string.compute_sync_status_failed, errorMessage)));
				default:
					break;
				}
			} else {
				progress.setVisibility(View.GONE);
				status.setVisibility(View.GONE);
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.compute_sync_fragment, container, false);

		final DefaultSharedPreferencesHelper defaultPrefHelper = new DefaultSharedPreferencesHelper(getActivity());
		final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(getActivity());

		final TextView explain = (TextView) view.findViewById(R.id.compute_sync_explain);
		final String refreshDate = DateFormat.getDateTimeInstance().format(techPrefHelper.getLastCaptureDate());
		explain.setText(getString(R.string.compute_sync_explain, defaultPrefHelper.getPadHerderUserName(), refreshDate));
		startButton = (Button) view.findViewById(R.id.compute_sync_button);
		progress = (ProgressBar) view.findViewById(R.id.compute_sync_progress);
		status = (TextView) view.findViewById(R.id.compute_sync_status);

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ComputeSyncTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new ComputeSyncTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		}
		mTaskFragment.registerCallbacks(callbacks);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				mTaskFragment.startComputeSyncService();
			}
		});

		final TextView missingCredentials = (TextView) view.findViewById(R.id.compute_sync_missing_credentials_text);
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
		if (StringUtils.isBlank(prefHelper.getPadHerderUserName()) || StringUtils.isBlank(prefHelper.getPadHerderUserPassword())) {
			missingCredentials.setTextColor(Color.RED);
			startButton.setEnabled(false);
		} else {
			missingCredentials.setVisibility(View.GONE);
		}

		final TextView missingCapture = (TextView) view.findViewById(R.id.compute_sync_missing_capture_text);
		final TechnicalSharedPreferencesHelper techHelper = new TechnicalSharedPreferencesHelper(getActivity());
		if (techHelper.getLastCaptureDate().getTime() == 0) {
			missingCapture.setTextColor(Color.RED);
			startButton.setEnabled(false);
		} else {
			missingCapture.setVisibility(View.GONE);
		}

		return view;
	}
}
