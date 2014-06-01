
package fr.neraud.padlistener.helper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
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

	private final ComputeSyncResultModel syncResult;

	public ChooseSyncInitHelper(ComputeSyncResultModel syncResult) {
		super();
		this.syncResult = syncResult;
	}

	public ChooseSyncModel filterSyncResult() {
		Log.d(getClass().getName(), "filterSyncResult");
		final ChooseSyncModel chooseSync = new ChooseSyncModel();

		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = new ChooseSyncModelContainer<SyncedUserInfoModel>();
		// TODO let the user choose ?
		syncedUserInfoToUpdate.setChoosen(false);
		syncedUserInfoToUpdate.setSyncedModel(syncResult.getSyncedUserInfo());

		final List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMaterialModel>>();

		for (final SyncedMaterialModel material : syncResult.getSyncedMaterials()) {
			if (material.getCapturedInfo().equals(material.getPadherderInfo())) {
				Log.d(getClass().getName(), "filterSyncResult : ignoring material : " + material);
			} else {
				final ChooseSyncModelContainer<SyncedMaterialModel> container = new ChooseSyncModelContainer<SyncedMaterialModel>();
				container.setChoosen(true);
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
				container.setChoosen(false);
				syncedMonstersToDelete.add(container);
			} else if (monster.getPadherderInfo() == null) {
				Log.d(getClass().getName(), "filterSyncResult : creating monster : " + monster);
				container.setChoosen(false);
				syncedMonstersToCreate.add(container);
			} else {
				Log.d(getClass().getName(), "filterSyncResult : updating monster : " + monster);
				container.setChoosen(true);
				syncedMonstersToUpdate.add(container);
			}
		}

		chooseSync.setSyncedUserInfoToUpdate(syncedUserInfoToUpdate);
		chooseSync.setSyncedMaterialsToUpdate(syncedMaterialsToUpdate);
		chooseSync.setSyncedMonstersToUpdate(syncedMonstersToUpdate);
		chooseSync.setSyncedMonstersToCreate(syncedMonstersToCreate);
		chooseSync.setSyncedMonstersToDelete(syncedMonstersToDelete);

		return chooseSync;
	}
}
