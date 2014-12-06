package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;

import java.lang.reflect.Constructor;

import fr.neraud.log.MyLog;

/**
 * Helper to access hidden WiFi settings.
 *
 * @author Neraud
 */
@SuppressWarnings("unchecked")
public class WifiConfigLollipopHelper extends AbstractWifiConfigHelper {

	public WifiConfigLollipopHelper(Context context) {
		super(context);
	}

	private void doSetProxy(String proxyMode, Object proxyInfo) throws Exception {
		MyLog.entry();

		final WifiConfiguration config = findCurrentWifiConfiguration();
		if (null == config) {
			throw new Exception("Current WIFI configuration not found");
		}

		// @see android.net.IpConfiguration
		final Object ipConfiguration = getDeclaredField(config, "mIpConfiguration");
		if (null == ipConfiguration) {
			throw new Exception("ipConfiguration not found");
		}

		setEnumField(ipConfiguration, "proxySettings", proxyMode);
		setField(ipConfiguration, "httpProxy", proxyInfo);

		saveConfig(config);

		MyLog.exit();
	}

	public void modifyWifiProxySettings(String proxyHost, int proxyPort) throws Exception {
		MyLog.entry();

		//get ProxyInfo constructor
		final Class proxyInfoClass = Class.forName("android.net.ProxyInfo");
		final Class[] proxyInfoCtorParamTypes = new Class[]{String.class, int.class, String.class};
		final Constructor proxyInfoCtor = proxyInfoClass.getConstructor(proxyInfoCtorParamTypes);

		//create the parameters for the constructor
		final Object[] proxyInfoCtorParams = new Object[]{proxyHost, proxyPort, null};

		//create a new object using the params
		final Object proxyInfo = proxyInfoCtor.newInstance(proxyInfoCtorParams);

		doSetProxy("STATIC", proxyInfo);

		MyLog.exit();
	}

	public void unsetWifiProxySettings() throws Exception {
		MyLog.entry();
		doSetProxy("NONE", null);
		MyLog.exit();
	}
}
