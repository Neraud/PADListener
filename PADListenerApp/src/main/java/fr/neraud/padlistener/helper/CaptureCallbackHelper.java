package fr.neraud.padlistener.helper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import fr.neraud.padlistener.R;

/**
 * Helper to notify the user when the Listener captures data
 * Created by Neraud on 28/06/2014.
 */
public class CaptureCallbackHelper {

	private final Context context;

	public CaptureCallbackHelper(Context context) {
		this.context = context;
	}


	/**
	 * Notify the start of a capture process
	 */
	public void notifyCaptureStarted() {
		Log.d(getClass().getName(), "notifyCaptureStarted");
	}

	/**
	 * Notify the end of a capture process
	 *
	 * @param accountName the account name captured
	 */
	public void notifyCaptureFinished(String accountName) {
		Log.d(getClass().getName(), "notifyCaptureFinished : " + accountName);
		final String toastMessage = context.getString(R.string.toast_data_captured, accountName);
		displayToast(toastMessage);
	}

	private void displayToast(final String toastMessage) {
		new Handler(context.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
			}
		});
	}
}
