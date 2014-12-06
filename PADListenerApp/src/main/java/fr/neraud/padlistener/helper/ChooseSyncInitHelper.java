package fr.neraud.padlistener.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.SyncMode;
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
		MyLog.entry();

		chooseSync = new ChooseSyncModel();
		chooseSync.setHasEncounteredUnknownMonster(syncResult.isHasEncounteredUnknownMonster());

		fillUserInfo();
		fillMaterials();
		fillMonsters();

		MyLog.exit();
		return chooseSync;
	}

	private void fillUserInfo() {
		MyLog.entry();

		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = new ChooseSyncModelContainer<SyncedUserInfoModel>();
		syncedUserInfoToUpdate.setChosen(prefHelper.isChooseSyncPreselectUserInfoToUpdate());
		syncedUserInfoToUpdate.setSyncedModel(syncResult.getSyncedUserInfo());

		chooseSync.setSyncedUserInfoToUpdate(syncedUserInfoToUpdate);

		MyLog.exit();
	}

	private void fillMaterials() {
		MyLog.entry();

		final List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMaterialModel>>();

		for (final SyncedMaterialModel material : syncResult.getSyncedMaterials()) {
			if (material.getCapturedInfo().equals(material.getPadherderInfo())) {
				MyLog.debug("ignoring material : " + material);
			} else {
				final ChooseSyncModelContainer<SyncedMaterialModel> container = new ChooseSyncModelContainer<SyncedMaterialModel>();
				container.setChosen(prefHelper.isChooseSyncPreselectMaterialsUpdated());
				container.setSyncedModel(material);
				MyLog.debug("keeping material : " + material);
				syncedMaterialsToUpdate.add(container);
			}
		}
		chooseSync.setSyncedMaterialsToUpdate(syncedMaterialsToUpdate);

		MyLog.exit();
	}

	private void fillMonsters() {
		MyLog.entry();

		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToCreate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToDelete = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();

		final Set<Integer> ignoredIds = prefHelper.getMonsterIgnoreList();

		for (final SyncedMonsterModel monster : syncResult.getSyncedMonsters()) {
			final ChooseSyncModelContainer<SyncedMonsterModel> container = new ChooseSyncModelContainer<SyncedMonsterModel>();
			container.setSyncedModel(monster);

			if (monster.getCapturedInfo() == null) {
				boolean ignored = prefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.DELETED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring deleted monster : " + monster);
				} else {
					MyLog.debug("adding deleted monster : " + monster);
					container.setChosen(prefHelper.isChooseSyncPreselectMonsters(SyncMode.DELETED));
					syncedMonstersToDelete.add(container);
				}
			} else if (monster.getPadherderInfo() == null) {
				boolean ignored = prefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.CREATED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring created monster : " + monster);
				} else {
					MyLog.debug("adding created monster : " + monster);
					container.setChosen(prefHelper.isChooseSyncPreselectMonsters(SyncMode.CREATED));
					syncedMonstersToCreate.add(container);
				}
			} else {
				boolean ignored = prefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.UPDATED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring updated monster : " + monster);
				} else {
					MyLog.debug("adding updated monster : " + monster);
					container.setChosen(prefHelper.isChooseSyncPreselectMonsters(SyncMode.UPDATED));
					syncedMonstersToUpdate.add(container);
				}
			}
		}
		chooseSync.setSyncedMonsters(SyncMode.UPDATED, syncedMonstersToUpdate);
		chooseSync.setSyncedMonsters(SyncMode.CREATED, syncedMonstersToCreate);
		chooseSync.setSyncedMonsters(SyncMode.DELETED, syncedMonstersToDelete);

		MyLog.exit();
	}
}
