package fr.neraud.padlistener.service.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;

/**
 * Created by Neraud on 23/11/2014.
 */
public abstract class AbstractAutoSyncReceiver extends ResultReceiver {

	public static final String COMPUTE_SYNC_PROGRESS = "";
	public static final String ERROR_NAME = "error";
	public static final String EXCEPTION_NAME = "exception";
	public static final String PUSH_SYNC_ITEMS_PUSHED = "push_items_pushed";
	public static final String PUSH_SYNC_ITEMS_TO_PUSH = "push_items_to_push";


	public static enum ResultCode {

		INITIALIZED,
		COMPUTING_SYNC_INITIALIZED,
		COMPUTING_SYNC_PROGRESS,
		COMPUTING_SYNC_FINISHED,
		PUSHING_SYNC_INITIALIZED,
		PUSHING_SYNC_PROGRESS,
		PUSHING_SYNC_FINISHED,
		ERROR;

		public int getCode() {
			return ordinal();
		}

		public static ResultCode fromCode(int code) {
			for (final ResultCode codeEnum : values()) {
				if (codeEnum.ordinal() == code) return codeEnum;
			}
			return null;
		}
	}

	public enum Error {
		NO_MATCHING_ACCOUNT,
		COMPUTE,
		PUSH;
	}

	protected AbstractAutoSyncReceiver(Handler handler) {
		super(handler);
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		MyLog.entry();

		switch (ResultCode.fromCode(resultCode)) {
			case INITIALIZED:
				notifyInitialized();
				break;
			case COMPUTING_SYNC_INITIALIZED:
				notifyComputeSyncInitialized();
				break;
			case COMPUTING_SYNC_PROGRESS:
				final RestCallRunningStep progress = (RestCallRunningStep) resultData.getSerializable(AbstractAutoSyncReceiver.COMPUTE_SYNC_PROGRESS);
				notifyComputeSyncProgress(progress);
				break;
			case COMPUTING_SYNC_FINISHED:
				notifyComputeSyncFinished();
				break;
			case PUSHING_SYNC_INITIALIZED:
				notifyPushSyncInitialized();
				break;
			case PUSHING_SYNC_PROGRESS:
				final int itemsPushedCount = resultData.getInt(AbstractAutoSyncReceiver.PUSH_SYNC_ITEMS_PUSHED);
				final int itemsToPushTotal = resultData.getInt(AbstractAutoSyncReceiver.PUSH_SYNC_ITEMS_TO_PUSH);
				notifyPushSyncProgress(itemsPushedCount, itemsToPushTotal);
				break;
			case PUSHING_SYNC_FINISHED:
				final int itemsFinishedCount = resultData.getInt(AbstractAutoSyncReceiver.PUSH_SYNC_ITEMS_PUSHED);
				notifyPushSyncFinished(itemsFinishedCount);
				break;
			case ERROR:
				final Error error = (Error) resultData.getSerializable(ERROR_NAME);
				Throwable ex = null;
				if (resultData.containsKey(EXCEPTION_NAME)) {
					ex = (Throwable) resultData.getSerializable(EXCEPTION_NAME);
				}
				notifyError(error, ex);
				break;
			default:
		}

		MyLog.exit();
	}

	protected abstract void notifyInitialized();

	protected abstract void notifyComputeSyncInitialized();

	protected abstract void notifyComputeSyncProgress(RestCallRunningStep progress);

	protected abstract void notifyComputeSyncFinished();

	protected abstract void notifyPushSyncInitialized();

	protected abstract void notifyPushSyncProgress(int itemsPushedCount, int itemsToPushTotal);

	protected abstract void notifyPushSyncFinished(int itemsPushedCount);

	protected abstract void notifyError(AbstractAutoSyncReceiver.Error error, Throwable t);


}