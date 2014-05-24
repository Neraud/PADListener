
package fr.neraud.padlistener.http.helper;

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

		public static RestRequest initRequestForGetMonsterInfo() {
			return initRequest(Services.GET_MONSTER_INFO);
		}

		public static RestRequest initRequestForGetUserInfo(String userName) {
			final String url = Services.GET_USER_INFO.apiUrl.replaceAll("\\[userName\\]", userName);
			return initRequest(Services.GET_USER_INFO, url);
		}

		public static RestRequest initRequestForPatchMaterial(long materialId) {
			final String url = Services.PATCH_MATERIAL.apiUrl.replaceAll("\\[id\\]", String.valueOf(materialId));
			return initRequest(Services.PATCH_MATERIAL, url);
		}

		public static RestRequest initRequestForPatchMonster(long monsterId) {
			final String url = Services.PATCH_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(monsterId));
			return initRequest(Services.PATCH_MONSTER, url);
		}

		public static RestRequest initRequestForPostMonster(long monsterId) {
			final String url = Services.POST_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(monsterId));
			return initRequest(Services.POST_MONSTER, url);
		}

		public static RestRequest initRequestForDeleteMonster(long monsterId) {
			final String url = Services.DELETE_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(monsterId));
			return initRequest(Services.DELETE_MONSTER, url);
		}

		private static RestRequest initRequest(Services service) {
			final RestRequest restRequest = new RestRequest();
			restRequest.setUrl(service.apiUrl);
			restRequest.setMethod(service.method);
			return restRequest;
		}

		private static RestRequest initRequest(Services service, String url) {
			final RestRequest restRequest = new RestRequest();
			restRequest.setUrl(url);
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

		GET_MONSTER_INFO("/api/monsters/", HttpMethod.GET),
		GET_USER_INFO("/user-api/user/[userName]", HttpMethod.GET),
		PATCH_MATERIAL("/user-api/material/[id]", HttpMethod.PATCH),
		PATCH_MONSTER("/user-api/monster/[id]", HttpMethod.PATCH),
		POST_MONSTER("/user-api/monster/", HttpMethod.POST),
		DELETE_MONSTER("/user-api/monster/[id]/", HttpMethod.DELETE);

		private final String apiUrl;
		private final HttpMethod method;

		private Services(String apiUrl, HttpMethod method) {
			this.apiUrl = apiUrl;
			this.method = method;
		}

	}
}
