package fr.neraud.padlistener.model;

import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * UserInfoMaterial model resulting from a PADherder user-api call for the monsters section
 *
 * @author Neraud
 */
public class UserInfoMonsterModel extends MonsterModel {

	private static final long serialVersionUID = 1L;
	private long padherderId;
	private MonsterPriority priority;
	private Integer targetEvolutionId;
	private String note;

	public long getPadherderId() {
		return padherderId;
	}

	public void setPadherderId(long padherderId) {
		this.padherderId = padherderId;
	}

	public MonsterPriority getPriority() {
		return priority;
	}

	public void setPriority(MonsterPriority priority) {
		this.priority = priority;
	}

	public Integer getTargetEvolutionId() {
		return targetEvolutionId;
	}

	public void setTargetEvolutionId(Integer targetEvolutionId) {
		this.targetEvolutionId = targetEvolutionId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
