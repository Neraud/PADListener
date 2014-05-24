
package fr.neraud.padlistener.http.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;

import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.model.ImageDownloadResponse;

public class ImageDownloadClient extends MyHttpClientClient<ImageDownloadResponse> {

	public ImageDownloadClient(String endpointUrl) {
		super(endpointUrl);
	}

	@Override
	protected ImageDownloadResponse createResultFromResponse(final HttpResponse httpResponse) throws HttpCallException {
		final ImageDownloadResponse result = new ImageDownloadResponse();

		final int status = httpResponse.getStatusLine().getStatusCode();
		result.setStatus(status);

		boolean success = false;
		InputStream inputStream = null;
		try {
			inputStream = httpResponse.getEntity().getContent();
			result.setInputStream(inputStream);
			success = true;
			return result;
		} catch (final IllegalStateException e) {
			throw new HttpCallException(e);
		} catch (final IOException e) {
			throw new HttpCallException(e);
		} finally {
			// Close when exception only, the stream needs to be opened ouside.
			if (!success && inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
				}
			}
		}

	}

}
