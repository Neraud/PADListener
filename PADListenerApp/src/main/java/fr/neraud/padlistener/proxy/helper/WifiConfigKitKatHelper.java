package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import fr.neraud.log.MyLog;

/**
 * Helper to access hidden WiFi settings.
 *
 * @author Neraud
 * @see "http://stackoverflow.com/questions/12486441/how-can-i-set-proxysettings-and-proxyproperties-on-android-wi-fi-connection-usin"
 */
@SuppressWarnings("unchecked")
public class WifiConfigKitKatHelper extends AbstractWifiConfigHelper {

	public WifiConfigKitKatHelper(Context context) {
		super(context);
	}

	private static void setProxySettings(String assign, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, "proxySettings", assign);
	}

	public void modifyWifiProxySettings(String proxyHost, int proxyPost) throws Exception {
		MyLog.entry();

		//get the current wifi configuration
		final WifiConfiguration config = findCurrentWifiConfiguration();
		if (null == config) {
			throw new Exception("Current WIFI configuration not found");
		}

		//get the link properties from the wifi configuration
		final Object linkProperties = getField(config, "linkProperties");
		if (null == linkProperties) {
			throw new Exception("linkProperties not found");
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

		saveConfig(config);

		MyLog.exit();
	}

	public void unsetWifiProxySettings() throws Exception {
		MyLog.entry();

		final WifiConfiguration config = findCurrentWifiConfiguration();
		if (null == config) {
			throw new Exception("Current WIFI configuration not found");
		}

		//get the link properties from the wifi configuration
		final Object linkProperties = getField(config, "linkProperties");
		if (null == linkProperties) {
			throw new Exception("linkProperties not found");
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

		saveConfig(config);

		MyLog.exit();
	}
}
