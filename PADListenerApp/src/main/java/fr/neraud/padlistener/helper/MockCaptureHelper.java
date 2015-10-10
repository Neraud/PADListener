package fr.neraud.padlistener.helper;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.PADRegion;
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

	public boolean hasMockData() {
		MyLog.entry();
		InputStream is = null;
		boolean result = false;

		try {
			is = context.getAssets().open(MOCK_CAPTURE_FILE_NAME);
			MyLog.debug("Mock data found");
			result = true;
		} catch (IOException e) {
			MyLog.debug("Error reading mock data : " + e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {
					MyLog.warn("Error closing in stream", e);
				}
			}
		}

		MyLog.exit();
		return result;
	}

	public void loadMockCapture() {
		MyLog.entry();

		try {
			final ApiCallModel model = new ApiCallModel();
			model.setAction(ApiAction.GET_PLAYER_DATA);
			//model.setRequestParams(requestParams);
			//model.setRequestContent(requestContentString);
			model.setRegion(PADRegion.US);
			model.setResponseContent(extractResponseContent());
			new ApiCallHandlerThread(context, model, null).start();
		} catch (final IOException e) {
			MyLog.error("Error loading mock capture", e);
		}

		MyLog.exit();
	}

	private String extractResponseContent() throws IOException {
		MyLog.entry();

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
					MyLog.warn("Error closing in stream", e);
				}
			}
		}

		MyLog.exit();
		return result;
	}
}
