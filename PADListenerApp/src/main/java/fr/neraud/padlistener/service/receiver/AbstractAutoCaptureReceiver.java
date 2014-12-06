package fr.neraud.padlistener.service.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import fr.neraud.log.MyLog;

/**
 * Created by Neraud on 23/11/2014.
 */
public abstract class AbstractAutoCaptureReceiver extends ResultReceiver {

	public static final int CODE_OK = 0;
	public static final int CODE_KO = 1;
	public static final String STATE_NAME = "state";
	public static final String EXCEPTION_NAME = "exception";

	public enum State {
		INITIALIZED,
		LISTENER_STARTING,
		LISTENER_STARTED,
		STOPPING_PAD,
		STARTING_PAD,
		CAPTURE_STARTING,
		CAPTURE_FINISHED;
	}

	protected AbstractAutoCaptureReceiver(Handler handler) {
		super(handler);
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		MyLog.entry();

		if(resultCode == CODE_OK) {
			State state = (State) resultData.getSerializable(STATE_NAME);
			notifyProgress(state);
		} else {
			Exception ex = null;
			if(resultData.containsKey(EXCEPTION_NAME)) {
				ex = (Exception) resultData.getSerializable(EXCEPTION_NAME);
			}
			notifyListenerError(ex);
		}

		MyLog.exit();
	}

	protected abstract void notifyProgress(State state);

	protected abstract void notifyListenerError(Exception error);
}