
package fr.neraud.padlistener.http.exception;

/**
 * Exception thrown when a rest call fails
 * 
 * @author Neraud
 */
public class HttpResponseException extends Exception {

	private static final long serialVersionUID = -7659278082777602362L;
	private final int code;

	public HttpResponseException(int code, String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		this.code = code;
	}

	public HttpResponseException(int code, String detailMessage) {
		super(detailMessage);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
