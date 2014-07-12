package fr.neraud.padlistener.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

		doCopyAsset(assetFileName, targetFilePath, targetFile);
	}

	protected void doCopyAsset(String assetFileName, String targetFilePath, File targetFile) throws IOException {
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
				} catch (final IOException e) {
					Log.w(getClass().getName(), "doCopyAsset : error closing in stream");
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "doCopyAsset : error closing out stream");
				}
			}
		}
	}

	protected Context getContext() {
		return context;
	}

}
