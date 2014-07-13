package fr.neraud.padlistener.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import fr.neraud.padlistener.constant.ExecutableAsset;

/**
 * Helper to copy the executables bundled in the assets
 *
 * @author Neraud
 */
public class ExecutableAssetHelper extends AbstractAssetHelper {

	public ExecutableAssetHelper(Context context) {
		super(context);
	}

	public void copyExecutablessFromAssets() throws IOException {
		Log.d(getClass().getName(), "copyScriptsFromAssets");
		copyExecutableFromAssets(ExecutableAsset.IPTABLES);
	}

	private void copyExecutableFromAssets(ExecutableAsset executableAsset) throws IOException {
		copyAsset(executableAsset.getAssetName(), getContext().getFilesDir().getPath() + "/" + executableAsset.getTargetName());
	}
}
