package fr.neraud.padlistener.proxy.helper;

import android.content.Context;

/**
 * Created by Neraud on 29/11/2014.
 */
public interface WifiConfigHelper {

	public void modifyWifiProxySettings(Context context, String proxyHost, int proxyPost) throws Exception;

	public void unsetWifiProxySettings(Context context) throws Exception;
}
