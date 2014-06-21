package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.List;

/**
 * ResultModel for a ComputeSync
 *
 * @author Neraud
 */
public class ComputeSyncResultModel implements Serializable {

	private static final long serialVersionUID = 1L;
	boolean hasEncountredUnknownMonster = false;
	private SyncedUserInfoModel syncedUserInfo;
	private List<SyncedMaterialModel> syncedMaterials;
	private List<SyncedMonsterModel> syncedMonsters;

	public boolean isHasEncountredUnknownMonster() {
		return hasEncountredUnknownMonster;
	}

	public void setHasEncountredUnknownMonster(boolean hasEncountredUnknownMonster) {
		this.hasEncountredUnknownMonster = hasEncountredUnknownMonster;
	}

	public SyncedUserInfoModel getSyncedUserInfo() {
		return syncedUserInfo;
	}

	public void setSyncedUserInfo(SyncedUserInfoModel syncedUserInfo) {
		this.syncedUserInfo = syncedUserInfo;
	}

	public List<SyncedMaterialModel> getSyncedMaterials() {
		return syncedMaterials;
	}

	public void setSyncedMaterials(List<SyncedMaterialModel> syncedMaterials) {
		this.syncedMaterials = syncedMaterials;
	}

	public List<SyncedMonsterModel> getSyncedMonsters() {
		return syncedMonsters;
	}

	public void setSyncedMonsters(List<SyncedMonsterModel> syncedMonsters) {
		this.syncedMonsters = syncedMonsters;
	}

}
