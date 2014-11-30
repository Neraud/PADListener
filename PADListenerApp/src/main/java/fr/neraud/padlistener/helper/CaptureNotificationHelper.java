package fr.neraud.padlistener.helper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.service.ListenerService;

/**
 * Helper to notify the user when the Listener captures data
 * Created by Neraud on 28/06/2014.
 */
public class CaptureNotificationHelper implements ListenerService.CaptureListener {

	private final Context mContext;

	public CaptureNotificationHelper(Context context) {
		this.mContext = context;
	}

	/**
	 * Notify the start of a capture process
	 */
	public void notifyCaptureStarted() {
		Log.d(getClass().getName(), "notifyCaptureStarted");
		final String notificationMessage = mContext.getString(R.string.notification_data_capture_started);
		displayNotification(notificationMessage);

		if(needsToShutDownListener()) {
			stopListener();
		}
	}

	@Override
	public void notifySavingMonsters(int num, int total, MonsterModel monster) {
		Log.d(getClass().getName(), "notifySavingMonsters : " + num);
		final String notificationMessage = mContext.getString(R.string.notification_data_capture_saving_monsters, monster.getIdJp());
		displayNotificationWithProgress(notificationMessage, num, total);
	}

	@Override
	public void notifySavingFriends(int num, int total, CapturedFriendModel friend) {
		Log.d(getClass().getName(), "notifySavingFriends : " + num);
		final String notificationMessage = mContext.getString(R.string.notification_data_capture_saving_friends, friend.getId(), friend.getName());
		displayNotificationWithProgress(notificationMessage, num, total);
	}

	/**
	 * Notify the end of a capture process
	 *
	 * @param accountName the account name captured
	 */
	public void notifyCaptureFinished(String accountName) {
		Log.d(getClass().getName(), "notifyCaptureFinished : " + accountName);

		final String toastMessage = mContext.getString(R.string.toast_data_capture_finished, accountName);
		displayToast(toastMessage);

		final String notificationMessage = mContext.getString(R.string.notification_data_capture_finished, accountName);
		displayNotification(notificationMessage);
	}

	private void displayToast(final String toastMessage) {
		new Handler(mContext.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
			}
		});
	}

	private NotificationCompat.Builder prepareNotification() {
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setWhen(new Date().getTime());
		builder.setContentTitle(mContext.getString(R.string.notification_data_capture_title));

		return builder;
	}

	private void showNotification(NotificationCompat.Builder builder) {
		NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MyNotification.ONGOING_CAPTURE.getId(), builder.build());
	}

	private void displayNotification(final String notificationMessage) {
		final NotificationCompat.Builder builder = prepareNotification();
		builder.setContentText(notificationMessage);
		showNotification(builder);
	}

	private void displayNotificationWithProgress(final String notificationMessage, int nb, int max) {
		final NotificationCompat.Builder builder = prepareNotification();
		builder.setContentText(notificationMessage);
		builder.setProgress(max, nb, false);
		showNotification(builder);
	}

	protected boolean needsToShutDownListener() {
		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(mContext);
		return helper.isListenerAutoShutdown();
	}

	private void stopListener() {
		Log.d(getClass().getName(), "stopListener");
		final Intent serviceIntent = new Intent(mContext, ListenerService.class);
		mContext.stopService(serviceIntent);
	}
}
