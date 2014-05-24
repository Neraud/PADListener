
package fr.neraud.padlistener.model;

import java.util.List;
import java.util.Map;

public class UserInfoModel {

	private long accountId;
	private int countryCode;
	// Id -> qty
	private Map<Integer, Integer> materials;
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

	public Map<Integer, Integer> getMaterials() {
		return materials;
	}

	public void setMaterials(Map<Integer, Integer> materials) {
		this.materials = materials;
	}

	public List<UserInfoMonsterModel> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<UserInfoMonsterModel> monsters) {
		this.monsters = monsters;
	}

}
