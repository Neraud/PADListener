
package fr.neraud.padlistener.http.client;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Base64;
import android.util.Log;
import fr.neraud.padlistener.http.constant.HttpMethod;
import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.helper.HttpPatch;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.http.model.MyHttpResponse;

public abstract class MyHttpClientClient<R extends MyHttpResponse> {

	private final String endpointUrl;
	private final HttpClient httpclient = new DefaultHttpClient();

	public MyHttpClientClient(String endpointUrl) {
		super();
		this.endpointUrl = endpointUrl;
	}

	public R call(MyHttpRequest httpRequest) throws HttpCallException {
		Log.d(getClass().getName(), "call");

		final String fullUrl = createFullUrl(httpRequest);
		final HttpRequestBase httpMethod = createMethod(httpRequest.getMethod(), fullUrl);
		httpMethod.setHeader("user-agent", "PADListener");

		if (httpRequest.isBasicAuthEnabled()) {
			Log.d(getClass().getName(), "call : adding basic auth with user " + httpRequest.getBasicAuthUserName());
			final byte[] authorizationBytes = (httpRequest.getBasicAuthUserName() + ":" + httpRequest.getBasicAuthUserPassword())
			        .getBytes();
			final String authorizationString = "Basic " + Base64.encodeToString(authorizationBytes, Base64.NO_WRAP);
			httpMethod.setHeader("Authorization", authorizationString);
		}
		if (httpRequest.getHeaderAccept() != null) {
			httpMethod.setHeader("Accept", httpRequest.getHeaderAccept());
		}
		if (httpRequest.getHeaderContentType() != null) {
			httpMethod.setHeader("Content-type", httpRequest.getHeaderContentType());
		}

		try {
			if (httpRequest.getBody() != null) {
				Log.d(getClass().getName(), "call : setting body : " + httpRequest.getBody());
				final StringEntity entity = new StringEntity(httpRequest.getBody(), "UTF-8");
				switch (httpRequest.getMethod()) {
				case POST:
					((HttpPost) httpMethod).setEntity(entity);
					break;
				case PUT:
					((HttpPut) httpMethod).setEntity(entity);
					break;
				case PATCH:
					((HttpPatch) httpMethod).setEntity(entity);
					break;
				default:

				}
			}

			Log.d(getClass().getName(), "call : " + httpMethod.getURI());
			final HttpResponse httpResponse = httpclient.execute(httpMethod);

			final R result = createResultFromResponse(httpResponse);
			return result;
		} catch (final ClientProtocolException e) {
			throw new HttpCallException(e);
		} catch (final IOException e) {
			throw new HttpCallException(e);
		}
	}

	protected abstract R createResultFromResponse(final HttpResponse httpResponse) throws HttpCallException;

	private String createFullUrl(MyHttpRequest httpRequest) {
		final StringBuilder urlBuilder = new StringBuilder(endpointUrl);

		final String apiUrl = httpRequest.getUrl();
		if (apiUrl != null) {
			if (endpointUrl.endsWith("/") && apiUrl.startsWith("/")) {
				urlBuilder.deleteCharAt(urlBuilder.length() - 1);
			}
			urlBuilder.append(apiUrl);
		}
		final Map<String, String> urlParams = httpRequest.getUrlParams();
		if (urlParams != null) {
			boolean first = true;
			for (final String paramName : urlParams.keySet()) {
				if (first) {
					first = false;
					urlBuilder.append("?");
				} else {
					urlBuilder.append("&");
				}
				urlBuilder.append(paramName).append("=").append(urlParams.get(paramName));
			}
		}
		return urlBuilder.toString();
	}

	private HttpRequestBase createMethod(HttpMethod method, String fullUrl) {
		switch (method) {
		case GET:
			return new HttpGet(fullUrl);
		case POST:
			return new HttpPost(fullUrl);
		case PUT:
			return new HttpPut(fullUrl);
		case PATCH:
			return new HttpPatch(fullUrl);
		case DELETE:
			return new HttpDelete(fullUrl);
		default:
			throw new IllegalArgumentException(method + " is not an allowed method");
		}
	}
}
