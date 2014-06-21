
package fr.neraud.padlistener.exception;

/**
 * Exception thrown when the execution of a command fails
 * 
 * @author Neraud
 */
public class RootCommandExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	public RootCommandExecutionException(Throwable cause) {
		super(cause);
	}

	public RootCommandExecutionException(String detailMessage) {
		super(detailMessage);
	}

}
