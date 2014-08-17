package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.List;

import fr.neraud.padlistener.constant.SyncMode;

/**
 * ChooseSync model
 *
 * @author Neraud
 */
public class ChooseSyncModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean hasEncounteredUnknownMonster = false;
	private ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate;
	private List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate;
	private List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate;
	private List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToCreate;
	private List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToDelete;

	public boolean isHasEncounteredUnknownMonster() {
		return hasEncounteredUnknownMonster;
	}

	public void setHasEncounteredUnknownMonster(boolean hasEncounteredUnknownMonster) {
		this.hasEncounteredUnknownMonster = hasEncounteredUnknownMonster;
	}

	public ChooseSyncModelContainer<SyncedUserInfoModel> getSyncedUserInfoToUpdate() {
		return syncedUserInfoToUpdate;
	}

	public void setSyncedUserInfoToUpdate(ChooseSyncModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate) {
		this.syncedUserInfoToUpdate = syncedUserInfoToUpdate;
	}

	public List<ChooseSyncModelContainer<SyncedMaterialModel>> getSyncedMaterialsToUpdate() {
		return syncedMaterialsToUpdate;
	}

	public void setSyncedMaterialsToUpdate(List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		this.syncedMaterialsToUpdate = syncedMaterialsToUpdate;
	}

	public List<ChooseSyncModelContainer<SyncedMonsterModel>> getSyncedMonsters(SyncMode mode) {
		switch (mode) {
			case UPDATED:
				return syncedMonstersToUpdate;
			case CREATED:
				return syncedMonstersToCreate;
			case DELETED:
				return syncedMonstersToDelete;
			default:
				return null;
		}
	}

	public void setSyncedMonsters(SyncMode mode, List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonsters) {
		switch (mode) {
			case UPDATED:
				syncedMonstersToUpdate = syncedMonsters;
				break;
			case CREATED:
				syncedMonstersToCreate = syncedMonsters;
				break;
			case DELETED:
				syncedMonstersToDelete = syncedMonsters;
				break;
		}
	}

}
