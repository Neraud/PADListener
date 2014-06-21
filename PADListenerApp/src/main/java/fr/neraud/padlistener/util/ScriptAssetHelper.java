
package fr.neraud.padlistener.util;

import java.io.IOException;

import android.content.Context;
import android.util.Log;
import fr.neraud.padlistener.constant.ScriptAsset;

/**
 * Helper to copy the scripts bundled in the assets
 * 
 * @author Neraud
 */
public class ScriptAssetHelper extends AbstractAssetHelper {

	public ScriptAssetHelper(Context context) {
		super(context);
	}

	public void copyScriptsFromAssets() throws IOException {
		Log.d(getClass().getName(), "copyScriptsFromAssets");
		copyScriptFromAssets(ScriptAsset.ENABLE_IPTABLES);
		copyScriptFromAssets(ScriptAsset.DISABLE_IPTABLES);
	}

	private void copyScriptFromAssets(ScriptAsset scriptAsset) throws IOException {
		copyAsset(scriptAsset.getScriptName(), getContext().getFilesDir().getPath() + "/" + scriptAsset.getScriptName());
	}
}
