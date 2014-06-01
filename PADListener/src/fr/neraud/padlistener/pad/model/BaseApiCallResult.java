
package fr.neraud.padlistener.pad.model;

/**
 * Base model representing the result of a PAD call to GunHo servers
 * 
 * @author Neraud
 */
public class BaseApiCallResult {

	private int res;

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public boolean isResOk() {
		return 0 == res;
	}
}
