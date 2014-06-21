
package fr.neraud.padlistener.http.helper;

import org.apache.http.client.methods.HttpPost;

/**
 * Patch method for HttpClient
 * 
 * @author Neraud
 */
public class HttpPatch extends HttpPost {

	public static final String METHOD_PATCH = "PATCH";

	public HttpPatch(final String url) {
		super(url);
	}

	@Override
	public String getMethod() {
		return METHOD_PATCH;
	}
}
