package fr.neraud.padlistener.http.client;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.model.ImageDownloadResponse;

/**
 * HttpClient used to download images.
 *
 * @author Neraud
 */
public class ImageDownloadClient extends MyHttpClientClient<ImageDownloadResponse> {

	public ImageDownloadClient(Context context, String endpointUrl) {
		super(context, endpointUrl);
	}

	@Override
	protected ImageDownloadResponse createResultFromResponse(final HttpResponse httpResponse) throws HttpCallException {
		Log.d(getClass().getName(), "createResultFromResponse");
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
			// Close when exception only, the stream needs to be opened outside.
			if (!success && inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "createResultFromResponse : error closing in stream");
				}
			}
		}

	}

}
