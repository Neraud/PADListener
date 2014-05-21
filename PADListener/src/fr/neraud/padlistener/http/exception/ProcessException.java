
package fr.neraud.padlistener.http.exception;

/**
 * Exception thrown when a process fails
 * 
 * @author Neraud
 */
public class ProcessException extends Exception {

	private static final long serialVersionUID = -7659278082777602362L;

	public ProcessException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ProcessException(String detailMessage) {
		super(detailMessage);
	}

	public ProcessException(Throwable throwable) {
		super(throwable);
	}

}
