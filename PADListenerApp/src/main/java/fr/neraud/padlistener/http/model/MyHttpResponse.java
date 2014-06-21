
package fr.neraud.padlistener.http.model;

/**
 * Base Http response
 * 
 * @author Neraud
 */
public class MyHttpResponse {

	private int status;

	public boolean isResponseOk() {
		return status < 400;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
