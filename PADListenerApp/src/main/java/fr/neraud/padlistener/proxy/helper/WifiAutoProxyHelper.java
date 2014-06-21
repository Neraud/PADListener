package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.util.Log;

/**
 * Helper to change the WiFi settings to enable the AutoWifi mode for the listener
 *
 * @author Neraud
 */
public class WifiAutoProxyHelper {

	private final Context context;

	public WifiAutoProxyHelper(Context context) {
		this.context = context;
	}

	public void activateAutoProxy() throws Exception {
		Log.d(getClass().getName(), "activateAutoProxy");

		try {
			final WifiConfigHelper helper = new WifiConfigHelper();
			helper.modifyWifiProxySettings(context, "localhost", 8008);
		} catch (final Exception e) {
			throw new Exception("Error activating auto proxy", e);
		}
	}

	public void deactivateAutoProxy() throws Exception {
		Log.d(getClass().getName(), "deactivateAutoProxy");
		try {
			final WifiConfigHelper helper = new WifiConfigHelper();
			helper.unsetWifiProxySettings(context);
		} catch (final Exception e) {
			throw new Exception("Error deactivating auto proxy", e);
		}
	}

}
