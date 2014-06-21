
package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.List;

import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.padherder.constant.MonsterElement;
import fr.neraud.padlistener.padherder.constant.MonsterType;

/**
 * MonsterInfoModel from PADherder
 * 
 * @author Neraud
 */
public class MonsterInfoModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int idJP;
	private Integer idUS;
	private String name;
	private int rarity;
	private MonsterElement element1;
	private MonsterElement element2;
	private MonsterType type1;
	private MonsterType type2;

	private String activeSkillName;
	private String leaderSkillName;
	private List<Integer> awokenSkillIds;

	private int maxLevel;
	private int expCurve;
	private int feedXp;
	private int teamCost;
	private boolean jpOnly;

	private int hpMin;
	private int hpMax;
	private float hpScale;
	private int atkMin;
	private int atkMax;
	private float atkScale;
	private int rcvMin;
	private int rcvMax;
	private float rcvScale;

	private String image40Url;
	private String image60Url;

	public int getIdJP() {
		return idJP;
	}

	public void setIdJP(int idJP) {
		this.idJP = idJP;
	}

	public Integer getIdUS() {
		return idUS;
	}

	public void setIdUS(Integer idUS) {
		this.idUS = idUS;
	}

	public Integer getId(PADRegion region) {
		switch (region) {
		case US:
			return idUS;
		case JP:
		default:
			return idJP;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public MonsterElement getElement1() {
		return element1;
	}

	public void setElement1(MonsterElement element1) {
		this.element1 = element1;
	}

	public MonsterElement getElement2() {
		return element2;
	}

	public void setElement2(MonsterElement element2) {
		this.element2 = element2;
	}

	public MonsterType getType1() {
		return type1;
	}

	public void setType1(MonsterType type1) {
		this.type1 = type1;
	}

	public MonsterType getType2() {
		return type2;
	}

	public void setType2(MonsterType type2) {
		this.type2 = type2;
	}

	public String getActiveSkillName() {
		return activeSkillName;
	}

	public void setActiveSkillName(String activeSkillName) {
		this.activeSkillName = activeSkillName;
	}

	public String getLeaderSkillName() {
		return leaderSkillName;
	}

	public void setLeaderSkillName(String leaderSkillName) {
		this.leaderSkillName = leaderSkillName;
	}

	public List<Integer> getAwokenSkillIds() {
		return awokenSkillIds;
	}

	public void setAwokenSkillIds(List<Integer> awokenSkillIds) {
		this.awokenSkillIds = awokenSkillIds;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getExpCurve() {
		return expCurve;
	}

	public void setExpCurve(int expCurve) {
		this.expCurve = expCurve;
	}

	public int getFeedXp() {
		return feedXp;
	}

	public void setFeedXp(int feedXp) {
		this.feedXp = feedXp;
	}

	public int getTeamCost() {
		return teamCost;
	}

	public void setTeamCost(int teamCost) {
		this.teamCost = teamCost;
	}

	public boolean isJpOnly() {
		return jpOnly;
	}

	public void setJpOnly(boolean jpOnly) {
		this.jpOnly = jpOnly;
	}

	public int getHpMin() {
		return hpMin;
	}

	public void setHpMin(int hpMin) {
		this.hpMin = hpMin;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public float getHpScale() {
		return hpScale;
	}

	public void setHpScale(float hpScale) {
		this.hpScale = hpScale;
	}

	public int getAtkMin() {
		return atkMin;
	}

	public void setAtkMin(int atkMin) {
		this.atkMin = atkMin;
	}

	public int getAtkMax() {
		return atkMax;
	}

	public void setAtkMax(int atkMax) {
		this.atkMax = atkMax;
	}

	public float getAtkScale() {
		return atkScale;
	}

	public void setAtkScale(float atkScale) {
		this.atkScale = atkScale;
	}

	public int getRcvMin() {
		return rcvMin;
	}

	public void setRcvMin(int rcvMin) {
		this.rcvMin = rcvMin;
	}

	public int getRcvMax() {
		return rcvMax;
	}

	public void setRcvMax(int rcvMax) {
		this.rcvMax = rcvMax;
	}

	public float getRcvScale() {
		return rcvScale;
	}

	public void setRcvScale(float rcvScale) {
		this.rcvScale = rcvScale;
	}

	public String getImage40Url() {
		return image40Url;
	}

	public void setImage40Url(String image40Url) {
		this.image40Url = image40Url;
	}

	public String getImage60Url() {
		return image60Url;
	}

	public void setImage60Url(String image60Url) {
		this.image60Url = image60Url;
	}

	@Override
	public String toString() {
		return "(" + idJP + ") " + name;
	}
}
