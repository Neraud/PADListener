package fr.neraud.padlistener.helper;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import fr.neraud.padlistener.R;

/**
 * Helper to notify the user when the Listener captures data
 * Created by Neraud on 28/06/2014.
 */
public class CaptureNotificationHelper {

	private final Context context;

	public CaptureNotificationHelper(Context context) {
		this.context = context;
	}

	/**
	 * Notify the start of a capture process
	 */
	public void notifyCaptureStarted() {
		Log.d(getClass().getName(), "notifyCaptureStarted");
		final String notificationMessage = context.getString(R.string.notification_data_capture_started);
		displayNotification(notificationMessage);
	}

	/**
	 * Notify the end of a capture process
	 *
	 * @param accountName the account name captured
	 */
	public void notifyCaptureFinished(String accountName) {
		Log.d(getClass().getName(), "notifyCaptureFinished : " + accountName);

		final String toastMessage = context.getString(R.string.toast_data_capture_finished, accountName);
		displayToast(toastMessage);

		final String notificationMessage = context.getString(R.string.notification_data_capture_finished, accountName);
		displayNotification(notificationMessage);
	}

	private void displayToast(final String toastMessage) {
		new Handler(context.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void displayNotification(final String notificationMessage) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_notification);
		mBuilder.setWhen(new Date().getTime());
		mBuilder.setContentTitle(context.getString(R.string.notification_data_capture_title));
		mBuilder.setContentText(notificationMessage);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(0, mBuilder.build());
	}
}
