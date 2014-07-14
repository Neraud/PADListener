package fr.neraud.padlistener.exception;

import java.util.List;

/**
 * Exception thrown when the execution of a command fails
 *
 * @author Neraud
 */
public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = 1L;
	private List<String> logs;

	public CommandExecutionException(Throwable cause) {
		super(cause);
	}

	public CommandExecutionException(String detailMessage) {
		super(detailMessage);
	}

	public CommandExecutionException(String detailMessage, List<String> logs) {
		this(detailMessage);
		this.logs = logs;
	}

	public List<String> getLogs() {
		return logs;
	}
}
