
package fr.neraud.padlistener.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import fr.neraud.padlistener.helper.PushSyncHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
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
		Log.d(getClass().getName(), "onHandleIntent");

		final ResultReceiver receiver = intent.getParcelableExtra(AbstractRestResultReceiver.RECEIVER_EXTRA_NAME);
		final ChooseSyncModel result = (ChooseSyncModel) intent.getExtras().getSerializable(CHOOSE_SYNC_MODEL_EXTRA_NAME);
		final int accountId = intent.getExtras().getInt(ACCOUNT_ID_EXTRA_NAME);

		final PushSyncHelper helper = new PushSyncHelper(getApplicationContext(), accountId);

		pushUserInfoToUpdate(helper, receiver, result);
		pushMaterialsToUpdate(helper, receiver, result);
		pushMonstersToUpdate(helper, receiver, result);
		pushMonstersToCreate(helper, receiver, result);
		pushMonstersToDelete(helper, receiver, result);
	}

	private void pushUserInfoToUpdate(PushSyncHelper helper, ResultReceiver receiver, ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushUserInfoToUpdate");
		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = result.getSyncedUserInfoToUpdate();
		if (syncedUserInfoToUpdate.isChoosen()) {
			try {
				helper.pushUserInfoToUpdate(syncedUserInfoToUpdate.getSyncedModel());
				notifyUserInfoUpdated(receiver);
			} catch (final Exception e) {
				Log.e(getClass().getName(), "pushUserInfoToUpdate : error syncing", e);
				notifyUserInfoUpdatedFailed(receiver, e.getMessage());
			}
		} else {
			Log.d(getClass().getName(), "pushUserInfoToUpdate : ignoring");
		}
	}

	private void pushMaterialsToUpdate(PushSyncHelper helper, ResultReceiver receiver, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMaterialsToUpdate");
		for (final ChooseSyncModelContainer<SyncedMaterialModel> syncModel : result.getSyncedMaterialsToUpdate()) {
			final SyncedMaterialModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMaterialToUpdate(model);
					notifyMaterialUpdated(receiver);
				} catch (final Exception e) {
					Log.e(getClass().getName(), "pushMaterialsToUpdate : error syncing", e);
					notifyMaterialUpdatedFailed(receiver, e.getMessage());
				}
			} else {
				Log.d(getClass().getName(), "pushMaterialsToUpdate : ignoring : " + model);
			}
		}
	}

	private void pushMonstersToUpdate(PushSyncHelper helper, ResultReceiver receiver, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMonstersToUpdate");
		for (final ChooseSyncModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonstersToUpdate()) {
			final SyncedMonsterModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMonsterToUpdate(model);
					notifyMonsterUpdated(receiver);
				} catch (final Exception e) {
					Log.e(getClass().getName(), "pushMonstersToUpdate : error syncing", e);
					notifyMonsterUpdatedFailed(receiver, e.getMessage());
				}
			} else {
				Log.d(getClass().getName(), "pushMonstersToUpdate : ignoring : " + model);
			}
		}
	}

	private void pushMonstersToCreate(PushSyncHelper helper, ResultReceiver receiver, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMonstersToCreate");
		for (final ChooseSyncModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonstersToCreate()) {
			final SyncedMonsterModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMonsterToCreate(model);
					notifyMonsterCreated(receiver);
				} catch (final Exception e) {
					Log.e(getClass().getName(), "pushMonstersToCreate : error syncing", e);
					notifyMonsterCreatedFailed(receiver, e.getMessage());
				}
			} else {
				Log.d(getClass().getName(), "pushMonstersToCreate : ignoring : " + model);
			}
		}
	}

	private void pushMonstersToDelete(PushSyncHelper helper, ResultReceiver receiver, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMonstersToDelete");
		for (final ChooseSyncModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonstersToDelete()) {
			final SyncedMonsterModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMonsterToDelete(model);
					notifyMonsterDeleted(receiver);
				} catch (final Exception e) {
					Log.e(getClass().getName(), "pushMonstersToDelete : error syncing", e);
					notifyMonsterDeletedFailed(receiver, e.getMessage());
				}
			} else {
				Log.d(getClass().getName(), "pushMonstersToDelete : ignoring : " + model);
			}
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

	private void notifyMonsterUpdated(ResultReceiver receiver) {
		notifyUpdate(receiver, ElementToPush.MONSTER_TO_UPDATE, true, null);
	}

	private void notifyMonsterUpdatedFailed(ResultReceiver receiver, String message) {
		notifyUpdate(receiver, ElementToPush.MONSTER_TO_UPDATE, false, message);
	}

	private void notifyMonsterCreated(ResultReceiver receiver) {
		notifyUpdate(receiver, ElementToPush.MONSTER_TO_CREATE, true, null);
	}

	private void notifyMonsterCreatedFailed(ResultReceiver receiver, String message) {
		notifyUpdate(receiver, ElementToPush.MONSTER_TO_CREATE, false, message);
	}

	private void notifyMonsterDeleted(ResultReceiver receiver) {
		notifyUpdate(receiver, ElementToPush.MONSTER_TO_DELETE, true, null);
	}

	private void notifyMonsterDeletedFailed(ResultReceiver receiver, String message) {
		notifyUpdate(receiver, ElementToPush.MONSTER_TO_DELETE, false, message);
	}

	private void notifyUpdate(ResultReceiver receiver, ElementToPush element, boolean isSuccess, String message) {
		final Bundle bundle = new Bundle();
		bundle.putString(RECEIVER_ELEMENT_NAME, element.name());
		bundle.putBoolean(RECEIVER_SUCCESS_NAME, isSuccess);
		bundle.putString(RECEIVER_MESSAGE_NAME, message);

		receiver.send(0, bundle);
	}
}
