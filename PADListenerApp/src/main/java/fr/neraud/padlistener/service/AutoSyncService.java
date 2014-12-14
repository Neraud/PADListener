package fr.neraud.padlistener.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.exception.NoMatchingAccountException;
import fr.neraud.padlistener.helper.ChooseSyncInitHelper;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.PADHerderAccountModel;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.receiver.AbstractAutoSyncReceiver;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * AutoSyncService that triggers an automatic sync
 * Created by Neraud on 16/11/2014.
 */
public class AutoSyncService extends IntentService {

	private static final String SYNC_LISTENER_EXTRA_NAME = "syncListener";
	private ResultReceiver mAutoSyncReceiver;
	private int mItemsPushedCount;
	private int mItemsToPushTotal;

	public AutoSyncService() {
		super("AutoSyncService");
	}

	public static void addSyncListenerInIntent(Intent intent, ResultReceiver receiver) {
		intent.putExtra(SYNC_LISTENER_EXTRA_NAME, receiver);
	}

	private class MyComputeSyncReceiver extends AbstractRestResultReceiver<ComputeSyncResultModel> {

		private final int accountId;

		public MyComputeSyncReceiver(Handler handler, int accountId) {
			super(handler);
			this.accountId = accountId;
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep progress) {
			MyLog.entry();
			notifyComputeSyncProgress(progress);
			MyLog.exit();
		}

		@Override
		protected void onReceiveSuccess(ComputeSyncResultModel result) {
			MyLog.entry();

			final ChooseSyncModel chooseModel = prepareSync(result);
			notifyComputeSyncFinished();
			pushSync(accountId, chooseModel);

			MyLog.exit();
		}

		@Override
		protected void onReceiveError(RestCallError error, Throwable errorCause) {
			MyLog.entry();

			notifyError(AbstractAutoSyncReceiver.Error.COMPUTE, errorCause);
			finishSync();

			MyLog.exit();
		}
	}

	private class MyPushSyncReceiver extends ResultReceiver {

		public MyPushSyncReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			MyLog.entry();

			//final PushSyncStatModel.ElementToPush element = PushSyncStatModel.ElementToPush.valueOf(resultData.getString(PushSyncService.RECEIVER_ELEMENT_NAME));
			final boolean isSuccess = resultData.getBoolean(PushSyncService.RECEIVER_SUCCESS_NAME);
			final String errorMessage = resultData.getString(PushSyncService.RECEIVER_MESSAGE_NAME);

			if (isSuccess) {
				mItemsPushedCount++;
				if (mItemsPushedCount < mItemsToPushTotal) {
					notifyPushSyncProgress();
				} else {
					notifyPushSyncFinished();
				}
			} else {
				notifyError(AbstractAutoSyncReceiver.Error.PUSH, new Exception(errorMessage));
			}

			MyLog.exit();
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		MyLog.entry();

		this.mAutoSyncReceiver = intent.getParcelableExtra(SYNC_LISTENER_EXTRA_NAME);
		notifyInitialized();

		try {
			final int accountId = extractAccountId();
			computeSync(accountId);
		} catch (NoMatchingAccountException e) {
			notifyError(AbstractAutoSyncReceiver.Error.NO_MATCHING_ACCOUNT, e);
		}

		MyLog.exit();
	}

	private int extractAccountId() throws NoMatchingAccountException {
		MyLog.entry();

		final List<PADHerderAccountModel> accounts = new DefaultSharedPreferencesHelper(this).getPadHerderAccounts();
		final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(this);
		final String lastCaptureAccountName = techPrefHelper.getLastCaptureName();
		if (StringUtils.isNotBlank(lastCaptureAccountName)) {
			for (final PADHerderAccountModel account : accounts) {
				if (lastCaptureAccountName.equals(account.getName())) {
					MyLog.exit();
					return account.getAccountId();
				}
			}
		}

		throw new NoMatchingAccountException(lastCaptureAccountName);
	}

	private void computeSync(int accountId) {
		MyLog.entry();

		final Intent startIntent = new Intent(this, ComputeSyncService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyComputeSyncReceiver(new Handler(), accountId));
		startIntent.putExtra(ComputeSyncService.EXTRA_ACCOUNT_ID_NAME, accountId);

		notifyComputeSyncInitialized();
		startService(startIntent);

		Looper.loop();

		MyLog.exit();
	}

	private ChooseSyncModel prepareSync(ComputeSyncResultModel result) {
		MyLog.entry();

		final ChooseSyncInitHelper initHelper = new ChooseSyncInitHelper(this, result);
		final ChooseSyncModel mChooseResult = initHelper.filterSyncResult();

		if (result.isHasEncounteredUnknownMonster()) {
			MyLog.info("Unknown monsters encountered");
			Toast.makeText(this, R.string.choose_sync_alert_unknown_monster_encountered_toast, Toast.LENGTH_LONG).show();
		}

		MyLog.exit();
		return mChooseResult;
	}

	private void pushSync(int accountId, ChooseSyncModel chooseModel) {
		MyLog.entry();

		notifyPushSyncInitialized();

		final boolean hasUserInfoToUpdate = chooseModel.getSyncedUserInfoToUpdate().getModel().hasDataToSync();
		final boolean hasUserInfoToUpdateChosen = hasUserInfoToUpdate && chooseModel.getSyncedUserInfoToUpdate().isChosen();
		final int materialToUpdateChosenCount = countChosenItems(chooseModel.getSyncedMaterialsToUpdate());
		final int monsterToUpdateChosenCount = countChosenItems(chooseModel.getSyncedMonsters(SyncMode.UPDATED));
		final int monsterToCreateChosenCount = countChosenItems(chooseModel.getSyncedMonsters(SyncMode.CREATED));
		final int monsterToDeleteChosenCount = countChosenItems(chooseModel.getSyncedMonsters(SyncMode.DELETED));

		mItemsPushedCount = 0;
		mItemsToPushTotal = (hasUserInfoToUpdateChosen ? 1 : 0) +
				materialToUpdateChosenCount +
				monsterToUpdateChosenCount +
				monsterToCreateChosenCount +
				monsterToDeleteChosenCount;

		if (mItemsToPushTotal > 0) {
			final Intent intent = new Intent(this, PushSyncService.class);
			intent.putExtra(PushSyncService.CHOOSE_SYNC_MODEL_EXTRA_NAME, chooseModel);
			intent.putExtra(PushSyncService.ACCOUNT_ID_EXTRA_NAME, accountId);
			intent.putExtra(PushSyncService.RECEIVER_EXTRA_NAME, new MyPushSyncReceiver(new Handler()));
			startService(intent);
		} else {
			notifyPushSyncFinished();
		}

		MyLog.exit();
	}

	private static <T extends Serializable> int countChosenItems(List<ChooseModelContainer<T>> list) {
		int count = 0;
		for (final ChooseModelContainer<?> item : list) {
			if (item.isChosen()) {
				count++;
			}
		}
		return count;
	}

	private void finishSync() {
		Looper.myLooper().quit();
	}

	private void notifyInitialized() {
		if (mAutoSyncReceiver != null) {
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.INITIALIZED.getCode(), null);
		}
	}

	private void notifyComputeSyncInitialized() {
		if (mAutoSyncReceiver != null) {
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.COMPUTING_SYNC_INITIALIZED.getCode(), null);
		}
	}

	private void notifyComputeSyncProgress(RestCallRunningStep progress) {
		if (mAutoSyncReceiver != null) {
			final Bundle data = new Bundle();
			data.putSerializable(AbstractAutoSyncReceiver.COMPUTE_SYNC_PROGRESS, progress);
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.COMPUTING_SYNC_PROGRESS.getCode(), data);
		}
	}

	private void notifyComputeSyncFinished() {
		if (mAutoSyncReceiver != null) {
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.COMPUTING_SYNC_FINISHED.getCode(), null);
		}
	}

	private void notifyPushSyncInitialized() {
		if (mAutoSyncReceiver != null) {
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.PUSHING_SYNC_INITIALIZED.getCode(), null);
		}
	}

	private void notifyPushSyncProgress() {
		if (mAutoSyncReceiver != null) {
			final Bundle data = new Bundle();
			data.putInt(AbstractAutoSyncReceiver.PUSH_SYNC_ITEMS_PUSHED, mItemsPushedCount);
			data.putInt(AbstractAutoSyncReceiver.PUSH_SYNC_ITEMS_TO_PUSH, mItemsToPushTotal);
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.PUSHING_SYNC_PROGRESS.getCode(), data);
		}
	}

	private void notifyPushSyncFinished() {
		if (mAutoSyncReceiver != null) {
			final Bundle data = new Bundle();
			data.putInt(AbstractAutoSyncReceiver.PUSH_SYNC_ITEMS_PUSHED, mItemsPushedCount);
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.PUSHING_SYNC_FINISHED.getCode(), data);
		}
	}

	private void notifyError(AbstractAutoSyncReceiver.Error error, Throwable t) {
		if (mAutoSyncReceiver != null) {
			final Bundle data = new Bundle();
			data.putSerializable(AbstractAutoSyncReceiver.ERROR_NAME, error);
			data.putSerializable(AbstractAutoSyncReceiver.EXCEPTION_NAME, t);
			mAutoSyncReceiver.send(AbstractAutoSyncReceiver.ResultCode.ERROR.getCode(), data);
		}
	}
}
