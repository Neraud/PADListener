package fr.neraud.padlistener.model;

/**
 * UserInfoMaterial model resulting from a PADherder user-api call for the monsters section
 *
 * @author Neraud
 */
public class UserInfoMonsterModel extends MonsterModel {

	private static final long serialVersionUID = 1L;
	private long padherderId;
	private Integer targetEvolutionIdJp;

	public long getPadherderId() {
		return padherderId;
	}

	public void setPadherderId(long padherderId) {
		this.padherderId = padherderId;
	}

	public Integer getTargetEvolutionIdJp() {
		return targetEvolutionIdJp;
	}

	public void setTargetEvolutionIdJp(Integer targetEvolutionIdJp) {
		this.targetEvolutionIdJp = targetEvolutionIdJp;
	}

}
