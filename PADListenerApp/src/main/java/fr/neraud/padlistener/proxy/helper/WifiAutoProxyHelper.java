package fr.neraud.padlistener.proxy.helper;

import android.content.Context;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.exception.MissingRequirementException;

/**
 * Helper to change the WiFi settings to enable the AutoWifi mode for the listener
 *
 * @author Neraud
 */
public class WifiAutoProxyHelper {

	private final Context mContext;

	public WifiAutoProxyHelper(Context context) {
		this.mContext = context;
	}

	private WifiConfigHelper getWifiConfigHelper() throws MissingRequirementException {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
			return new WifiConfigKitKatHelper(mContext);
		} else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
			return new WifiConfigLollipopHelper(mContext);
		}

		throw new MissingRequirementException(MissingRequirementException.Requirement.ANDROID_VERSION);
	}

	public void activateAutoProxy() throws Exception {
		MyLog.entry();

		final WifiConfigHelper helper = getWifiConfigHelper();
		try {
			helper.modifyWifiProxySettings("127.0.0.1", 8008);
		} catch (final Exception e) {
			throw new Exception("Error activating auto proxy", e);
		}

		MyLog.exit();
	}

	public void deactivateAutoProxy() throws Exception {
		MyLog.entry();

		final WifiConfigHelper helper = getWifiConfigHelper();
		try {
			helper.unsetWifiProxySettings();
		} catch (final Exception e) {
			throw new Exception("Error deactivating auto proxy", e);
		}

		MyLog.exit();
	}

}
