package fr.neraud.padlistener.http.model;

/**
 * Http response used for rest calls
 *
 * @author Neraud
 */
public class RestResponse extends MyHttpResponse {

	private String contentResult;

	public String getContentResult() {
		return contentResult;
	}

	public void setContentResult(String contentResult) {
		this.contentResult = contentResult;
	}

}
