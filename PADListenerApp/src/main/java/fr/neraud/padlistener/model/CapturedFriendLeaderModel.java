package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * FriendLeaderModel for captured data
 *
 * @author Neraud
 */
public class CapturedFriendLeaderModel implements Serializable {

	private int idJp;
	private int level;
	private int skillLevel;
	private int plusHp;
	private int plusAtk;
	private int plusRcv;
	private int awakenings;

	public int getIdJp() {
		return idJp;
	}

	public void setIdJp(int idJp) {
		this.idJp = idJp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getPlusHp() {
		return plusHp;
	}

	public void setPlusHp(int plusHp) {
		this.plusHp = plusHp;
	}

	public int getPlusAtk() {
		return plusAtk;
	}

	public void setPlusAtk(int plusAtk) {
		this.plusAtk = plusAtk;
	}

	public int getPlusRcv() {
		return plusRcv;
	}

	public void setPlusRcv(int plusRcv) {
		this.plusRcv = plusRcv;
	}

	public int getAwakenings() {
		return awakenings;
	}

	public void setAwakenings(int awakenings) {
		this.awakenings = awakenings;
	}

	@Override
	public String toString() {
		return "" + idJp;
	}
}
