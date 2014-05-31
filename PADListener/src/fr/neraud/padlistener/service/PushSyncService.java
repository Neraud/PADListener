
package fr.neraud.padlistener.service;

import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import fr.neraud.padlistener.helper.PushSyncHelper;
import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.SyncedUserInfoModel;

public class PushSyncService extends IntentService {

	public static final String EXTRA_CHOOSE_SYNC_MODEL_NAME = "chooseSyncModel";

	public PushSyncService() {
		super("PushSyncService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");
		final ChooseSyncModel result = (ChooseSyncModel) intent.getExtras().getSerializable(EXTRA_CHOOSE_SYNC_MODEL_NAME);

		final PushSyncHelper helper = new PushSyncHelper(getApplicationContext());

		pushUserInfoToUpdate(helper, result);
		pushMaterialsToUpdate(helper, result);
		pushMonstersToUpdate(helper, result);
		pushMonstersToCreate(helper, result);
		pushMonstersToDelete(helper, result);
	}

	private void pushUserInfoToUpdate(PushSyncHelper helper, ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushUserInfoToUpdate");
		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = result.getSyncedUserInfoToUpdate();
		if (syncedUserInfoToUpdate.isChoosen()) {
			helper.pushUserInfoToUpdate(syncedUserInfoToUpdate.getSyncedModel());
		} else {
			Log.d(getClass().getName(), "pushUserInfoToUpdate : ignoring");
		}
	}

	private void pushMaterialsToUpdate(PushSyncHelper helper, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMaterialsToUpdate");
		for (final ChooseSyncModelContainer<SyncedMaterialModel> syncModel : result.getSyncedMaterialsToUpdate()) {
			final SyncedMaterialModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMaterialToUpdate(model);
				} catch (final JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final HttpCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.d(getClass().getName(), "pushMaterialsToUpdate : ignoring : " + model);
			}
		}
	}

	private void pushMonstersToUpdate(PushSyncHelper helper, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMonstersToUpdate");
		for (final ChooseSyncModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonstersToUpdate()) {
			final SyncedMonsterModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMonsterToUpdate(model);
				} catch (final JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final HttpCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.d(getClass().getName(), "pushMonstersToUpdate : ignoring : " + model);
			}
		}
	}

	private void pushMonstersToCreate(PushSyncHelper helper, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMonstersToCreate");
		for (final ChooseSyncModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonstersToCreate()) {
			final SyncedMonsterModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMonsterToCreate(model);
				} catch (final JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final HttpCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.d(getClass().getName(), "pushMonstersToCreate : ignoring : " + model);
			}
		}
	}

	private void pushMonstersToDelete(PushSyncHelper helper, final ChooseSyncModel result) {
		Log.d(getClass().getName(), "pushMonstersToDelete");
		for (final ChooseSyncModelContainer<SyncedMonsterModel> syncModel : result.getSyncedMonstersToDelete()) {
			final SyncedMonsterModel model = syncModel.getSyncedModel();
			if (syncModel.isChoosen()) {
				try {
					helper.pushMonsterToDelete(model);
				} catch (final HttpCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Log.d(getClass().getName(), "pushMonstersToDelete : ignoring : " + model);
			}
		}
	}

}
