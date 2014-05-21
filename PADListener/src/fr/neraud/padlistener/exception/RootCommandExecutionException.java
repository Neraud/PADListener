
package fr.neraud.padlistener.exception;

public class RootCommandExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	public RootCommandExecutionException(Throwable cause) {
		super(cause);
	}

	public RootCommandExecutionException(String detailMessage) {
		super(detailMessage);
	}

}
