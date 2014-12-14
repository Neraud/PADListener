package fr.neraud.padlistener.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.pad.model.PADCapturedFriendModel;
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
		MyLog.entry();

		final String notificationMessage = mContext.getString(R.string.notification_data_capture_started);
		displayNotification(notificationMessage);

		if (needsToShutDownListener()) {
			stopListener();
		}

		MyLog.exit();
	}

	@Override
	public void notifySavingMonsters(int num, int total, MonsterModel monster) {
		MyLog.entry("num = " + num);

		final String notificationMessage = mContext.getString(R.string.notification_data_capture_saving_monsters, monster.getIdJp());
		displayNotificationWithProgress(notificationMessage, num, total);

		MyLog.exit();
	}

	@Override
	public void notifySavingFriends(int num, int total, PADCapturedFriendModel friend) {
		MyLog.entry("num = " + num);

		final String notificationMessage = mContext.getString(R.string.notification_data_capture_saving_friends, friend.getId(), friend.getName());
		displayNotificationWithProgress(notificationMessage, num, total);

		MyLog.exit();
	}

	/**
	 * Notify the end of a capture process
	 *
	 * @param accountName the account name captured
	 */
	public void notifyCaptureFinished(String accountName) {
		MyLog.entry("accountName = " + accountName);

		final String toastMessage = mContext.getString(R.string.toast_data_capture_finished, accountName);
		displayToast(toastMessage);

		final String notificationMessage = mContext.getString(R.string.notification_data_capture_finished, accountName);
		displayNotification(notificationMessage);

		MyLog.exit();
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
		MyLog.entry();
		final Intent serviceIntent = new Intent(mContext, ListenerService.class);
		mContext.stopService(serviceIntent);
		MyLog.exit();
	}
}
