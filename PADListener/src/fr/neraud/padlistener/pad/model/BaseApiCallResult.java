
package fr.neraud.padlistener.pad.model;

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
