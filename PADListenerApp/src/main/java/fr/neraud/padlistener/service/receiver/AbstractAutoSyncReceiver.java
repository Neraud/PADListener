package fr.neraud.padlistener.service.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by Neraud on 23/11/2014.
 */
public abstract class AbstractAutoSyncReceiver extends ResultReceiver {

	public static final int CODE_OK = 0;
	public static final int CODE_KO = 1;
	public static final String STATE_NAME = "state";
	public static final String ERROR_NAME = "error";
	public static final String EXCEPTION_NAME = "exception";

	public enum State {
		INITIALIZED,
		COMPUTE_STARTED,
		COMPUTE_FINISHED,
		PUSH_STARTED,
		NOTHING_TO_PUSH,
		PUSH_FINISHED;
	}

	public enum Error {
		NO_MATCHING_ACCOUNT,
		COMPUTE,
		PUSH;
	}

	protected AbstractAutoSyncReceiver(Handler handler) {
		super(handler);
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(getClass().getName(), "onReceiveResult");
		if(resultCode == CODE_OK) {
			State state = (State) resultData.getSerializable(STATE_NAME);
			notifyProgress(state);
		} else {
			Error error = (Error) resultData.getSerializable(ERROR_NAME);
			Throwable ex = null;
			if(resultData.containsKey(EXCEPTION_NAME)) {
				ex = (Throwable) resultData.getSerializable(EXCEPTION_NAME);
			}
			notifyError(error, ex);
		}
	}

	protected abstract void notifyProgress(State state);

	protected abstract void notifyError(Error error, Throwable t);
}