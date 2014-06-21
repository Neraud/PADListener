package fr.neraud.padlistener.http.exception;

/**
 * Exception thrown when a rest call fails
 *
 * @author Neraud
 */
public class HttpCallException extends Exception {

	private static final long serialVersionUID = -7659278082777602362L;

	public HttpCallException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public HttpCallException(String detailMessage) {
		super(detailMessage);
	}

	public HttpCallException(Throwable throwable) {
		super(throwable);
	}

}
