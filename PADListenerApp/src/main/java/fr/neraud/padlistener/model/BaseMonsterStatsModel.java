package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * Created by Neraud on 28/09/2014.
 */
public class BaseMonsterStatsModel implements Serializable {

	private int idJp;
	private long exp;
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

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
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
		final StringBuilder builder = new StringBuilder();
		builder.append("(").append(idJp).append(") ");
		builder.append(skillLevel).append(" skill, ");
		builder.append("+").append(plusHp);
		builder.append(" +").append(plusAtk);
		builder.append(" +").append(plusRcv);
		builder.append(" ").append(awakenings);
		return builder.toString();
	}
}
