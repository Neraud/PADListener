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

	private final DefaultSharedPreferencesHelper mPrefHelper;
	private final ComputeSyncResultModel mSyncResult;
	private ChooseSyncModel mChooseSyncModel = null;
	private boolean mHasDataToSync = false;
	private boolean mHasChosenDataToSync = false;

	public ChooseSyncInitHelper(Context context, ComputeSyncResultModel syncResult) {
		super();
		mPrefHelper = new DefaultSharedPreferencesHelper(context);
		this.mSyncResult = syncResult;
	}

	public ChooseSyncModel filterSyncResult() {
		MyLog.entry();

		mChooseSyncModel = new ChooseSyncModel();
		mChooseSyncModel.setHasEncounteredUnknownMonster(mSyncResult.isHasEncounteredUnknownMonster());

		fillUserInfo();
		fillMaterials();
		fillMonsters();

		MyLog.exit();
		return mChooseSyncModel;
	}

	private void fillUserInfo() {
		MyLog.entry();

		final ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = new ChooseSyncModelContainer<SyncedUserInfoModel>();
		syncedUserInfoToUpdate.setChosen(mPrefHelper.isChooseSyncPreselectUserInfoToUpdate());
		syncedUserInfoToUpdate.setSyncedModel(mSyncResult.getSyncedUserInfo());

		mChooseSyncModel.setSyncedUserInfoToUpdate(syncedUserInfoToUpdate);

		MyLog.exit();
	}

	private void fillMaterials() {
		MyLog.entry();

		final List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMaterialModel>>();

		for (final SyncedMaterialModel material : mSyncResult.getSyncedMaterials()) {
			if (material.getCapturedInfo().equals(material.getPadherderInfo())) {
				MyLog.debug("ignoring material : " + material);
			} else {
				MyLog.debug("keeping material : " + material);
				final ChooseSyncModelContainer<SyncedMaterialModel> container = new ChooseSyncModelContainer<SyncedMaterialModel>();
				container.setChosen(mPrefHelper.isChooseSyncPreselectMaterialsUpdated());
				container.setSyncedModel(material);
				syncedMaterialsToUpdate.add(container);
				flagDataToSync(container);
			}
		}
		mChooseSyncModel.setSyncedMaterialsToUpdate(syncedMaterialsToUpdate);

		MyLog.exit();
	}

	private void fillMonsters() {
		MyLog.entry();

		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToCreate = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();
		final List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToDelete = new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>();

		final Set<Integer> ignoredIds = mPrefHelper.getMonsterIgnoreList();

		for (final SyncedMonsterModel monster : mSyncResult.getSyncedMonsters()) {
			final ChooseSyncModelContainer<SyncedMonsterModel> container = new ChooseSyncModelContainer<SyncedMonsterModel>();
			container.setSyncedModel(monster);

			if (monster.getCapturedInfo() == null) {
				boolean ignored = mPrefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.DELETED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring deleted monster : " + monster);
				} else {
					MyLog.debug("adding deleted monster : " + monster);
					container.setChosen(mPrefHelper.isChooseSyncPreselectMonsters(SyncMode.DELETED));
					syncedMonstersToDelete.add(container);
					flagDataToSync(container);
				}
			} else if (monster.getPadherderInfo() == null) {
				boolean ignored = mPrefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.CREATED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring created monster : " + monster);
				} else {
					MyLog.debug("adding created monster : " + monster);
					container.setChosen(mPrefHelper.isChooseSyncPreselectMonsters(SyncMode.CREATED));
					syncedMonstersToCreate.add(container);
					flagDataToSync(container);
				}
			} else {
				boolean ignored = mPrefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.UPDATED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring updated monster : " + monster);
				} else {
					MyLog.debug("adding updated monster : " + monster);
					container.setChosen(mPrefHelper.isChooseSyncPreselectMonsters(SyncMode.UPDATED));
					syncedMonstersToUpdate.add(container);
					flagDataToSync(container);
				}
			}
		}
		mChooseSyncModel.setSyncedMonsters(SyncMode.UPDATED, syncedMonstersToUpdate);
		mChooseSyncModel.setSyncedMonsters(SyncMode.CREATED, syncedMonstersToCreate);
		mChooseSyncModel.setSyncedMonsters(SyncMode.DELETED, syncedMonstersToDelete);

		MyLog.exit();
	}

	private void flagDataToSync(ChooseSyncModelContainer container) {
		mHasDataToSync = true;
		if(container.isChosen()) {
			mHasChosenDataToSync = true;
		}
	}

	public boolean isHasDataToSync() {
		return mHasDataToSync;
	}
	public boolean isHasChosenDataToSync() {
		return mHasChosenDataToSync;
	}
}
