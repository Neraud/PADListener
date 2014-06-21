
package fr.neraud.padlistener.http.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

		public static MyHttpRequest initRequestForGetUserInfo(Context context, int accountId) {
			final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);

			String cleanedAccountName = null;
			try {
				cleanedAccountName = URLEncoder.encode(helper.getPadHerderUserName(accountId), "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				// Should never happen
				throw new RuntimeException(e);
			}

			final String url = Services.GET_USER_INFO.apiUrl.replaceAll("\\[userName\\]", cleanedAccountName);
			return initRequest(Services.GET_USER_INFO, context, accountId, url);
		}

		public static MyHttpRequest initRequestForUpdateUserInfo(Context context, int accountId, int profileApiId) {
			final String url = Services.PATCH_USER_INFO.apiUrl.replaceAll("\\[id\\]", String.valueOf(profileApiId));
			return initRequest(Services.PATCH_USER_INFO, context, accountId, url);
		}

		public static MyHttpRequest initRequestForPatchMaterial(Context context, int accountId, long padherderMaterialId) {
			final String url = Services.PATCH_MATERIAL.apiUrl.replaceAll("\\[id\\]", String.valueOf(padherderMaterialId));
			return initRequest(Services.PATCH_MATERIAL, context, accountId, url);
		}

		public static MyHttpRequest initRequestForPatchMonster(Context context, int accountId, long padherderMonsterId) {
			final String url = Services.PATCH_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(padherderMonsterId));
			return initRequest(Services.PATCH_MONSTER, context, accountId, url);
		}

		public static MyHttpRequest initRequestForPostMonster(Context context, int accountId) {
			return initRequest(Services.POST_MONSTER, context, accountId, Services.POST_MONSTER.apiUrl);
		}

		public static MyHttpRequest initRequestForDeleteMonster(Context context, int accountId, long padherderMonsterId) {
			final String url = Services.DELETE_MONSTER.apiUrl.replaceAll("\\[id\\]", String.valueOf(padherderMonsterId));
			return initRequest(Services.DELETE_MONSTER, context, accountId, url);
		}

		private static MyHttpRequest initRequest(Services service) {
			final MyHttpRequest restRequest = new MyHttpRequest();
			restRequest.setUrl(service.apiUrl);
			restRequest.setMethod(service.method);
			restRequest.setHeaderAccept("application/json");
			restRequest.setHeaderContentType("application/json");
			return restRequest;
		}

		private static MyHttpRequest initRequest(Services service, Context context, int accountId, String url) {
			final MyHttpRequest restRequest = new MyHttpRequest();
			restRequest.setUrl(url);
			restRequest.setMethod(service.method);
			restRequest.setHeaderAccept("application/json");
			restRequest.setHeaderContentType("application/json");
			if (service.needsAuth) {
				final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);
				restRequest.setBasicAuthEnabled(true);
				restRequest.setBasicAuthUserName(helper.getPadHerderUserName(accountId));
				restRequest.setBasicAuthUserPassword(helper.getPadHerderUserPassword(accountId));
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
		PATCH_USER_INFO("/user-api/profile/[id]/", HttpMethod.PATCH, true),
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
