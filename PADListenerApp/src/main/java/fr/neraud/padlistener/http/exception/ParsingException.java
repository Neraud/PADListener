
package fr.neraud.padlistener.http.exception;

/**
 * Exception thrown when a parsing fails
 * 
 * @author Neraud
 */
public class ParsingException extends Exception {

	private static final long serialVersionUID = -7659278082777602362L;

	public ParsingException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ParsingException(String detailMessage) {
		super(detailMessage);
	}

	public ParsingException(Throwable throwable) {
		super(throwable);
	}

}
