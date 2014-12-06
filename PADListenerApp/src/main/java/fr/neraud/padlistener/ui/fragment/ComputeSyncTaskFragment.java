package fr.neraud.padlistener.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import fr.neraud.log.MyLog;
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

	private int mAccountId;
	private RestCallState mCallState = null;
	private RestCallRunningStep mCallRunningStep = null;
	private ComputeSyncResultModel mSyncResult = null;
	private Throwable mCallErrorCause = null;
	private CallBacks mCallbacks = null;

	/**
	 * Interface to implement to be notified when the sync computation progresses
	 *
	 * @author Neraud
	 */
	public static interface CallBacks {

		public void updateState(RestCallState callState, RestCallRunningStep callEunningStep, ComputeSyncResultModel syncResult,
				Throwable callErrorCause);
	}

	private class MyComputeSyncReceiver extends AbstractRestResultReceiver<ComputeSyncResultModel> {

		public MyComputeSyncReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep progress) {
			MyLog.entry("progress = " + progress);
			mCallState = RestCallState.RUNNING;
			mCallRunningStep = progress;
			notifyCallBacks();
			MyLog.exit();
		}

		@Override
		protected void onReceiveSuccess(ComputeSyncResultModel result) {
			MyLog.entry();
			mCallState = RestCallState.SUCCEEDED;
			mSyncResult = result;
			notifyCallBacks();
			MyLog.exit();
		}

		@Override
		protected void onReceiveError(RestCallError error, Throwable cause) {
			MyLog.entry("error = " + error);
			mCallState = RestCallState.FAILED;
			mCallErrorCause = cause;
			notifyCallBacks();
			MyLog.exit();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		MyLog.exit();
	}

	@Override
	public void onDetach() {
		MyLog.entry();
		super.onDetach();
		mCallbacks = null;
		MyLog.exit();
	}

	public void registerCallbacks(CallBacks callbacks) {
		this.mCallbacks = callbacks;
		notifyCallBacks();
	}

	private void notifyCallBacks() {
		if (mCallbacks != null) {
			mCallbacks.updateState(mCallState, mCallRunningStep, mSyncResult, mCallErrorCause);
		}
	}

	/**
	 * Starts the ComputeSyncService
	 *
	 */
	public void startComputeSyncService() {
		MyLog.entry();
		if (mCallState == null) {
			doStartComputeSyncService();
		}
		MyLog.exit();
	}

	private void doStartComputeSyncService() {
		MyLog.entry();

		mCallState = RestCallState.RUNNING;
		mCallRunningStep = null;
		mSyncResult = null;
		mCallErrorCause = null;
		notifyCallBacks();

		final Intent startIntent = new Intent(getActivity(), ComputeSyncService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyComputeSyncReceiver(new Handler()));
		startIntent.putExtra(ComputeSyncService.EXTRA_ACCOUNT_ID_NAME, mAccountId);

		getActivity().startService(startIntent);

		MyLog.exit();
	}

	public int getAccountId() {
		return mAccountId;
	}

	public void setAccountId(int mAccountId) {
		this.mAccountId = mAccountId;
	}

}
