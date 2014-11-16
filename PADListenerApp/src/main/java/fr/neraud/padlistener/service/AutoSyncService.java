package fr.neraud.padlistener.service;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.helper.ChooseSyncInitHelper;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.PADHerderAccountModel;
import fr.neraud.padlistener.model.PushSyncStatModel;
import fr.neraud.padlistener.service.constant.RestCallError;
import fr.neraud.padlistener.service.constant.RestCallRunningStep;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * AutoSyncService that triggers an automatic sync
 * Created by Neraud on 16/11/2014.
 */
public class AutoSyncService extends IntentService {

	public AutoSyncService() {
		super("AutoSyncService");
	}

	private class MyComputeSyncReceiver extends AbstractRestResultReceiver<ComputeSyncResultModel> {

		private final int accountId;

		public MyComputeSyncReceiver(Handler handler, int accountId) {
			super(handler);
			this.accountId = accountId;
		}

		@Override
		protected void onReceiveProgress(RestCallRunningStep progress) {
			Log.d(getClass().getName(), "onReceiveProgress");

		}

		@Override
		protected void onReceiveSuccess(ComputeSyncResultModel result) {
			Log.d(getClass().getName(), "onReceiveSuccess");

			final ChooseSyncModel chooseModel = prepareSync(result);
			pushSync(accountId, chooseModel);
		}

		@Override
		protected void onReceiveError(RestCallError error, Throwable errorCause) {
			Log.d(getClass().getName(), "onReceiveError");

			finishSync();
		}
	}

	private class MyPushSyncReceiver extends ResultReceiver {

		public MyPushSyncReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			Log.d(getClass().getName(), "onReceiveResult");

			final PushSyncStatModel.ElementToPush element = PushSyncStatModel.ElementToPush.valueOf(resultData.getString(PushSyncService.RECEIVER_ELEMENT_NAME));
			final boolean isSuccess = resultData.getBoolean(PushSyncService.RECEIVER_SUCCESS_NAME);
			final String errorMessage = resultData.getString(PushSyncService.RECEIVER_MESSAGE_NAME);

			// TODO
			if (isSuccess) {
			} else {
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");

		final int accountId = extractAccountId();
		computeSync(accountId);
	}

	private int extractAccountId() {
		Log.d(getClass().getName(), "extractAccountId");

		final List<PADHerderAccountModel> accounts = new DefaultSharedPreferencesHelper(this).getPadHerderAccounts();
		final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(this);
		final String lastCaptureAccountName = techPrefHelper.getLastCaptureName();
		if(StringUtils.isNotBlank(lastCaptureAccountName)) {
			for (final PADHerderAccountModel account : accounts) {
				if (lastCaptureAccountName.equals(account.getName())) {
					return account.getAccountId();
				}
			}
		}

		// TODO
		return 1;
	}

	private void computeSync(int accountId) {
		Log.d(getClass().getName(), "computeSync : " + accountId);

		final Intent startIntent = new Intent(this, ComputeSyncService.class);
		startIntent.putExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME, new MyComputeSyncReceiver(new Handler(), accountId));
		startIntent.putExtra(ComputeSyncService.EXTRA_ACCOUNT_ID_NAME, accountId);

		startService(startIntent);

		Looper.loop();
	}


	private ChooseSyncModel prepareSync(ComputeSyncResultModel result) {
		final ChooseSyncInitHelper initHelper = new ChooseSyncInitHelper(this, result);
		final ChooseSyncModel mChooseResult = initHelper.filterSyncResult();

		if (result.isHasEncounteredUnknownMonster()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.choose_sync_alert_unknown_monster_encountered_title);
			builder.setMessage(R.string.choose_sync_alert_unknown_monster_encountered_content);
			builder.create().show();
		}

		return mChooseResult;
	}

	private void pushSync(int accountId, ChooseSyncModel chooseModel) {
		final boolean hasUserInfoToUpdate = chooseModel.getSyncedUserInfoToUpdate().getSyncedModel().hasDataToSync();
		final boolean hasUserInfoToUpdateChosen = hasUserInfoToUpdate && chooseModel.getSyncedUserInfoToUpdate().isChosen();
		//final int materialToUpdateCount = chooseModel.getSyncedMaterialsToUpdate().size();
		final int materialToUpdateChosenCount = countChosenItems(chooseModel.getSyncedMaterialsToUpdate());
		//final int monsterToUpdateCount = chooseModel.getSyncedMonsters(SyncMode.UPDATED).size();
		final int monsterToUpdateChosenCount = countChosenItems(chooseModel.getSyncedMonsters(SyncMode.UPDATED));
		//final int monsterToCreateCount = chooseModel.getSyncedMonsters(SyncMode.CREATED).size();
		final int monsterToCreateChosenCount = countChosenItems(chooseModel.getSyncedMonsters(SyncMode.CREATED));
		//final int monsterToDeleteCount = chooseModel.getSyncedMonsters(SyncMode.DELETED).size();
		final int monsterToDeleteChosenCount = countChosenItems(chooseModel.getSyncedMonsters(SyncMode.DELETED));

		if (materialToUpdateChosenCount > 0 || monsterToUpdateChosenCount > 0 || monsterToCreateChosenCount > 0
				|| monsterToDeleteChosenCount > 0 || hasUserInfoToUpdateChosen) {

			final Intent intent = new Intent(this, PushSyncService.class);
			intent.putExtra(PushSyncService.CHOOSE_SYNC_MODEL_EXTRA_NAME, chooseModel);
			intent.putExtra(PushSyncService.ACCOUNT_ID_EXTRA_NAME, accountId);
			intent.putExtra(PushSyncService.RECEIVER_EXTRA_NAME, new MyPushSyncReceiver(new Handler()));
			startService(intent);
		} else {
			// Nothing to push
		}
	}

	private static <T extends Serializable> int countChosenItems(List<ChooseSyncModelContainer<T>> list) {
		int count = 0;
		for (final ChooseSyncModelContainer<?> item : list) {
			if (item.isChosen()) {
				count++;
			}
		}
		return count;
	}

	private void finishSync() {
		Looper.myLooper().quit();
	}
}
