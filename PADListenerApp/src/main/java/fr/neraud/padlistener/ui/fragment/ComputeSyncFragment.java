package fr.neraud.padlistener.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.ChooseSyncInitHelper;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.exception.HttpResponseException;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.PADHerderAccountModel;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.ui.activity.AbstractPADListenerActivity;
import fr.neraud.padlistener.ui.activity.ChooseSyncActivity;
import fr.neraud.padlistener.ui.activity.ComputeSyncActivity;
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

	private boolean mAutoSync;
	private boolean mAccountFound = false;
	private int mAccountId = -1;

	@InjectView(R.id.compute_sync_button)
	Button mStartButton;
	@InjectView(R.id.compute_sync_progress)
	ProgressBar mProgress;
	@InjectView(R.id.compute_sync_status)
	TextView mStatus;
	@InjectView(R.id.compute_sync_error_explain)
	TextView mErrorExplain;
	@InjectView(R.id.compute_sync_auto_no_matching_account)
	TextView mAutoNoMatchingAccount;
	@InjectView(R.id.compute_sync_content)
	TextView mContent;
	@InjectView(R.id.compute_sync_choose_account_spinner)
	Spinner mChooseAccountSpinner;
	@InjectView(R.id.compute_sync_missing_credentials_text)
	TextView mMissingCredentials;
	@InjectView(R.id.compute_sync_missing_capture_text)
	TextView mMissingCapture;
	@InjectView(R.id.compute_sync_nothing_to_sync_text)
	TextView mNothingToSync;

	private final ComputeSyncTaskFragment.CallBacks mCallBacks = new ComputeSyncTaskFragment.CallBacks() {

		@Override
		public void updateState(RestCallState state, RestCallRunningStep runningStep, ComputeSyncResultModel syncResult, Throwable errorCause) {
			MyLog.entry();
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

						final ChooseSyncInitHelper initHelper = new ChooseSyncInitHelper(getActivity(), syncResult);
						final ChooseSyncModel chooseSyncModel = initHelper.filterSyncResult();

						if (mAutoSync) {
							if (initHelper.isHasChosenDataToSync()) {
								final Bundle extras = new Bundle();
								extras.putSerializable(PushSyncFragment.EXTRA_CHOOSE_SYNC_MODEL_NAME, chooseSyncModel);
								extras.putInt(PushSyncFragment.EXTRA_ACCOUNT_ID_NAME, mAccountId);
								((AbstractPADListenerActivity) getActivity()).goToScreen(UiScreen.PUSH_SYNC, extras);
							} else {
								mNothingToSync.setText(initHelper.isHasDataToSync() ? R.string.compute_sync_nothing_chosen_to_sync : R.string.compute_sync_nothing_to_sync);
								mNothingToSync.setVisibility(View.VISIBLE);
							}
						} else {
							if (initHelper.isHasDataToSync()) {
								final Bundle extras = new Bundle();
								extras.putSerializable(ChooseSyncActivity.EXTRA_CHOOSE_SYNC_RESULT_NAME, chooseSyncModel);
								extras.putInt(ChooseSyncActivity.EXTRA_ACCOUNT_ID_NAME, mAccountId);
								((AbstractPADListenerActivity) getActivity()).goToScreen(UiScreen.CHOOSE_SYNC, extras);
							} else {
								mNothingToSync.setText(R.string.compute_sync_nothing_to_sync);
								mNothingToSync.setVisibility(View.VISIBLE);
							}
						}
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
			MyLog.exit();
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final Bundle extras = getActivity().getIntent().getExtras();
		mAutoSync = extras != null && extras.getBoolean(ComputeSyncActivity.AUTO_SYNC_EXTRA_NAME);

		final View view = inflater.inflate(R.layout.compute_sync_fragment, container, false);
		ButterKnife.inject(this, view);

		final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(getActivity());

		final String refreshDate = DateFormat.getDateTimeInstance().format(techPrefHelper.getLastCaptureDate());
		mContent.setText(getString(R.string.compute_sync_content, refreshDate));

		final List<PADHerderAccountModel> accounts = new DefaultSharedPreferencesHelper(getActivity()).getPadHerderAccounts();

		final AccountSpinnerAdapter adapter = new AccountSpinnerAdapter(getActivity(), accounts);
		mChooseAccountSpinner.setAdapter(adapter);

		final String lastCaptureAccountName = techPrefHelper.getLastCaptureName();
		if (StringUtils.isNotBlank(lastCaptureAccountName)) {
			for (final PADHerderAccountModel account : accounts) {
				if (lastCaptureAccountName.equals(account.getName())) {
					final int selectedPosition = adapter.getPositionById(account.getAccountId());
					mChooseAccountSpinner.setSelection(selectedPosition);
					mAccountId = account.getAccountId();
					mAccountFound = true;
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
			mChooseAccountSpinner.setSelection(position);
		}
		mTaskFragment.registerCallbacks(mCallBacks);

		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
		if (prefHelper.getPadHerderAccounts().isEmpty()) {
			mMissingCredentials.setTextColor(Color.RED);
			mStartButton.setEnabled(false);
		} else {
			mMissingCredentials.setVisibility(View.GONE);
		}

		final TechnicalSharedPreferencesHelper techHelper = new TechnicalSharedPreferencesHelper(getActivity());
		if (techHelper.getLastCaptureDate().getTime() == 0) {
			mMissingCapture.setTextColor(Color.RED);
			mStartButton.setEnabled(false);
		} else {
			mMissingCapture.setVisibility(View.GONE);
		}

		mNothingToSync.setVisibility(View.GONE);

		MyLog.exit();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@OnItemSelected(R.id.compute_sync_choose_account_spinner)
	@SuppressWarnings("unused")
	void onAccountSelected(AdapterView<?> parent, View view, int position, long id) {
		MyLog.entry("id = " + id);
		mAccountId = (int) id;
		mTaskFragment.setAccountId(mAccountId);
		MyLog.exit();
	}

	@OnClick(R.id.compute_sync_button)
	@SuppressWarnings("unused")
	void onStartButtonClicked() {
		MyLog.entry();
		mTaskFragment.startComputeSyncService();
		MyLog.exit();
	}

	@Override
	public void onStart() {
		MyLog.entry();
		super.onStart();
		handleAutoSync();
		MyLog.exit();
	}

	private void handleAutoSync() {
		MyLog.entry();

		if (mAutoSync) {
			if (mAccountFound) {
				mAutoNoMatchingAccount.setVisibility(View.GONE);
				mTaskFragment.setAccountId(mAccountId);
				mTaskFragment.startComputeSyncService();
			} else {
				final TechnicalSharedPreferencesHelper techHelper = new TechnicalSharedPreferencesHelper(getActivity());
				final String content = getActivity().getString(R.string.compute_sync_auto_no_matching_account, techHelper.getLastCaptureName());
				mAutoNoMatchingAccount.setText(content);
				mAutoNoMatchingAccount.setVisibility(View.VISIBLE);
			}
		} else {
			mAutoNoMatchingAccount.setVisibility(View.GONE);
		}

		MyLog.exit();
	}
}
