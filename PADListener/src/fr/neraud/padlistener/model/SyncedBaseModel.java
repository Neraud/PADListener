
package fr.neraud.padlistener.model;

import java.io.Serializable;

public abstract class SyncedBaseModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private MonsterInfoModel monsterInfo;
	private long padherderId;
	private T capturedInfo;
	private T padherderInfo;

	public MonsterInfoModel getMonsterInfo() {
		return monsterInfo;
	}

	public void setMonsterInfo(MonsterInfoModel monsterInfo) {
		this.monsterInfo = monsterInfo;
	}

	public long getPadherderId() {
		return padherderId;
	}

	public void setPadherderId(long padherderId) {
		this.padherderId = padherderId;
	}

	public T getCapturedInfo() {
		return capturedInfo;
	}

	public void setCapturedInfo(T capturedInfo) {
		this.capturedInfo = capturedInfo;
	}

	public T getPadherderInfo() {
		return padherderInfo;
	}

	public void setPadherderInfo(T padherderInfo) {
		this.padherderInfo = padherderInfo;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(monsterInfo.toString());
		builder.append(" : ").append(padherderInfo).append(" -> ").append(capturedInfo);
		return builder.toString();
	}
}
