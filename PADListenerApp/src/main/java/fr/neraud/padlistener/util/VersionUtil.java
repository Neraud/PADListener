package fr.neraud.padlistener.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Helper to get the current version
 *
 * @author Neraud
 */
public class VersionUtil {

	/**
	 * @return the current version
	 */
	public static String getVersion(Context context) {
		String version = null;
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (final NameNotFoundException e) {
			version = "Unknown";
		}
		return version;
	}

}
