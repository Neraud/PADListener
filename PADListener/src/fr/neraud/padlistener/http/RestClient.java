
package fr.neraud.padlistener.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;

import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.model.RestResponse;

public class RestClient extends MyHttpClientClient<RestResponse> {

	public RestClient(String endpointUrl) {
		super(endpointUrl);
	}

	@Override
	protected RestResponse createResultFromResponse(final HttpResponse httpResponse) throws HttpCallException {
		final RestResponse result = new RestResponse();

		final int status = httpResponse.getStatusLine().getStatusCode();
		result.setStatus(status);

		InputStream inputStream = null;
		try {
			inputStream = httpResponse.getEntity().getContent();
			final Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
			final String stringResult = scanner.hasNext() ? scanner.next() : "";
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
				}
			}
		}

		return result;
	}

}
