package fr.neraud.padlistener.exception;

/**
 * Exception thrown when the execution of a command fails
 *
 * @author Neraud
 */
public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommandExecutionException(Throwable cause) {
		super(cause);
	}

	public CommandExecutionException(String detailMessage) {
		super(detailMessage);
	}

}
