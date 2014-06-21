package fr.neraud.padlistener.exception;

/**
 * Exception thrown when trying to find MonsterInfo for an unknown monster
 *
 * @author Neraud
 */
public class UnknownMonsterException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownMonsterException(String message) {
		super(message);
	}
}
