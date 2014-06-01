
package fr.neraud.padlistener.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

/**
 * Helper to copy the assets
 * 
 * @author Neraud
 */
public abstract class AbstractAssetHelper {

	private final Context context;

	public AbstractAssetHelper(Context context) {
		super();
		this.context = context;
	}

	protected void copyAsset(final String assetFileName, String targetFilePath) throws IOException {
		Log.d(getClass().getName(), "copyAsset : " + assetFileName + " -> " + targetFilePath);

		final File targetFile = new File(targetFilePath);
		if (targetFile.exists()) {
			Log.d(getClass().getName(), "copyAsset : deleting old file");
			targetFile.delete();
		}

		InputStream in = null;
		OutputStream out = null;
		try {
			in = context.getAssets().open(assetFileName);
			out = new FileOutputStream(targetFilePath);

			final byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			out.flush();
			targetFile.setExecutable(true);
		} catch (final IOException e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (final IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (final IOException e) {
				}
			}
		}
	}

	protected Context getContext() {
		return context;
	}

}
