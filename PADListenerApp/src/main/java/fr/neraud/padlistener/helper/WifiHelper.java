package fr.neraud.padlistener.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import fr.neraud.log.MyLog;

/**
 * Helper to access Wifi properties
 *
 * Created by Neraud on 28/06/2014.
 */
public class WifiHelper {

	private final Context context;

	public WifiHelper(Context context) {
		this.context = context;
	}

	/**
	 * @return true if Wifi is connected
	 */
	public boolean isWifiConnected() {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return (wifiNetwork != null && wifiNetwork.isConnected());
	}

	/**
	 * @return the Wifi IP address
	 * @see "http://stackoverflow.com/questions/16730711/get-my-wifi-ip-address-android"
	 */
	public String getWifiIpAddress() {
		final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

		// Convert little-endian to big-endianif needed
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
			ipAddress = Integer.reverseBytes(ipAddress);
		}

		final byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

		String ipAddressString;
		try {
			ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
		} catch (UnknownHostException ex) {
			MyLog.warn("Unable to get host address.", ex);
			ipAddressString = null;
		}

		return ipAddressString;
	}
}
