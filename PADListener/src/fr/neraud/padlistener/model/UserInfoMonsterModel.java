
package fr.neraud.padlistener.model;

/**
 * UserInfoMaterial model resulting from a PADherder user-api call for the monsters section
 * 
 * @author Neraud
 */
public class UserInfoMonsterModel extends BaseMonsterModel {

	private static final long serialVersionUID = 1L;
	private long padherderId;
	private int priority;
	private Integer targetEvolutionId;
	private String note;

	public long getPadherderId() {
		return padherderId;
	}

	public void setPadherderId(long padherderId) {
		this.padherderId = padherderId;
	}

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
