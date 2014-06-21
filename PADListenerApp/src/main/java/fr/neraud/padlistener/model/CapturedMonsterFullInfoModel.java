
package fr.neraud.padlistener.model;

/**
 * MonsterModel wrapping a CapturedMonsterCardModel captured from PAD, and a MonsterInfoModel to provide additionnal informations
 * from PADherder
 * 
 * @author Neraud
 */
public class CapturedMonsterFullInfoModel {

	private CapturedMonsterCardModel capturedMonster;
	private MonsterInfoModel monsterInfo;

	public CapturedMonsterCardModel getCapturedMonster() {
		return capturedMonster;
	}

	public void setCapturedMonster(CapturedMonsterCardModel capturedMonster) {
		this.capturedMonster = capturedMonster;
	}

	public MonsterInfoModel getMonsterInfo() {
		return monsterInfo;
	}

	public void setMonsterInfo(MonsterInfoModel monsterInfo) {
		this.monsterInfo = monsterInfo;
	}

}
