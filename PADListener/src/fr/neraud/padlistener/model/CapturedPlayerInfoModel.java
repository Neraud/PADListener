
package fr.neraud.padlistener.model;

import java.util.Date;

public class CapturedPlayerInfoModel {

	private Date lastUpdate;
	private int friendMax;
	private int cardMax;
	private String name;
	private int rank;
	private long exp;
	private long currentLevelExp;
	private long nextLevelExp;
	private int costMax;
	private int stamina;
	private int staminaMax;
	private int stones;
	private long coins;

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getFriendMax() {
		return friendMax;
	}

	public void setFriendMax(int friendMax) {
		this.friendMax = friendMax;
	}

	public int getCardMax() {
		return cardMax;
	}

	public void setCardMax(int cardMax) {
		this.cardMax = cardMax;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public long getCurrentLevelExp() {
		return currentLevelExp;
	}

	public void setCurrentLevelExp(long currentLevelExp) {
		this.currentLevelExp = currentLevelExp;
	}

	public long getNextLevelExp() {
		return nextLevelExp;
	}

	public void setNextLevelExp(long nextLevelExp) {
		this.nextLevelExp = nextLevelExp;
	}

	public int getCostMax() {
		return costMax;
	}

	public void setCostMax(int costMax) {
		this.costMax = costMax;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getStaminaMax() {
		return staminaMax;
	}

	public void setStaminaMax(int staminaMax) {
		this.staminaMax = staminaMax;
	}

	public int getStones() {
		return stones;
	}

	public void setStones(int stones) {
		this.stones = stones;
	}

	public long getCoins() {
		return coins;
	}

	public void setCoins(long coins) {
		this.coins = coins;
	}

}
