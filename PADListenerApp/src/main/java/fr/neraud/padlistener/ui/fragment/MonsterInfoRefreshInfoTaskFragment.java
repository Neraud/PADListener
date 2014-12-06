package fr.neraud.padlistener.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.service.FetchPadHerderMonsterInfoService;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * MonsterInfo retained fragment to store the RefreshInfo progression
 *
 * @author Neraud
 */
public class MonsterInfoRefreshInfoTaskFragment extends Fragment {

	private boolean mAutoStart = false;
	private RestCallState mCallState = null;
	private RestCallRunningStep mCallRunningStep = null;
	private Throwable mCallErrorCause = null;
	private ProgressCallBacks mProgressCallbacks = null;

	public static interface ProgressCallBacks {

		public void updateCallState(RestCallState callState, RestCallRunningStep callRunningStep, Throwable callErrorCause);
	}

	private class MyResultReceiver extends AbstractRestResultReceiver<Integer> {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep step) {
			MyLog.entry("step = " + step);
			mCallState = RestCallState.RUNNING;
			mCallRunningStep = step;
			notifyProgressCallBacks();
			MyLog.exit();
		}

		@Override
		protected void onReceiveSuccess(Integer result) {
			MyLog.entry();
			mCallState = RestCallState.SUCCEEDED;
			notifyProgressCallBacks();
			MyLog.exit();
		}

		@Override
		protected void onReceiveError(RestCallError error, Throwable cause) {
			MyLog.entry("error = " + error);
			mCallState = RestCallState.FAILED;
			mCallErrorCause = cause;
			notifyProgressCallBacks();
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
	public void onAttach(Activity activity) {
		MyLog.entry();
		super.onAttach(activity);

		if(mAutoStart && mCallState == null) {
			startFetchInfoService();
		}
		MyLog.exit();
	}

	@Override
	public void onDetach() {
		MyLog.entry();
		super.onDetach();
		mProgressCallbacks = null;
		MyLog.exit();
	}

	public void registerCallbacks(ProgressCallBacks callbacks) {
		this.mProgressCallbacks = callbacks;
		notifyProgressCallBacks();
	}

	private void notifyProgressCallBacks() {
		if (mProgressCallbacks != null) {
			mProgressCallbacks.updateCallState(mCallState, mCallRunningStep, mCallErrorCause);
		}
	}

	public void startFetchInfoService() {
		MyLog.entry();
		mCallState = RestCallState.RUNNING;
		mCallRunningStep = null;
		mCallErrorCause = null;
		notifyProgressCallBacks();

		final Intent startIntent = new Intent(getActivity(), FetchPadHerderMonsterInfoService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
		getActivity().startService(startIntent);
		MyLog.exit();
	}

	public boolean isAutoStart() {
		return mAutoStart;
	}

	public void setAutoStart(boolean autoStart) {
		mAutoStart = autoStart;
	}
}
