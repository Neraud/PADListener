
package fr.neraud.padlistener.gui.fragment;

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

	private RestCallState state = null;
	private RestCallRunningStep runningStep = null;
	private String errorMessage = null;

	private CallBacks callbacks = null;

	public static interface CallBacks {

		public void updateState(RestCallState state, RestCallRunningStep runningStep, String errorMessage);
	}

	private class MyResultReceiver extends AbstractRestResultReceiver<Integer> {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep step) {
			Log.d(getClass().getName(), "onReceiveProgress : " + step);
			state = RestCallState.RUNNING;
			runningStep = step;
			notifyCallBacks();
		}

		@Override
		protected void onReceiveSuccess(Integer result) {
			Log.d(getClass().getName(), "onReceiveSuccess");
			state = RestCallState.SUCCESSED;
			notifyCallBacks();
		}

		@Override
		protected void onReceiveError(RestCallError error, String message) {
			Log.d(getClass().getName(), "onReceiveError : " + error);
			state = RestCallState.FAILED;
			errorMessage = message;
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
			callbacks.updateState(state, runningStep, errorMessage);
		}
	}

	public void startFetchInfoService() {
		Log.d(getClass().getName(), "startFetchInfoService");
		state = RestCallState.RUNNING;
		runningStep = null;
		errorMessage = null;
		notifyCallBacks();

		final Intent startIntent = new Intent(getActivity(), FetchPadHerderMonsterInfoService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyResultReceiver(new Handler()));
		getActivity().startService(startIntent);
	}
}
