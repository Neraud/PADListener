
package fr.neraud.padlistener.http.exception;

/**
 * Exception thrown when a rest call fails
 * 
 * @author Neraud
 */
public class HttpResponseException extends Exception {

	private static final long serialVersionUID = -7659278082777602362L;

	public HttpResponseException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public HttpResponseException(String detailMessage) {
		super(detailMessage);
	}

	public HttpResponseException(Throwable throwable) {
		super(throwable);
	}

}
