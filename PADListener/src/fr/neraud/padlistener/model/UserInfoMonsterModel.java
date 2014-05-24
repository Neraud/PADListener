
package fr.neraud.padlistener.model;

public class UserInfoMonsterModel extends BaseMonsterModel {

	private int priority;
	private Integer targetEvolutionId;
	private String note;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
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
