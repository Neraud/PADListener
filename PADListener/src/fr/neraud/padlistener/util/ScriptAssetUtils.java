
package fr.neraud.padlistener.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import fr.neraud.padlistener.constant.ScriptAsset;

public class ScriptAssetUtils {

	private static String TAG = ScriptAssetUtils.class.getName();
	private final Context context;

	public ScriptAssetUtils(Context context) {
		super();
		this.context = context;
	}

	public void copyScriptsFromAssets() {
		Log.d(TAG, "copyScriptsFromAssets");
		copyScriptFromAssets(ScriptAsset.ENABLE_IPTABLES);
		copyScriptFromAssets(ScriptAsset.DISABLE_IPTABLES);
	}

	private void copyScriptFromAssets(ScriptAsset scriptAsset) {
		Log.d(TAG, "copyScriptFromAssets : " + scriptAsset);

		final AssetManager assetManager = context.getAssets();
		final String targetFilePath = context.getFilesDir().getPath() + "/" + scriptAsset.getScriptName();

		Log.d(TAG, "copyScriptFromAssets : -> " + targetFilePath);

		final File targetFile = new File(targetFilePath);
		if (targetFile.exists()) {
			Log.d(TAG, "copyScriptFromAssets : deleting old file");
			targetFile.delete();
		}

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(scriptAsset.getScriptName());
			out = new FileOutputStream(targetFilePath);

			final byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			out.flush();
			targetFile.setExecutable(true);
		} catch (final IOException e) {
			Log.e(TAG, "copyScriptFromAssets : error : " + e.getMessage());
			// TODO throw
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
}
