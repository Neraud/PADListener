package fr.neraud.padlistener.proxy.plugin;

import android.content.Context;

import org.sandrop.webscarab.httpclient.HTTPClient;
import org.sandrop.webscarab.model.HttpUrl;
import org.sandrop.webscarab.model.Request;
import org.sandrop.webscarab.model.Response;
import org.sandrop.webscarab.plugin.proxy.ProxyPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.pad.constant.ApiAction;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.service.ListenerService;

/**
 * ProxyPlugin to capture PAD calls to GunHo servers
 *
 * @author Neraud
 */
public class PADPlugin extends ProxyPlugin {

	private final Context context;
	private final ListenerService.CaptureListener captureListener;
	private final Set<String> targetHostnames;
	private boolean _enabled = true;

	private class Plugin implements HTTPClient {

		private final HTTPClient _in;

		public Plugin(HTTPClient in) {
			_in = in;
		}

		@Override
		public Response fetchResponse(Request request) throws IOException {
			if (_enabled) {
				final Response response = _in.fetchResponse(request);

				final HttpUrl reqUrl = request.getURL();
				MyLog.debug("reqUrl = " + reqUrl);
				final String reqHost = reqUrl.getHost();
				final String reqPath = reqUrl.getPath();

				// Read only calls to GunHo API, with a 200 HTTP code
				if ("200".equals(response.getStatus()) && targetHostnames.contains(reqHost) && "/api.php".equals(reqPath)) {
					final byte[] requestContentByte = request.getContent();
					final String requestContentString = new String(requestContentByte);
					final Map<String, String> requestParams = extractParams(reqUrl);
					final PADRegion region = extractRegion(reqHost);
					final String actionString = requestParams.get("action");
					final ApiAction action = ApiAction.fromString(actionString);

					final byte[] responseContentByte = response.getContent();
					final String responseContentString = new String(responseContentByte);

					final ApiCallModel model = new ApiCallModel();
					model.setAction(action);
					model.setRegion(region);
					model.setRequestParams(requestParams);
					model.setRequestContent(requestContentString);
					model.setResponseContent(responseContentString);

					MyLog.debug("model = " + model);
					final ApiCallHandlerThread handler = new ApiCallHandlerThread(context, model, captureListener);
					handler.start();
				}

				return response;
			}
			// just make normal action without any custom parsing
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

		private PADRegion extractRegion(String reqHost) {
			final PADVersion server = PADVersion.fromHostName(reqHost);

			if (server != null) {
				return server.getRegion();
			}

			MyLog.warn("host is not in the standard one, return US by default");
			return PADRegion.US;
		}
	}

	public PADPlugin(Context context, ListenerService.CaptureListener captureListener) {
		this.context = context;
		this.captureListener = captureListener;
		targetHostnames = new DefaultSharedPreferencesHelper(context).getAllListenerTargetHostnames();
	}

	@Override
	public String getPluginName() {
		return "PADListenerPlugin";
	}

	@Override
	public boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean bool) {
		_enabled = bool;
	}

	@Override
	public HTTPClient getProxyPlugin(HTTPClient in) {
		return new Plugin(in);
	}

}
