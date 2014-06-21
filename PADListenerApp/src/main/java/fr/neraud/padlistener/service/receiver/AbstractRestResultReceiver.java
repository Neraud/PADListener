package fr.neraud.padlistener.service.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.Serializable;

import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.constant.RestCallState;

/**
 * Base ResultReceiver to track the progress of a Rest call
 *
 * @author Neraud
 */
public abstract class AbstractRestResultReceiver<R extends Serializable> extends ResultReceiver {

	public static final String RECEIVER_EXTRA_NAME = "receiver";

	public static final String RECEIVER_BUNDLE_STEP_NAME = "step";
	public static final String RECEIVER_BUNDLE_RESULT_NAME = "result";
	public static final String RECEIVER_BUNDLE_ERROR_NAME = "error";
	public static final String RECEIVER_BUNDLE_ERROR_CAUSE = "errorCause";

	public AbstractRestResultReceiver(Handler handler) {
		super(handler);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(getClass().getName(), "onReceiveResult");
		super.onReceiveResult(resultCode, resultData);

		//final RestCallState state = RestCallState.findByCode(resultCode);

		final RestCallState progress = RestCallState.findByCode(resultCode);

		switch (progress) {
			case RUNNING:
				final String stepString = resultData.getString(AbstractRestResultReceiver.RECEIVER_BUNDLE_STEP_NAME);
				final RestCallRunningStep step = RestCallRunningStep.valueOf(stepString);
				onReceiveProgress(step);
				break;
			case SUCCEEDED:
				final R result = (R) resultData.getSerializable(AbstractRestResultReceiver.RECEIVER_BUNDLE_RESULT_NAME);
				onReceiveSuccess(result);
				break;
			case FAILED:
				final String errorString = resultData.getString(AbstractRestResultReceiver.RECEIVER_BUNDLE_ERROR_NAME);
				final RestCallError error = RestCallError.valueOf(errorString);
				final Throwable errorCause = (Throwable) resultData
						.getSerializable(AbstractRestResultReceiver.RECEIVER_BUNDLE_ERROR_CAUSE);
				onReceiveError(error, errorCause);
				break;
			default:
				break;
		}
	}

	protected abstract void onReceiveProgress(RestCallRunningStep progress);

	protected abstract void onReceiveSuccess(R result);

	protected abstract void onReceiveError(RestCallError error, Throwable errorCause);

}
