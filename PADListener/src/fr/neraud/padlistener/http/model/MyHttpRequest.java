
package fr.neraud.padlistener.http.model;

import java.util.Map;

import fr.neraud.padlistener.http.constant.HttpMethod;

public class MyHttpRequest {

	private HttpMethod method;
	private String url;
	private Map<String, String> urlParams;

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

}
