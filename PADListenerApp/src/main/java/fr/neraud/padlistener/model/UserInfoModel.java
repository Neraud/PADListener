package fr.neraud.padlistener.model;

import java.util.List;

/**
 * UserInfoModel resulting from a PADherder user-api call
 *
 * @author Neraud
 */
public class UserInfoModel {

	private long accountId;
	private int countryCode;
	private int rank;
	private int starterColor;
	private int profileApiId;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getStarterColor() {
		return starterColor;
	}

	public void setStarterColor(int starterColor) {
		this.starterColor = starterColor;
	}

	public int getProfileApiId() {
		return profileApiId;
	}

	public void setProfileApiId(int profileApiId) {
		this.profileApiId = profileApiId;
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

	@Override
	public String toString() {
		return "UserInfoModel [accountId=" + accountId + ", countryCode=" + countryCode + ", rank=" + rank + ", starterColor="
				+ starterColor + "]";
	}

}
