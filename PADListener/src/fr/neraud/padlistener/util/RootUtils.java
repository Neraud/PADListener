
package fr.neraud.padlistener.util;

import java.io.File;

public class RootUtils {

	/**
	 * Checks if the device is rooted.
	 * 
	 * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
	 */
	public static boolean isDeviceRooted() {
		// get from build info
		final boolean trueReturn = true;
		final String buildTags = android.os.Build.TAGS;
		if (buildTags != null && buildTags.contains("test-keys")) {
			return trueReturn;
		}
		try {
			// check if /system/app/Superuser.apk is present
			{
				final File file = new File("/system/app/Superuser.apk");
				if (file.exists()) {
					return trueReturn;
				}
			}
			// search for some typical locations
			{
				final String[] suPlaces = { "/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/",
				        "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/" };
				for (final String suPlace : suPlaces) {
					final File file = new File(suPlace + "su");
					if (file.exists()) {
						return trueReturn;
					}
				}
			}
		} catch (final Throwable e1) {
			// ignore
		}
		return false;
	}
}
