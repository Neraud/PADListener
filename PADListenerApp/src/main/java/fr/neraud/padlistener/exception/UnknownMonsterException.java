package fr.neraud.padlistener.exception;

/**
 * Exception thrown when trying to find MonsterInfo for an unknown monster
 *
 * @author Neraud
 */
public class UnknownMonsterException extends Exception {

	private static final long serialVersionUID = 1L;
	private final Integer monsterId;

	public UnknownMonsterException(Integer monsterId) {
		super("Unknown monster with capturedId = " + monsterId);
		this.monsterId = monsterId;
	}

	public Integer getMonsterId() {
		return monsterId;
	}
}
