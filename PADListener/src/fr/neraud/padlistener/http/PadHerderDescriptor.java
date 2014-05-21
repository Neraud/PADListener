
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

		MONSTER_INFO("/api/monsters/", HttpMethod.GET);

		private final String apiUrl;
		private final HttpMethod method;

		private Services(String apiUrl, HttpMethod method) {
			this.apiUrl = apiUrl;
			this.method = method;
		}

	}
}
