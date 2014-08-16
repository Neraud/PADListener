package fr.neraud.padlistener.model;

/**
 * Base model for syncing monsters and materials
 *
 * @author Neraud
 */
public abstract class SyncedBaseCardModel<T> extends SyncedBaseModel<T> {

	private static final long serialVersionUID = 1L;
	private MonsterInfoModel padherderMonsterInfo;
	private MonsterInfoModel capturedMonsterInfo;
	private long padherderId;

	public MonsterInfoModel getCapturedMonsterInfo() {
		return capturedMonsterInfo;
	}

	public void setCapturedMonsterInfo(MonsterInfoModel capturedMonsterInfo) {
		this.capturedMonsterInfo = capturedMonsterInfo;
	}

	public MonsterInfoModel getPadherderMonsterInfo() {
		return padherderMonsterInfo;
	}

	public void setPadherderMonsterInfo(MonsterInfoModel padherderMonsterInfo) {
		this.padherderMonsterInfo = padherderMonsterInfo;
	}

	public long getPadherderId() {
		return padherderId;
	}

	public void setPadherderId(long padherderId) {
		this.padherderId = padherderId;
	}

	public MonsterInfoModel getDisplayedMonsterInfo() {
		return (capturedMonsterInfo != null) ? capturedMonsterInfo : padherderMonsterInfo;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(getDisplayedMonsterInfo().toString());
		builder.append(" : ").append(getPadherderInfo()).append(" -> ").append(getCapturedInfo());
		return builder.toString();
	}
}
