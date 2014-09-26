package fr.neraud.padlistener.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.service.ComputeSyncService;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * ComputeSync retained fragment to store the sync computation progression
 *
 * @author Neraud
 */
public class ComputeSyncTaskFragment extends Fragment {

	private int accountId;
	private RestCallState state = null;
	private RestCallRunningStep runningStep = null;
	private ComputeSyncResultModel syncResult = null;
	private Throwable errorCause = null;
	private CallBacks callbacks = null;

	/**
	 * Interface to implement to be notified when the sync computation progresses
	 *
	 * @author Neraud
	 */
	public static interface CallBacks {

		public void updateState(RestCallState state, RestCallRunningStep runningStep, ComputeSyncResultModel syncResult,
				Throwable errorCause);
	}

	private class MyComputeSyncReceiver extends AbstractRestResultReceiver<ComputeSyncResultModel> {

		public MyComputeSyncReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep progress) {
			Log.d(getClass().getName(), "onReceiveProgress : " + progress);
			state = RestCallState.RUNNING;
			runningStep = progress;
			notifyCallBacks();
		}

		@Override
		protected void onReceiveSuccess(ComputeSyncResultModel result) {
			Log.d(getClass().getName(), "onReceiveSuccess");
			state = RestCallState.SUCCEEDED;
			syncResult = result;
			notifyCallBacks();
		}

		@Override
		protected void onReceiveError(RestCallError error, Throwable cause) {
			Log.d(getClass().getName(), "onReceiveError : " + error);
			state = RestCallState.FAILED;
			errorCause = cause;
			notifyCallBacks();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public void onDetach() {
		Log.d(getClass().getName(), "onDetach");
		super.onDetach();
		callbacks = null;
	}

	public void registerCallbacks(CallBacks callbacks) {
		this.callbacks = callbacks;
		notifyCallBacks();
	}

	private void notifyCallBacks() {
		if (callbacks != null) {
			callbacks.updateState(state, runningStep, syncResult, errorCause);
		}
	}

	/**
	 * Starts the ComputeSyncService
	 *
	 * @param accountId the accountId
	 */
	public void startComputeSyncService(int accountId) {
		Log.d(getClass().getName(), "startComputeSyncService");
		state = RestCallState.RUNNING;
		runningStep = null;
		syncResult = null;
		errorCause = null;
		notifyCallBacks();

		final Intent startIntent = new Intent(getActivity(), ComputeSyncService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyComputeSyncReceiver(new Handler()));
		startIntent.putExtra(ComputeSyncService.EXTRA_ACCOUNT_ID_NAME, accountId);

		getActivity().startService(startIntent);
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

}
