package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.util.List;

import fr.neraud.log.MyLog;

/**
 * Helper to access hidden WiFi settings.
 *
 * @author Neraud
 * @see "http://stackoverflow.com/questions/12486441/how-can-i-set-proxysettings-and-proxyproperties-on-android-wi-fi-connection-usin"
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public abstract class AbstractWifiConfigHelper implements WifiConfigHelper {

	private final WifiManager mWifiManager;

	protected AbstractWifiConfigHelper(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	protected static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getDeclaredField(name);
		return f.get(obj);
	}

	protected static void setField(Object obj, String name, Object value) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getField(name);
		f.set(obj, value);
	}

	protected static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		return f.get(obj);
	}

	protected static void setEnumField(Object obj, String name, String value) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}

	protected WifiConfiguration findCurrentWifiConfiguration() {
		MyLog.entry();

		WifiConfiguration configuration = null;

		if (mWifiManager.isWifiEnabled()) {
			final List<WifiConfiguration> configurationList = mWifiManager.getConfiguredNetworks();
			final int cur = mWifiManager.getConnectionInfo().getNetworkId();
			for (final WifiConfiguration wifiConfiguration : configurationList) {
				if (wifiConfiguration.networkId == cur) {
					configuration = wifiConfiguration;
					break;
				}
			}
		}

		MyLog.exit();
		return configuration;
	}

	protected void saveConfig(WifiConfiguration config) {
		MyLog.entry();

		mWifiManager.updateNetwork(config);
		mWifiManager.saveConfiguration();
		mWifiManager.disconnect();
		mWifiManager.reconnect();

		MyLog.exit();
	}
}
