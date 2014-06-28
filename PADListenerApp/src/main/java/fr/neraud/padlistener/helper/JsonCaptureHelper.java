package fr.neraud.padlistener.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Helper to save the JSON from PAD and PADherder to files
 * Created by Neraud on 26/06/2014.
 */
public class JsonCaptureHelper {

	private static final String PAD_CAPTURED_DATA_FILE_NAME = "capture.json";
	private static final String PADHERDER_USER_INFO_FILE_NAME = "padherder.json";
	private final Context context;

	public JsonCaptureHelper(Context context) {
		this.context = context;
	}

	/**
	 * Save the captured data in a file
	 *
	 * @param content the content of the captured data
	 */
	public void savePadCapturedData(String content) {
		saveData(PAD_CAPTURED_DATA_FILE_NAME, content);
	}

	/**
	 * Save PADherder account information in a fil
	 *
	 * @param content the content of PADherder information
	 */
	public void savePadHerderUserInfo(String content) {
		saveData(PADHERDER_USER_INFO_FILE_NAME, content);
	}

	/**
	 * @return true if a captured data file has been saved
	 */
	public boolean hasPadCapturedData() {
		return hasFilename(PAD_CAPTURED_DATA_FILE_NAME);
	}

	/**
	 * @return true if a PADherder information file has been saved
	 */
	public File getPadCapturedDataFile() {
		return getFile(PAD_CAPTURED_DATA_FILE_NAME);
	}

	@SuppressLint("WorldReadableFiles")
	private void saveData(String fileName, String content) {
		OutputStream out = null;
		try {
			// Written with MODE_WORLD_READABLE to be able to share the json files directly without having to copy them to ext storage
			out = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
			IOUtils.write(content, out);
		} catch (IOException e) {
			Log.w(getClass().getName(), "saveData : error saving file", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Log.w(getClass().getName(), "saveData : error closing out stream");
				}
			}
		}
	}

	private boolean hasFilename(String fileName) {
		return getFile(fileName).exists();
	}

	private File getFile(String fileName) {
		final String targetFilePath = context.getFilesDir().getPath() + "/" + fileName;
		return new File(targetFilePath);
	}

}
