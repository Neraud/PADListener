package fr.neraud.padlistener.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.helper.PushSyncHelper;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.PushSyncStatModel.ElementToPush;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.SyncedUserInfoModel;
import fr.neraud.padlistener.service.receiver.AbstractRestResultReceiver;

/**
 * IntentService used to push sync to PADherder
 *
 * @author Neraud
 */
public class PushSyncService extends IntentService {

	public static final String CHOOSE_SYNC_MODEL_EXTRA_NAME = "chooseSyncModel";
	public static final String ACCOUNT_ID_EXTRA_NAME = "accountId";
	public static final String RECEIVER_EXTRA_NAME = "receiver";

	public static final String RECEIVER_ELEMENT_NAME = "element";
	public static final String RECEIVER_SUCCESS_NAME = "success";
	public static final String RECEIVER_MESSAGE_NAME = "message";

	public PushSyncService() {
		super("PushSyncService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		MyLog.entry();

		final ResultReceiver receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);
		final ChooseSyncModel result = (ChooseSyncModel) intent.getExtras().getSerializable(CHOOSE_SYNC_MODEL_EXTRA_NAME);
		final int accountId = intent.getExtras().getInt(ACCOUNT_ID_EXTRA_NAME);

		final PushSyncHelper helper = new PushSyncHelper(getApplicationContext(), accountId);

		pushUserInfoToUpdate(helper, receiver, result);
		pushMaterialsToUpdate(helper, receiver, result);
		pushMonsters(SyncMode.UPDATED, helper, receiver, result);
		pushMonsters(SyncMode.CREATED, helper, receiver, result);
		pushMonsters(SyncMode.DELETED, helper, receiver, result);

		MyLog.exit();
	}

	private void pushUserInfoToUpdate(PushSyncHelper helper, ResultReceiver receiver, ChooseSyncModel result) {
		MyLog.entry();

		final ChooseModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = result.getSyncedUserInfoToUpdate();
		if (syncedUserInfoToUpdate.getModel().hasDataToSync() && syncedUserInfoToUpdate.isChosen()) {
			try {
				helper.pushUserInfoToUpdate(syncedUserInfoToUpdate.getModel());
				notifyUserInfoUpdated(receiver);
			} catch (final Exception e) {
				MyLog.error("error syncing", e);
				notifyUserInfoUpdatedFailed(receiver, e.getMessage());
			}
		} else {
			MyLog.debug("ignoring");
		}

		MyLog.exit();
	}

	private void pushMaterialsToUpdate(PushSyncHelper helper, ResultReceiver receiver, final ChooseSyncModel result) {
		MyLog.entry();

		for (final ChooseModelContainer<SyncedMaterialModel> syncModel : result.getSyncedMaterialsToUpdate()) {
			final SyncedMaterialModel model = syncModel.getModel();
			if (syncModel.isChosen()) {
				try {
					helper.pushMaterialToUpdate(model);
					notifyMaterialUpdated(receiver);
				} catch (final Exception e) {
					MyLog.error("error syncing", e);
					notifyMaterialUpdatedFailed(receiver, e.getMessage());
				}
			} else {
				MyLog.debug("ignoring : " + model);
			}
		}

		MyLog.exit();
	}

	private void pushMonsters(SyncMode mode, PushSyncHelper helper, ResultReceiver receiver, final ChooseSyncModel result) {
		MyLog.entry();

		for (final ChooseModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonsters(mode)) {
			final SyncedMonsterModel model = syncModel.getModel();
			if (syncModel.isChosen()) {
				try {
					helper.pushMonster(mode, model);
					notifyMonsterPushed(mode, receiver);
				} catch (final Exception e) {
					MyLog.error("error syncing", e);
					notifyMonsterPushedFailed(mode, receiver, e.getMessage());
				}
			} else {
				MyLog.debug("ignoring : " + model);
			}
		}

		MyLog.exit();
	}

	private ElementToPush extractMonsterElementToPushFromSyncMode(SyncMode mode) {
		switch (mode) {
			case CREATED:
				return ElementToPush.MONSTER_TO_CREATE;
			case UPDATED:
				return ElementToPush.MONSTER_TO_UPDATE;
			case DELETED:
				return ElementToPush.MONSTER_TO_DELETE;
			default:
				return null;
		}
	}

	private void notifyUserInfoUpdated(ResultReceiver receiver) {
		notifyUpdate(receiver, ElementToPush.USER_INFO, true, null);
	}

	private void notifyUserInfoUpdatedFailed(ResultReceiver receiver, String message) {
		notifyUpdate(receiver, ElementToPush.USER_INFO, false, message);
	}

	private void notifyMaterialUpdated(ResultReceiver receiver) {
		notifyUpdate(receiver, ElementToPush.MATERIAL_TO_UPDATE, true, null);
	}

	private void notifyMaterialUpdatedFailed(ResultReceiver receiver, String message) {
		notifyUpdate(receiver, ElementToPush.MATERIAL_TO_UPDATE, false, message);
	}

	private void notifyMonsterPushed(SyncMode mode, ResultReceiver receiver) {
		notifyUpdate(receiver, extractMonsterElementToPushFromSyncMode(mode), true, null);
	}

	private void notifyMonsterPushedFailed(SyncMode mode, ResultReceiver receiver, String message) {
		notifyUpdate(receiver, extractMonsterElementToPushFromSyncMode(mode), false, message);
	}

	private void notifyUpdate(ResultReceiver receiver, ElementToPush element, boolean isSuccess, String message) {
		final Bundle bundle = new Bundle();
		bundle.putString(RECEIVER_ELEMENT_NAME, element.name());
		bundle.putBoolean(RECEIVER_SUCCESS_NAME, isSuccess);
		bundle.putString(RECEIVER_MESSAGE_NAME, message);

		receiver.send(0, bundle);
	}
}
