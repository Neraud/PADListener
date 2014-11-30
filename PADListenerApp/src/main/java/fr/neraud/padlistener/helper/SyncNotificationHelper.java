package fr.neraud.padlistener.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
		Log.d(getClass().getName(), "notifyInitialized");
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_started);
		displayNotification(notificationMessage);
	}

	public void notifyComputeSyncInitialized() {
		Log.d(getClass().getName(), "notifyComputeSyncInitialized");
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_computing);
		displayNotification(notificationMessage);
	}

	public void notifyComputeSyncProgress(RestCallRunningStep progress) {
		Log.d(getClass().getName(), "notifyComputeSyncProgress : " + progress);
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
	}

	public void notifyComputeSyncFinished() {
		Log.d(getClass().getName(), "notifyComputeSyncFinished");
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_computed);
		displayNotification(notificationMessage);
	}

	public void notifyPushSyncInitialized() {
		Log.d(getClass().getName(), "notifyPushSyncInitialized");
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_pushing);
		displayNotification(notificationMessage);
	}

	public void notifyPushSyncProgress(int itemsPushedCount, int itemsToPushTotal) {
		Log.d(getClass().getName(), "notifyPushSyncProgress : " + itemsPushedCount + "/" + itemsToPushTotal);
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_pushing);
		displayNotificationWithProgress(notificationMessage, itemsPushedCount, itemsToPushTotal);
	}

	public void notifyPushSyncFinished(int itemsPushedCount) {
		Log.d(getClass().getName(), "notifyPushSyncFinished : " + itemsPushedCount);
		final String notificationMessage = mContext.getString(R.string.notification_data_sync_push_finished, itemsPushedCount);
		displayNotification(notificationMessage);
	}

	public void notifyError(AbstractAutoSyncReceiver.Error error, Throwable t) {
		Log.d(getClass().getName(), "notifyError : " + error);
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
	}
}
