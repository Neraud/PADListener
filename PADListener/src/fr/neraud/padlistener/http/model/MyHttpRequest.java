
package fr.neraud.padlistener.http.model;

import java.util.Map;

import fr.neraud.padlistener.http.constant.HttpMethod;

/**
 * Http request
 * 
 * @author Neraud
 */
public class MyHttpRequest {

	private HttpMethod method;
	private String url;
	private Map<String, String> urlParams;
	private String headerAccept;
	private String headerContentType;
	private String body;
	private boolean basicAuthEnabled = false;
	private String basicAuthUserName;
	private String basicAuthUserPassword;

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(Map<String, String> urlParams) {
		this.urlParams = urlParams;
	}

	public String getHeaderAccept() {
		return headerAccept;
	}

	public void setHeaderAccept(String headerAccept) {
		this.headerAccept = headerAccept;
	}

	public String getHeaderContentType() {
		return headerContentType;
	}

	public void setHeaderContentType(String headerContentType) {
		this.headerContentType = headerContentType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isBasicAuthEnabled() {
		return basicAuthEnabled;
	}

	public void setBasicAuthEnabled(boolean basicAuthEnabled) {
		this.basicAuthEnabled = basicAuthEnabled;
	}

	public String getBasicAuthUserName() {
		return basicAuthUserName;
	}

	public void setBasicAuthUserName(String basicAuthUserName) {
		this.basicAuthUserName = basicAuthUserName;
	}

	public String getBasicAuthUserPassword() {
		return basicAuthUserPassword;
	}

	public void setBasicAuthUserPassword(String basicAuthUserPassword) {
		this.basicAuthUserPassword = basicAuthUserPassword;
	}

}
