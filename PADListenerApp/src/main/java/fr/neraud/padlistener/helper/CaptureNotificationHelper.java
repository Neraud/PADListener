package fr.neraud.padlistener.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.service.ListenerService;

/**
 * Helper to notify the user when the Listener captures data
 * Created by Neraud on 28/06/2014.
 */
public class CaptureNotificationHelper extends NotificationHelper implements ListenerService.CaptureListener {

	private final Context mContext;

	public CaptureNotificationHelper(Context context) {
		super(context, MyNotification.ONGOING_CAPTURE, R.string.notification_data_capture_title);
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
