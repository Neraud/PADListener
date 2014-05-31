
package fr.neraud.padlistener.proxy.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.sandrop.webscarab.httpclient.HTTPClient;
import org.sandrop.webscarab.model.HttpUrl;
import org.sandrop.webscarab.model.Request;
import org.sandrop.webscarab.model.Response;
import org.sandrop.webscarab.plugin.proxy.ProxyPlugin;

import android.content.Context;
import android.util.Log;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.pad.constant.ApiAction;
import fr.neraud.padlistener.pad.model.ApiCallModel;

public class PADPlugin extends ProxyPlugin {

	private boolean _enabled = true;
	private final Context context;
	private final String targetHost;

	public PADPlugin(Context context) {
		this.context = context;
		targetHost = new DefaultSharedPreferencesHelper(context).getListenerTargetHostname();
	}

	public void parseProperties() {
	}

	@Override
	public String getPluginName() {
		return new String("PADListenerPlugin");
	}

	public void setEnabled(boolean bool) {
		_enabled = bool;
	}

	@Override
	public boolean getEnabled() {
		return _enabled;
	}

	@Override
	public HTTPClient getProxyPlugin(HTTPClient in) {
		return new Plugin(in);
	}

	private class Plugin implements HTTPClient {

		private final HTTPClient _in;

		public Plugin(HTTPClient in) {
			_in = in;
		}

		@Override
		public Response fetchResponse(Request request) throws IOException {
			if (_enabled) {
				Log.d(PADPlugin.class.getName(), "fetchResponse");
				final Response response = _in.fetchResponse(request);

				final HttpUrl reqUrl = request.getURL();
				final String reqHost = reqUrl.getHost();
				final String reqPath = reqUrl.getPath();

				if ("200".equals(response.getStatus()) && targetHost.equals(reqHost) && "/api.php".equals(reqPath)) {
					final byte[] requestContentByte = request.getContent();
					final String requestContentString = new String(requestContentByte);
					final Map<String, String> requestParams = extractParams(reqUrl);
					final String actionString = requestParams.get("action");
					final ApiAction action = ApiAction.fromString(actionString);

					final byte[] reponseContentByte = response.getContent();
					final String responseContentString = new String(reponseContentByte);

					final ApiCallModel model = new ApiCallModel();
					model.setAction(action);
					model.setRequestParams(requestParams);
					model.setRequestContent(requestContentString);
					model.setResponseContent(responseContentString);

					Log.d(PADPlugin.class.getName(), "" + model);
					new ApiCallHandlerThread(context, model).start();
				}

				return response;
			}
			// just make normal action whitout any custom parsing
			return _in.fetchResponse(request);
		}

		private Map<String, String> extractParams(final HttpUrl reqUrl) {
			final Map<String, String> params = new HashMap<String, String>();
			// param1=value1&param2=value2
			final String reqQuery = reqUrl.getQuery();
			if (reqQuery != null && !"".equals(reqQuery)) {
				final String[] reqParams = reqQuery.split("&");
				for (final String oneParam : reqParams) {
					final String[] oneParamSplit = oneParam.split("=");
					params.put(oneParamSplit[0], oneParamSplit[1]);
				}
			}
			return params;
		}
	}

}
