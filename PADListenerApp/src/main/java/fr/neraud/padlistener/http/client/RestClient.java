package fr.neraud.padlistener.http.client;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.model.RestResponse;

/**
 * HttpClient used for rest calls
 *
 * @author Neraud
 */
public class RestClient extends MyHttpClientClient<RestResponse> {

	public RestClient(Context context, String endpointUrl) {
		super(context, endpointUrl);
	}

	@Override
	protected RestResponse createResultFromResponse(final HttpResponse httpResponse) throws HttpCallException {
		Log.d(getClass().getName(), "createResultFromResponse");
		final RestResponse result = new RestResponse();

		final int status = httpResponse.getStatusLine().getStatusCode();
		result.setStatus(status);

		if (httpResponse.getEntity() != null) {
			InputStream inputStream = null;
			try {
				inputStream = httpResponse.getEntity().getContent();
				final Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
				final String stringResult = scanner.hasNext() ? scanner.next() : "";
				Log.d(getClass().getName(), "createResultFromResponse : " + stringResult);
				result.setContentResult(stringResult);
			} catch (final IllegalStateException e) {
				throw new HttpCallException(e);
			} catch (final IOException e) {
				throw new HttpCallException(e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (final IOException e) {
						Log.w(getClass().getName(), "createResultFromResponse : error closing in stream");
					}
				}
			}
		}

		return result;
	}

}
