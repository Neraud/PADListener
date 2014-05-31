
package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.List;

public class SyncComputeResultModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private SyncedUserInfoModel syncedUserInfo;
	private List<SyncedMaterialModel> syncedMaterials;
	private List<SyncedMonsterModel> syncedMonsters;

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
