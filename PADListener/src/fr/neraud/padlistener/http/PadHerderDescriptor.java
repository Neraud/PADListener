
package fr.neraud.padlistener.http;

import fr.neraud.padlistener.http.constant.HttpMethod;
import fr.neraud.padlistener.http.model.RestRequest;

public class PadHerderDescriptor {

	public static final String serverUrl = "https://www.padherder.com";

	/**
	 * Helper for the Request
	 * 
	 * @author Neraud
	 */
	public static class RequestHelper {

		public static RestRequest initRequestForMonsterInfo() {
			return initRequest(Services.MONSTER_INFO);
		}

		public static RestRequest initRequestForUserInfo(String userName) {
			final RestRequest restRequest = new RestRequest();
			final String url = Services.USER_INFO.apiUrl.replaceAll("\\[userName\\]", userName);
			restRequest.setUrl(url);
			restRequest.setMethod(Services.USER_INFO.method);
			return restRequest;
		}

		private static RestRequest initRequest(Services service) {
			final RestRequest restRequest = new RestRequest();
			restRequest.setUrl(service.apiUrl);
			restRequest.setMethod(service.method);
			return restRequest;
		}
	}

	/**
	 * Services used by PadHerder
	 * 
	 * @author Neraud
	 */
	private enum Services {

		MONSTER_INFO("/api/monsters/", HttpMethod.GET),
		USER_INFO("/user-api/user/[userName]", HttpMethod.GET);

		private final String apiUrl;
		private final HttpMethod method;

		private Services(String apiUrl, HttpMethod method) {
			this.apiUrl = apiUrl;
			this.method = method;
		}

	}
}
