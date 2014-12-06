package fr.neraud.padlistener.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.neraud.log.MyLog;

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
		MyLog.entry(assetFileName + " -> " + targetFilePath);

		final File targetFile = new File(targetFilePath);
		if (targetFile.exists()) {
			MyLog.debug("deleting old file");
			targetFile.delete();
		}

		doCopyAsset(assetFileName, targetFilePath, targetFile);

		MyLog.exit();
	}

	protected void doCopyAsset(String assetFileName, String targetFilePath, File targetFile) throws IOException {
		MyLog.entry();

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
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					MyLog.warn("error closing in stream");
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					MyLog.warn("error closing out stream");
				}
			}
		}
		MyLog.exit();
	}

	protected Context getContext() {
		return context;
	}

}
