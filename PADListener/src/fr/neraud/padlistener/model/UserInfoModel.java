
package fr.neraud.padlistener.model;

import java.util.List;

public class UserInfoModel {

	private long accountId;
	private int countryCode;
	private List<UserInfoMaterialModel> materials;
	private List<UserInfoMonsterModel> monsters;

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public int getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}

	public List<UserInfoMaterialModel> getMaterials() {
		return materials;
	}

	public void setMaterials(List<UserInfoMaterialModel> materials) {
		this.materials = materials;
	}

	public List<UserInfoMonsterModel> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<UserInfoMonsterModel> monsters) {
		this.monsters = monsters;
	}

}
