package fr.neraud.padlistener.service.receiver;

import android.content.Context;
import android.os.Handler;

import fr.neraud.padlistener.helper.SyncNotificationHelper;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;

/**
 * Created by Neraud on 30/11/2014.
 */
public class AutoSyncReceiver extends AbstractAutoSyncReceiver {

	final SyncNotificationHelper mSyncNotificationHelper;

	public AutoSyncReceiver(Context context, Handler handler) {
		super(handler);
		mSyncNotificationHelper = new SyncNotificationHelper(context);
	}

	@Override
	protected void notifyInitialized() {
		mSyncNotificationHelper.notifyInitialized();
	}

	@Override
	protected void notifyComputeSyncInitialized() {
		mSyncNotificationHelper.notifyComputeSyncInitialized();

	}

	@Override
	protected void notifyComputeSyncProgress(RestCallRunningStep progress) {
		mSyncNotificationHelper.notifyComputeSyncProgress(progress);

	}

	@Override
	protected void notifyComputeSyncFinished() {
		mSyncNotificationHelper.notifyComputeSyncFinished();

	}

	@Override
	protected void notifyPushSyncInitialized() {
		mSyncNotificationHelper.notifyPushSyncInitialized();

	}

	@Override
	protected void notifyPushSyncProgress(int itemsPushedCount, int itemsToPushTotal) {
		mSyncNotificationHelper.notifyPushSyncProgress(itemsPushedCount, itemsToPushTotal);

	}

	@Override
	protected void notifyPushSyncFinished(int itemsPushedCount) {
		mSyncNotificationHelper.notifyPushSyncFinished(itemsPushedCount);

	}

	@Override
	public void notifyError(Error error, Throwable t) {
		mSyncNotificationHelper.notifyError(error, t);
	}
}
