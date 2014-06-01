
package fr.neraud.padlistener.http.helper;

import android.content.Context;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.http.constant.HttpMethod;
import fr.neraud.padlistener.http.model.MyHttpRequest;

/**
 * Descriptor for PADherder
 * 
 * @author Neraud
 */
public class PadHerderDescriptor {

	public static final String serverUrl = "https://www.padherder.com";

	/**
	 * Helper to create requests
	 * 
	 * @author Neraud
	 */
	public static class RequestHelper {

		public static MyHttpRequest initRequestForGetMonsterInfo() {
			return initRequest(Services.GET_MONSTER_INFO);
		}

		public static MyHttpRequest initRequestForGetUserInfo(Context context) {
			final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);
			final String url = Services.GET_USER_INFO.apiUrl.replaceAll("\\[userName\\]", helper.getPadHerderUserName());
			return initRequest(Services.GET_USER_INFO, context, url);
		}

		public static MyHttpRequest initRequestForPatchMaterial(Context context, long padherderMaterialId) {
			final String url = Services.PATCH_MATERIAL.apiUrl.replaceAll("\\[id\\]", String.valueOf(padherderMaterialId));
			return initRequest(Services.PATCH_MATERIAL, context, url);
		}

		public static MyHttpRequest initRequestForPatchMonster(Context context, long padherderMonsterId) {
			final String url = Services.PATCH_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(padherderMonsterId));
			return initRequest(Services.PATCH_MONSTER, context, url);
		}

		public static MyHttpRequest initRequestForPostMonster(Context context) {
			return initRequest(Services.POST_MONSTER, context, Services.POST_MONSTER.apiUrl);
		}

		public static MyHttpRequest initRequestForDeleteMonster(Context context, long padherderMonsterId) {
			final String url = Services.DELETE_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(padherderMonsterId));
			return initRequest(Services.DELETE_MONSTER, context, url);
		}

		private static MyHttpRequest initRequest(Services service) {
			final MyHttpRequest restRequest = new MyHttpRequest();
			restRequest.setUrl(service.apiUrl);
			restRequest.setMethod(service.method);
			restRequest.setHeaderAccept("application/json");
			restRequest.setHeaderContentType("application/json");
			return restRequest;
		}

		private static MyHttpRequest initRequest(Services service, Context context, String url) {
			final MyHttpRequest restRequest = new MyHttpRequest();
			restRequest.setUrl(url);
			restRequest.setMethod(service.method);
			restRequest.setHeaderAccept("application/json");
			restRequest.setHeaderContentType("application/json");
			if (service.needsAuth) {
				final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);
				restRequest.setBasicAuthEnabled(true);
				restRequest.setBasicAuthUserName(helper.getPadHerderUserName());
				restRequest.setBasicAuthUserPassword(helper.getPadHerderUserPassword());
			}
			return restRequest;
		}
	}

	/**
	 * Services used by PadHerder
	 * 
	 * @author Neraud
	 */
	private enum Services {

		GET_MONSTER_INFO("/api/monsters/", HttpMethod.GET, false),
		GET_USER_INFO("/user-api/user/[userName]/", HttpMethod.GET, true),
		PATCH_MATERIAL("/user-api/material/[id]/", HttpMethod.PATCH, true),
		PATCH_MONSTER("/user-api/monster/[id]/", HttpMethod.PATCH, true),
		POST_MONSTER("/user-api/monster/", HttpMethod.POST, true),
		DELETE_MONSTER("/user-api/monster/[id]/", HttpMethod.DELETE, true);

		private final String apiUrl;
		private final HttpMethod method;
		private final boolean needsAuth;

		private Services(String apiUrl, HttpMethod method, boolean needsAuth) {
			this.apiUrl = apiUrl;
			this.method = method;
			this.needsAuth = needsAuth;
		}

	}
}
