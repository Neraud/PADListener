package fr.neraud.padlistener.pad.model;

import java.util.Map;

import fr.neraud.padlistener.pad.constant.ApiAction;

/**
 * Model representing an api call from PAD to GunHo servers
 *
 * @author Neraud
 */
public class ApiCallModel {

	private ApiAction action;
	private Map<String, String> requestParams;
	private String requestContent;
	private String responseContent;

	public ApiAction getAction() {
		return action;
	}

	public void setAction(ApiAction action) {
		this.action = action;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	@Override
	public String toString() {
		return action + " : " + requestParams + " + " + requestContent + " = " + responseContent;
	}
}
