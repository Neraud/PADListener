package fr.neraud.padlistener.exception;

/**
 * Created by Neraud on 23/11/2014.
 */
public class NoMatchingAccountException extends Exception {

	private final String accountName;

	public NoMatchingAccountException(String accountName) {
		super();
		this.accountName = accountName;
	}

	public String getAccountName() {
		return accountName;
	}
}
