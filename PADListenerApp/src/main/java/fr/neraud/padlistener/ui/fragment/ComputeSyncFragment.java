package fr.neraud.padlistener.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.exception.HttpResponseException;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.PADHerderAccountModel;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.ui.activity.AbstractPADListenerActivity;
import fr.neraud.padlistener.ui.activity.ChooseSyncActivity;
import fr.neraud.padlistener.ui.adapter.AccountSpinnerAdapter;
import fr.neraud.padlistener.ui.constant.UiScreen;

/**
 * ComputeSync fragment
 *
 * @author Neraud
 */
public class ComputeSyncFragment extends Fragment {

	private static final String TAG_TASK_FRAGMENT = "compute_sync_task_fragment";
	private ComputeSyncTaskFragment mTaskFragment;

	private Button mStartButton;
	private ProgressBar mProgress;
	private TextView mStatus;
	private TextView mErrorExplain;
	private int mAccountId = -1;
	private final ComputeSyncTaskFragment.CallBacks mCallBacks = new ComputeSyncTaskFragment.CallBacks() {

		@Override
		public void updateState(RestCallState state, RestCallRunningStep runningStep, ComputeSyncResultModel syncResult,
				Throwable errorCause) {
			Log.d(getClass().getName(), "updateState");
			if (state != null) {
				mStartButton.setEnabled(false);
				mProgress.setVisibility(View.VISIBLE);
				mStatus.setVisibility(View.VISIBLE);
				mErrorExplain.setVisibility(View.GONE);
				mProgress.setMax(4);

				switch (state) {
					case RUNNING:
						if (runningStep == null) {
							mProgress.setIndeterminate(true);
						} else {
							mProgress.setIndeterminate(false);
							switch (runningStep) {
								case STARTED:
									mStatus.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_calling)));
									mProgress.setProgress(1);
									break;
								case RESPONSE_RECEIVED:
									mStatus.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_parsing)));
									mProgress.setProgress(2);
									break;
								case RESPONSE_PARSED:
									mStatus.setText(getString(R.string.compute_sync_status,
											getString(R.string.compute_sync_status_computing)));
									mProgress.setProgress(3);
									break;
								default:
									break;
							}
						}
						break;
					case SUCCEEDED:
						mProgress.setIndeterminate(false);
						mStatus.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_finished)));
						mProgress.setProgress(4);

						final Bundle extras = new Bundle();
						extras.putSerializable(ChooseSyncActivity.EXTRA_SYNC_RESULT_NAME, syncResult);
						extras.putInt(ChooseSyncActivity.EXTRA_ACCOUNT_ID_NAME, mAccountId);
						((AbstractPADListenerActivity) getActivity()).goToScreen(UiScreen.CHOOSE_SYNC, extras);

						break;
					case FAILED:
						mProgress.setIndeterminate(false);

						final String message = errorCause != null ? errorCause.getMessage() : "?";
						mStatus.setText(getString(R.string.compute_sync_status, getString(R.string.compute_sync_status_failed, message)));
						if (errorCause instanceof HttpResponseException) {
							final int code = ((HttpResponseException) errorCause).getCode();
							if (code == 403) {
								final String accountLogin = new DefaultSharedPreferencesHelper(getActivity())
										.getPadHerderUserName(mAccountId);
								mErrorExplain.setText(getString(R.string.compute_sync_check_credentials, accountLogin));
								mErrorExplain.setVisibility(View.VISIBLE);
							} else if (code == 404) {
								final String accountLogin = new DefaultSharedPreferencesHelper(getActivity())
										.getPadHerderUserName(mAccountId);
								mErrorExplain.setText(getString(R.string.compute_sync_check_login_case, accountLogin));
								mErrorExplain.setVisibility(View.VISIBLE);
							} else if (code == 500) {
								mErrorExplain.setText(R.string.compute_sync_check_padherder_status);
								mErrorExplain.setVisibility(View.VISIBLE);
							}
						}
					default:
						break;
				}
			} else {
				mProgress.setVisibility(View.GONE);
				mStatus.setVisibility(View.GONE);
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.compute_sync_fragment, container, false);

		final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(getActivity());

		final TextView content = (TextView) view.findViewById(R.id.compute_sync_content);
		final String refreshDate = DateFormat.getDateTimeInstance().format(techPrefHelper.getLastCaptureDate());
		content.setText(getString(R.string.compute_sync_content, refreshDate));
		final Spinner chooseAccountSpinner = (Spinner) view.findViewById(R.id.compute_sync_choose_account_spinner);
		mStartButton = (Button) view.findViewById(R.id.compute_sync_button);
		mProgress = (ProgressBar) view.findViewById(R.id.compute_sync_progress);
		mStatus = (TextView) view.findViewById(R.id.compute_sync_status);
		mErrorExplain = (TextView) view.findViewById(R.id.compute_sync_error_explain);

		final List<PADHerderAccountModel> accounts = new DefaultSharedPreferencesHelper(getActivity()).getPadHerderAccounts();

		final AccountSpinnerAdapter adapter = new AccountSpinnerAdapter(getActivity(), accounts);
		chooseAccountSpinner.setAdapter(adapter);

		final String lastCaptureAccountName = techPrefHelper.getLastCaptureName();
		if(StringUtils.isNotBlank(lastCaptureAccountName)) {
			for (final PADHerderAccountModel account : accounts) {
				if (lastCaptureAccountName.equals(account.getName())) {
					final int selectedPosition = adapter.getPositionById(account.getAccountId());
					chooseAccountSpinner.setSelection(selectedPosition);
					break;
				}
			}
		}

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (ComputeSyncTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new ComputeSyncTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		} else {
			mAccountId = mTaskFragment.getAccountId();
			final int position = adapter.getPositionById(mAccountId);
			chooseAccountSpinner.setSelection(position);
		}
		mTaskFragment.registerCallbacks(mCallBacks);

		chooseAccountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(getClass().getName(), "onItemSelected : " + id);
				mAccountId = (int) id;
				mTaskFragment.setAccountId(mAccountId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.d(getClass().getName(), "onNothingSelected");

			}
		});

		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				mTaskFragment.startComputeSyncService(mAccountId);
			}
		});

		final TextView missingCredentials = (TextView) view.findViewById(R.id.compute_sync_missing_credentials_text);
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
		if (prefHelper.getPadHerderAccounts().isEmpty()) {
			missingCredentials.setTextColor(Color.RED);
			mStartButton.setEnabled(false);
		} else {
			missingCredentials.setVisibility(View.GONE);
		}

		final TextView missingCapture = (TextView) view.findViewById(R.id.compute_sync_missing_capture_text);
		final TechnicalSharedPreferencesHelper techHelper = new TechnicalSharedPreferencesHelper(getActivity());
		if (techHelper.getLastCaptureDate().getTime() == 0) {
			missingCapture.setTextColor(Color.RED);
			mStartButton.setEnabled(false);
		} else {
			missingCapture.setVisibility(View.GONE);
		}

		return view;
	}
}
