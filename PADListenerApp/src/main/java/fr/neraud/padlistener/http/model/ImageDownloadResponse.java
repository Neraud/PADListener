package fr.neraud.padlistener.http.model;

import java.io.InputStream;

/**
 * Http response used when downloading images
 *
 * @author Neraud
 */
public class ImageDownloadResponse extends MyHttpResponse {

	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
