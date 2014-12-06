package fr.neraud.padlistener.helper;

import android.content.Context;
import android.widget.Toast;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.exception.NoMatchingAccountException;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.receiver.AbstractAutoSyncReceiver;

/**
 * Created by Neraud on 30/11/2014.
 */
public class SyncNotificationHelper extends NotificationHelper {

	private final Context mContext;

	public SyncNotificationHelper(Context context) {
		super(context, MyNotification.ONGOING_SYNC, R.string.notification_data_sync_title);
		this.mContext = context;
	}

	public void notifyInitialized() {
		MyLog.entry();
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_started);
		displayNotification(notificationMessage);
		MyLog.exit();
	}

	public void notifyComputeSyncInitialized() {
		MyLog.entry();
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_computing);
		displayNotification(notificationMessage);
		MyLog.exit();
	}

	public void notifyComputeSyncProgress(RestCallRunningStep progress) {
		MyLog.entry("progress = " + progress);

		String notificationMessage = "";
		switch (progress) {
			case STARTED:
				notificationMessage = mContext.getString(R.string.notification_data_sync_computing_calling);
				break;
			case RESPONSE_RECEIVED:
				notificationMessage = mContext.getString(R.string.notification_data_sync_computing_parsing);
				break;
			case RESPONSE_PARSED:
				notificationMessage = mContext.getString(R.string.notification_data_sync_computing_computing);
				break;
		}
		displayNotification(notificationMessage);
		MyLog.exit();
	}

	public void notifyComputeSyncFinished() {
		MyLog.entry();
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_computed);
		displayNotification(notificationMessage);
		MyLog.exit();
	}

	public void notifyPushSyncInitialized() {
		MyLog.entry();
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_pushing);
		displayNotification(notificationMessage);
		MyLog.exit();
	}

	public void notifyPushSyncProgress(int itemsPushedCount, int itemsToPushTotal) {
		MyLog.entry(itemsPushedCount + "/" + itemsToPushTotal);
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_pushing);
		displayNotificationWithProgress(notificationMessage, itemsPushedCount, itemsToPushTotal);
		MyLog.exit();
	}

	public void notifyPushSyncFinished(int itemsPushedCount) {
		MyLog.entry("itemsPushedCount = " + itemsPushedCount);
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_push_finished, itemsPushedCount);
		displayNotification(notificationMessage);
		MyLog.exit();
	}

	public void notifyError(AbstractAutoSyncReceiver.Error error, Throwable t) {
		MyLog.entry("error = " + error);
		switch (error) {
			case NO_MATCHING_ACCOUNT:

				String accountName = "";
				if (t instanceof NoMatchingAccountException) {
					accountName = ((NoMatchingAccountException) t).getAccountName();
				}
				final String content = mContext.getString(R.string.auto_sync_no_matching_account_content, accountName);
				Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
				break;
			case COMPUTE:
				Toast.makeText(mContext, R.string.auto_sync_compute_failed, Toast.LENGTH_LONG).show();
				break;
			case PUSH:
				Toast.makeText(mContext, R.string.auto_sync_push_failed, Toast.LENGTH_LONG).show();
				break;
		}
		MyLog.exit();
	}
}
