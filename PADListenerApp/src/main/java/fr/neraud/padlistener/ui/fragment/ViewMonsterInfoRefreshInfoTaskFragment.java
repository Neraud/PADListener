package fr.neraud.padlistener.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import fr.neraud.padlistener.service.FetchPadHerderMonsterInfoService;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * ViewMonsterInfo retained fragment to store the RefreshInfo progression
 *
 * @author Neraud
 */
public class ViewMonsterInfoRefreshInfoTaskFragment extends Fragment {

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
			Log.d(getClass().getName(), "onReceiveProgress : " + step);
			mCallState = RestCallState.RUNNING;
			mCallRunningStep = step;
			notifyProgressCallBacks();
		}

		@Override
		protected void onReceiveSuccess(Integer result) {
			Log.d(getClass().getName(), "onReceiveSuccess");
			mCallState = RestCallState.SUCCEEDED;
			notifyProgressCallBacks();
		}

		@Override
		protected void onReceiveError(RestCallError error, Throwable cause) {
			Log.d(getClass().getName(), "onReceiveError : " + error);
			mCallState = RestCallState.FAILED;
			mCallErrorCause = cause;
			notifyProgressCallBacks();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(getClass().getName(), "onAttach");

		if(mAutoStart && mCallState == null) {
			startFetchInfoService();
		}
	}

	@Override
	public void onDetach() {
		Log.d(getClass().getName(), "onDetach");
		super.onDetach();
		mProgressCallbacks = null;
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
		Log.d(getClass().getName(), "startFetchInfoService");
		mCallState = RestCallState.RUNNING;
		mCallRunningStep = null;
		mCallErrorCause = null;
		notifyProgressCallBacks();

		final Intent startIntent = new Intent(getActivity(), FetchPadHerderMonsterInfoService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
		getActivity().startService(startIntent);
	}

	public boolean isAutoStart() {
		return mAutoStart;
	}

	public void setAutoStart(boolean autoStart) {
		mAutoStart = autoStart;
	}
}
