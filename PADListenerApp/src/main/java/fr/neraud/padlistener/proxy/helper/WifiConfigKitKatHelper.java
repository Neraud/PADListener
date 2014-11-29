package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Helper to access hidden WiFi settings.
 *
 * @author Neraud
 * @see "http://stackoverflow.com/questions/12486441/how-can-i-set-proxysettings-and-proxyproperties-on-android-wi-fi-connection-usin"
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class WifiConfigKitKatHelper extends AbstractWifiConfigHelper {

	private static void setProxySettings(String assign, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, assign, "proxySettings");
	}

	private WifiConfiguration findCurrentWifiConfiguration(WifiManager manager) {
		if (!manager.isWifiEnabled()) {
			return null;
		}

		final List<WifiConfiguration> configurationList = manager.getConfiguredNetworks();
		WifiConfiguration configuration = null;
		final int cur = manager.getConnectionInfo().getNetworkId();
		for (final WifiConfiguration wifiConfiguration : configurationList) {
			if (wifiConfiguration.networkId == cur) {
				configuration = wifiConfiguration;
				break;
			}
		}

		return configuration;
	}

	public void modifyWifiProxySettings(Context context, String proxyHost, int proxyPost) throws Exception {
		Log.d(getClass().getName(), "modifyWifiProxySettings");
		//get the current wifi configuration
		final WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		final WifiConfiguration config = findCurrentWifiConfiguration(manager);
		if (null == config) {
			return;
		}

		//get the link properties from the wifi configuration
		final Object linkProperties = getField(config, "linkProperties");
		if (null == linkProperties) {
			return;
		}

		//get the setHttpProxy method for LinkProperties
		final Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
		final Class[] setHttpProxyParams = new Class[]{proxyPropertiesClass};
		final Class lpClass = Class.forName("android.net.LinkProperties");
		final Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
		setHttpProxy.setAccessible(true);

		//get ProxyProperties constructor
		final Class[] proxyPropertiesCtorParamTypes = new Class[]{String.class, int.class, String.class};
		final Constructor proxyPropertiesCtor = proxyPropertiesClass.getConstructor(proxyPropertiesCtorParamTypes);

		//create the parameters for the constructor
		final Object[] proxyPropertiesCtorParams = new Object[]{proxyHost, proxyPost, null};

		//create a new object using the params
		final Object proxySettings = proxyPropertiesCtor.newInstance(proxyPropertiesCtorParams);

		//pass the new object to setHttpProxy
		final Object[] params = new Object[]{proxySettings};
		setHttpProxy.invoke(linkProperties, params);

		setProxySettings("STATIC", config);

		//save the settings
		manager.updateNetwork(config);
		manager.saveConfiguration();
		manager.disconnect();
		manager.reconnect();
	}

	public void unsetWifiProxySettings(Context context) throws Exception {
		Log.d(getClass().getName(), "unsetWifiProxySettings");
		final WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		final WifiConfiguration config = findCurrentWifiConfiguration(manager);
		if (null == config) {
			return;
		}

		//get the link properties from the wifi configuration
		final Object linkProperties = getField(config, "linkProperties");
		if (null == linkProperties) {
			return;
		}

		//get the setHttpProxy method for LinkProperties
		final Class proxyPropertiesClass = Class.forName("android.net.ProxyProperties");
		final Class[] setHttpProxyParams = new Class[]{proxyPropertiesClass};
		final Class lpClass = Class.forName("android.net.LinkProperties");
		final Method setHttpProxy = lpClass.getDeclaredMethod("setHttpProxy", setHttpProxyParams);
		setHttpProxy.setAccessible(true);

		//pass null as the proxy
		final Object[] params = new Object[]{null};
		setHttpProxy.invoke(linkProperties, params);

		setProxySettings("NONE", config);

		//save the config
		manager.updateNetwork(config);
		manager.saveConfiguration();
		manager.disconnect();
		manager.reconnect();
	}
}