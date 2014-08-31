package fr.neraud.padlistener.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

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

	public void copyExecutablesFromAssets() throws IOException {
		Log.d(getClass().getName(), "copyScriptsFromAssets");
		copyExecutableFromAssets(ExecutableAsset.IPTABLES);
	}

	private void copyExecutableFromAssets(ExecutableAsset executableAsset) throws IOException {
		copyAsset(executableAsset.getAssetName(), getContext().getFilesDir().getPath() + "/" + executableAsset.getTargetName());
	}
}
