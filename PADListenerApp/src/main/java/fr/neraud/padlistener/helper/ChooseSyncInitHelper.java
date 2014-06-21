package fr.neraud.padlistener.helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.SyncedUserInfoModel;

/**
 * Helper to prepare data for ChooseSync
 *
 * @author Neraud
 */
public class ChooseSyncInitHelper {

	private final Context context;
	private final ComputeSyncResultModel syncResult;

	public ChooseSyncInitHelper(Context context, ComputeSyncResultModel syncResult) {
		super();
		this.context = context;
		this.syncResult = syncResult;
	}

	public ChooseSyncModel filterSyncResult() {
		Log.d(getClass().getName(), "filterSyncResult");
		final ChooseSyncModel chooseSync = new ChooseSyncModel();

		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(context);

		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = new ChooseSyncModelContainer<SyncedUserInfoModel>();
		syncedUserInfoToUpdate.setChosen(prefHelper.isChooseSyncPreselectUserInfoToUpdate());
		syncedUserInfoToUpdate.setSyncedModel(syncResult.getSyncedUserInfo());

		final List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMaterialModel>>();

		for (final SyncedMaterialModel material : syncResult.getSyncedMaterials()) {
			if (material.getCapturedInfo().equals(material.getPadherderInfo())) {
				Log.d(getClass().getName(), "filterSyncResult : ignoring material : " + material);
			} else {
				final ChooseSyncModelContainer<SyncedMaterialModel> container = new ChooseSyncModelContainer<SyncedMaterialModel>();
				container.setChosen(prefHelper.isChooseSyncPreselectMaterialsUpdated());
				container.setSyncedModel(material);
				Log.d(getClass().getName(), "filterSyncResult : keeping material : " + material);
				syncedMaterialsToUpdate.add(container);
			}
		}

		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToCreate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToDelete = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();

		for (final SyncedMonsterModel monster : syncResult.getSyncedMonsters()) {
			final ChooseSyncModelContainer<SyncedMonsterModel> container = new ChooseSyncModelContainer<SyncedMonsterModel>();
			container.setSyncedModel(monster);

			if (monster.getCapturedInfo() == null) {
				Log.d(getClass().getName(), "filterSyncResult : deleting monster : " + monster);
				container.setChosen(prefHelper.isChooseSyncPreselectMonstersDeleted());
				syncedMonstersToDelete.add(container);
			} else if (monster.getPadherderInfo() == null) {
				Log.d(getClass().getName(), "filterSyncResult : creating monster : " + monster);
				container.setChosen(prefHelper.isChooseSyncPreselectMonstersCreated());
				syncedMonstersToCreate.add(container);
			} else {
				Log.d(getClass().getName(), "filterSyncResult : updating monster : " + monster);
				container.setChosen(prefHelper.isChooseSyncPreselectMonstersUpdated());
				syncedMonstersToUpdate.add(container);
			}
		}

		chooseSync.setHasEncounteredUnknownMonster(syncResult.isHasEncounteredUnknownMonster());
		chooseSync.setSyncedUserInfoToUpdate(syncedUserInfoToUpdate);
		chooseSync.setSyncedMaterialsToUpdate(syncedMaterialsToUpdate);
		chooseSync.setSyncedMonstersToUpdate(syncedMonstersToUpdate);
		chooseSync.setSyncedMonstersToCreate(syncedMonstersToCreate);
		chooseSync.setSyncedMonstersToDelete(syncedMonstersToDelete);

		return chooseSync;
	}
}
