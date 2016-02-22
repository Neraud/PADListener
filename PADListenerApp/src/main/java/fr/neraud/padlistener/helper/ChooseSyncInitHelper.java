package fr.neraud.padlistener.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.ChooseSyncModel;
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
	private int nbElementsToSync = 0;

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

		MyLog.info("nbElementsToSync : " + nbElementsToSync);

		MyLog.exit();
		return mChooseSyncModel;
	}

	private void fillUserInfo() {
		MyLog.entry();

		final ChooseModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate = new ChooseModelContainer<SyncedUserInfoModel>();
		syncedUserInfoToUpdate.setChosen(mPrefHelper.isChooseSyncPreselectUserInfoToUpdate());
		syncedUserInfoToUpdate.setModel(mSyncResult.getSyncedUserInfo());

		mChooseSyncModel.setSyncedUserInfoToUpdate(syncedUserInfoToUpdate);
		nbElementsToSync++;

		MyLog.exit();
	}

	private void fillMaterials() {
		MyLog.entry();

		final List<ChooseModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate = new ArrayList<ChooseModelContainer<SyncedMaterialModel>>();

		for (final SyncedMaterialModel material : mSyncResult.getSyncedMaterials()) {
			if (material.getCapturedInfo().equals(material.getPadherderInfo())) {
				MyLog.debug("ignoring material : " + material);
			} else {
				MyLog.debug("keeping material : " + material);
				final ChooseModelContainer<SyncedMaterialModel> container = new ChooseModelContainer<SyncedMaterialModel>();
				container.setChosen(mPrefHelper.isChooseSyncPreselectMaterialsUpdated());
				container.setModel(material);
				syncedMaterialsToUpdate.add(container);
				nbElementsToSync++;
				flagDataToSync(container);
			}
		}
		mChooseSyncModel.setSyncedMaterialsToUpdate(syncedMaterialsToUpdate);

		MyLog.exit();
	}

	private void fillMonsters() {
		MyLog.entry();

		mChooseSyncModel.setSyncedMonsters(SyncMode.UPDATED, new ArrayList<ChooseModelContainer<SyncedMonsterModel>>());
		mChooseSyncModel.setSyncedMonsters(SyncMode.CREATED, new ArrayList<ChooseModelContainer<SyncedMonsterModel>>());
		mChooseSyncModel.setSyncedMonsters(SyncMode.DELETED, new ArrayList<ChooseModelContainer<SyncedMonsterModel>>());

		final Set<Integer> ignoredIds = mPrefHelper.getMonsterIgnoreList();

		for (final SyncedMonsterModel monster : mSyncResult.getSyncedMonsters()) {
			final ChooseModelContainer<SyncedMonsterModel> container = new ChooseModelContainer<SyncedMonsterModel>();
			container.setModel(monster);

			if (monster.getCapturedInfo() == null) {
				boolean ignored = mPrefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.DELETED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring deleted monster : " + monster);
				} else {
					MyLog.debug("adding deleted monster : " + monster);
					addMonsterToSync(SyncMode.DELETED, container);
				}
			} else if (monster.getPadherderInfo() == null) {
				boolean ignored = mPrefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.CREATED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring created monster : " + monster);
				} else {
					MyLog.debug("adding created monster : " + monster);
					addMonsterToSync(SyncMode.CREATED, container);
				}
			} else {
				boolean ignored = mPrefHelper.isChooseSyncUseIgnoreListForMonsters(SyncMode.UPDATED) && ignoredIds.contains(monster.getDisplayedMonsterInfo().getIdJP());
				if (ignored) {
					MyLog.debug("ignoring updated monster : " + monster);
				} else {
					MyLog.debug("adding updated monster : " + monster);
					addMonsterToSync(SyncMode.UPDATED, container);
				}
			}
		}


		MyLog.exit();
	}

	private void addMonsterToSync(SyncMode mode, ChooseModelContainer<SyncedMonsterModel> container) {
		container.setChosen(mPrefHelper.isChooseSyncPreselectMonsters(mode));
		mChooseSyncModel.getSyncedMonsters(mode).add(container);
		flagDataToSync(container);
	}

	private void flagDataToSync(ChooseModelContainer container) {
		nbElementsToSync++;
		mHasDataToSync = true;
		if (container.isChosen()) {
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
