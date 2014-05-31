
package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.util.Log;

public class WifiAutoProxyHelper {

	private final Context context;

	public WifiAutoProxyHelper(Context context) {
		this.context = context;
	}

	public void activateAutoProxy() {
		Log.d(getClass().getName(), "activateAutoProxy");

	}

	public void deactivateAutoProxy() {
		Log.d(getClass().getName(), "deactivateAutoProxy");

	}
}
