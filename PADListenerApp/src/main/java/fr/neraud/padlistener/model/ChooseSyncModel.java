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
	private ChooseModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate;
	private List<ChooseModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate;
	private List<ChooseModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate;
	private List<ChooseModelContainer<SyncedMonsterModel>> syncedMonstersToCreate;
	private List<ChooseModelContainer<SyncedMonsterModel>> syncedMonstersToDelete;

	public boolean isHasEncounteredUnknownMonster() {
		return hasEncounteredUnknownMonster;
	}

	public void setHasEncounteredUnknownMonster(boolean hasEncounteredUnknownMonster) {
		this.hasEncounteredUnknownMonster = hasEncounteredUnknownMonster;
	}

	public ChooseModelContainer<SyncedUserInfoModel> getSyncedUserInfoToUpdate() {
		return syncedUserInfoToUpdate;
	}

	public void setSyncedUserInfoToUpdate(ChooseModelContainer<SyncedUserInfoModel> syncedUserInfoToUpdate) {
		this.syncedUserInfoToUpdate = syncedUserInfoToUpdate;
	}

	public List<ChooseModelContainer<SyncedMaterialModel>> getSyncedMaterialsToUpdate() {
		return syncedMaterialsToUpdate;
	}

	public void setSyncedMaterialsToUpdate(List<ChooseModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		this.syncedMaterialsToUpdate = syncedMaterialsToUpdate;
	}

	public List<ChooseModelContainer<SyncedMonsterModel>> getSyncedMonsters(SyncMode mode) {
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

	public void setSyncedMonsters(SyncMode mode, List<ChooseModelContainer<SyncedMonsterModel>> syncedMonsters) {
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
