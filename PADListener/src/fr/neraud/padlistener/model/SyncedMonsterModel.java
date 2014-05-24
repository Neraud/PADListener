
package fr.neraud.padlistener.model;

import java.io.Serializable;

public class SyncedMonsterModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private MonsterInfoModel monsterInfo;
	private BaseMonsterModel capturedMonster;
	private BaseMonsterModel padherderMonster;

	public MonsterInfoModel getMonsterInfo() {
		return monsterInfo;
	}

	public void setMonsterInfo(MonsterInfoModel monsterInfo) {
		this.monsterInfo = monsterInfo;
	}

	public BaseMonsterModel getCapturedMonster() {
		return capturedMonster;
	}

	public void setCapturedMonster(BaseMonsterModel capturedMonster) {
		this.capturedMonster = capturedMonster;
	}

	public BaseMonsterModel getPadherderMonster() {
		return padherderMonster;
	}

	public void setPadherderMonster(BaseMonsterModel padherderMonster) {
		this.padherderMonster = padherderMonster;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(monsterInfo.toString());
		builder.append(" : ").append(padherderMonster).append(" -> ").append(capturedMonster);
		return builder.toString();
	}
}
