package fr.neraud.padlistener.model;

/**
 * MonsterModel wrapping a CapturedMonsterCardModel captured from PAD, and a MonsterInfoModel to provide additional information
 * from PADherder
 *
 * @author Neraud
 */
public class CapturedMonsterFullInfoModel {

	private MonsterModel capturedMonster;
	private MonsterInfoModel monsterInfo;

	public MonsterModel getCapturedMonster() {
		return capturedMonster;
	}

	public void setCapturedMonster(MonsterModel capturedMonster) {
		this.capturedMonster = capturedMonster;
	}

	public MonsterInfoModel getMonsterInfo() {
		return monsterInfo;
	}

	public void setMonsterInfo(MonsterInfoModel monsterInfo) {
		this.monsterInfo = monsterInfo;
	}

}
