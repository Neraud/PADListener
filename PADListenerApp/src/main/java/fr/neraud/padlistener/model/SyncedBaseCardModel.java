
package fr.neraud.padlistener.model;

/**
 * Base model for syncing monsters and materials
 * 
 * @author Neraud
 * @param <T>
 */
public abstract class SyncedBaseCardModel<T> extends SyncedBaseModel<T> {

	private static final long serialVersionUID = 1L;
	private MonsterInfoModel monsterInfo;
	private long padherderId;

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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(monsterInfo.toString());
		builder.append(" : ").append(getPadherderInfo()).append(" -> ").append(getCapturedInfo());
		return builder.toString();
	}
}
