
package fr.neraud.padlistener.service.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import fr.neraud.padlistener.service.constant.RestCallProgress;

public abstract class AbstractRestResultReceiver extends ResultReceiver {

	public static final String RECEIVER_EXTRA_NAME = "receiver";

	public AbstractRestResultReceiver(Handler handler) {
		super(handler);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(getClass().getName(), "onReceiveResult");
		super.onReceiveResult(resultCode, resultData);

		//final RestCallState state = RestCallState.findByCode(resultCode);

		final String progressString = resultData.getString("progress");
		final RestCallProgress progress = RestCallProgress.valueOf(progressString);
		onReceiveProgress(progress);
	}

	protected abstract void onReceiveProgress(RestCallProgress progress);
}
