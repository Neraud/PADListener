package fr.neraud.padlistener.model;

/**
 * Created by Neraud on 15/08/2014.
 */
public class PADHerderAccountModel {

	private int accountId;
	private String login;
	private String password;
	private String name;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
