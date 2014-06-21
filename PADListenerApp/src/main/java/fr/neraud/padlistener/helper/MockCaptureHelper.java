package fr.neraud.padlistener.helper;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fr.neraud.padlistener.pad.constant.ApiAction;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.proxy.plugin.ApiCallHandlerThread;

/**
 * Helper to load a mock capture
 *
 * @author Neraud
 */
public class MockCaptureHelper {

	private static final String MOCK_CAPTURE_FILE_NAME = "capture.json";
	private final Context context;

	public MockCaptureHelper(Context context) {
		super();
		this.context = context;
	}

	public void loadMockCapture() {
		Log.d(getClass().getName(), "loadMockCapture");
		try {
			final ApiCallModel model = new ApiCallModel();
			model.setAction(ApiAction.GET_PLAYER_DATA);
			//model.setRequestParams(requestParams);
			//model.setRequestContent(requestContentString);
			model.setResponseContent(extractResponseContent());
			new ApiCallHandlerThread(context, model).start();
		} catch (final IOException e) {
			Log.e(getClass().getName(), "loadMockCapture", e);
		}
	}

	private String extractResponseContent() throws IOException {
		InputStream is = null;
		String result = null;
		try {
			is = context.getAssets().open(MOCK_CAPTURE_FILE_NAME);
			final List<String> lines = IOUtils.readLines(is);
			final StringBuilder builder = new StringBuilder();
			for (final String line : lines) {
				builder.append(line);
			}
			result = builder.toString();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "extractResponseContent : error closing in stream");
				}
			}
		}
		return result;
	}
}
