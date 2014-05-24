
package fr.neraud.padlistener.model;

import java.io.Serializable;

public class SyncedMaterialModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private MonsterInfoModel monsterInfo;
	private int capturedQuantity;
	private int padherderQuantity;

	public MonsterInfoModel getMonsterInfo() {
		return monsterInfo;
	}

	public void setMonsterInfo(MonsterInfoModel monsterInfo) {
		this.monsterInfo = monsterInfo;
	}

	public int getCapturedQuantity() {
		return capturedQuantity;
	}

	public void setCapturedQuantity(int capturedQuantity) {
		this.capturedQuantity = capturedQuantity;
	}

	public int getPadherderQuantity() {
		return padherderQuantity;
	}

	public void setPadherderQuantity(int padherderQuantity) {
		this.padherderQuantity = padherderQuantity;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(monsterInfo.toString());
		builder.append(" : ").append(padherderQuantity).append(" -> ").append(capturedQuantity);
		return builder.toString();
	}

}
