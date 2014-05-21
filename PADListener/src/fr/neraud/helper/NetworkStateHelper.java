
package fr.neraud.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Helper to access the networj states
 * 
 * @author Neraud
 */
public class NetworkStateHelper {

	/**
	 * @param context the Context
	 * @return true if the device is connected to internet
	 */
	public static boolean isConnected(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		final boolean connected = (wifiNetworkInfo != null && wifiNetworkInfo.isConnectedOrConnecting())
		        || (dataNetworkInfo != null && dataNetworkInfo.isConnectedOrConnecting());

		return connected;
	}

	/**
	 * @param context the Context
	 * @return true if the device is connected over WIFI
	 */
	public static boolean isConnectedOverWifi(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final boolean connected = networkInfo != null && networkInfo.isConnectedOrConnecting();

		return connected;
	}
}
