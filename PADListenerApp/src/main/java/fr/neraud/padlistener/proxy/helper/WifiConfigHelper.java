package fr.neraud.padlistener.proxy.helper;

/**
 * Created by Neraud on 29/11/2014.
 */
public interface WifiConfigHelper {

	public void modifyWifiProxySettings(String proxyHost, int proxyPost) throws Exception;

	public void unsetWifiProxySettings() throws Exception;
}
