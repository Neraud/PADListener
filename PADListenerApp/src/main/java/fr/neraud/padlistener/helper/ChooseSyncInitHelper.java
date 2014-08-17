package fr.neraud.padlistener.helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	private final DefaultSharedPreferencesHelper prefHelper;
	private final ComputeSyncResultModel syncResult;
	private ChooseSyncModel chooseSync = null;

	public ChooseSyncInitHelper(Context context, ComputeSyncResultModel syncResult) {
		super();
		prefHelper = new DefaultSharedPreferencesHelper(context);
		this.syncResult = syncResult;
	}

	public ChooseSyncModel filterSyncResult() {
		Log.d(getClass().getName(), "filterSyncResult");
		chooseSync = new ChooseSyncModel();
		chooseSync.setHasEncounteredUnknownMonster(syncResult.isHasEncounteredUnknownMonster());

		fillUserInfo();
		fillMaterials();
		fillMonsters();

		return chooseSync;
	}

	private void fillUserInfo() {
		Log.d(getClass().getName(), "fillUserInfo");
		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = new ChooseSyncModelContainer<SyncedUserInfoModel>();
		syncedUserInfoToUpdate.setChosen(prefHelper.isChooseSyncPreselectUserInfoToUpdate());
		syncedUserInfoToUpdate.setSyncedModel(syncResult.getSyncedUserInfo());

		chooseSync.setSyncedUserInfoToUpdate(syncedUserInfoToUpdate);
	}

	private void fillMaterials() {
		Log.d(getClass().getName(), "fillMaterials");
		final List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMaterialModel>>();

		for (final SyncedMaterialModel material : syncResult.getSyncedMaterials()) {
			if (material.getCapturedInfo().equals(material.getPadherderInfo())) {
				Log.d(getClass().getName(), "fillMaterials : ignoring material : " + material);
			} else {
				final ChooseSyncModelContainer<SyncedMaterialModel> container = new ChooseSyncModelContainer<SyncedMaterialModel>();
				container.setChosen(prefHelper.isChooseSyncPreselectMaterialsUpdated());
				container.setSyncedModel(material);
				Log.d(getClass().getName(), "fillMaterials : keeping material : " + material);
				syncedMaterialsToUpdate.add(container);
			}
		}
		chooseSync.setSyncedMaterialsToUpdate(syncedMaterialsToUpdate);
	}

	private void fillMonsters() {
		Log.d(getClass().getName(), "fillMonsters");
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToCreate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToDelete = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();

		final Set<Integer> ignoredIds = prefHelper.getMonsterIgnoreList();

		for (final SyncedMonsterModel monster : syncResult.getSyncedMonsters()) {
			final ChooseSyncModelContainer<SyncedMonsterModel> container = new ChooseSyncModelContainer<SyncedMonsterModel>();
			container.setSyncedModel(monster);

			if (monster.getCapturedInfo() == null) {
				boolean ignored = prefHelper.isChooseSyncUseIgnoreListForMonstersDeleted() && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					Log.d(getClass().getName(), "fillMonsters : ignoring deleted monster : " + monster);
				} else {
					Log.d(getClass().getName(), "fillMonsters : adding deleted monster : " + monster);
					container.setChosen(prefHelper.isChooseSyncPreselectMonstersDeleted());
					syncedMonstersToDelete.add(container);
				}
			} else if (monster.getPadherderInfo() == null) {
				boolean ignored = prefHelper.isChooseSyncUseIgnoreListForMonstersCreated() && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					Log.d(getClass().getName(), "fillMonsters : ignoring created monster : " + monster);
				} else {
					Log.d(getClass().getName(), "fillMonsters : adding created monster : " + monster);
					container.setChosen(prefHelper.isChooseSyncPreselectMonstersCreated());
					syncedMonstersToCreate.add(container);
				}
			} else {
				boolean ignored = prefHelper.isChooseSyncUseIgnoreListForMonstersUpdated() && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					Log.d(getClass().getName(), "fillMonsters : ignoring updated monster : " + monster);
				} else {
					Log.d(getClass().getName(), "fillMonsters : adding updated monster : " + monster);
					container.setChosen(prefHelper.isChooseSyncPreselectMonstersUpdated());
					syncedMonstersToUpdate.add(container);
				}
			}
		}
		chooseSync.setSyncedMonstersToUpdate(syncedMonstersToUpdate);
		chooseSync.setSyncedMonstersToCreate(syncedMonstersToCreate);
		chooseSync.setSyncedMonstersToDelete(syncedMonstersToDelete);
	}
}
