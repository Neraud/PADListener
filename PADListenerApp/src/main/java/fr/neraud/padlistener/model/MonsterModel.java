package fr.neraud.padlistener.model;

import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * Base MonsterModel
 *
 * @author Neraud
 */
public class MonsterModel extends BaseMonsterStatsModel {

	private static final long serialVersionUID = 1L;
	private MonsterPriority priority;
	private String note;

	public MonsterPriority getPriority() {
		return priority;
	}

	public void setPriority(MonsterPriority priority) {
		this.priority = priority;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
