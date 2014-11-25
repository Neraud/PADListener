package fr.neraud.padlistener.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.constant.PADVersion;

/**
 * Created by Neraud on 25/11/2014.
 */
public class InstalledPadVersionsHelper {

	private final Context mContext;

	public InstalledPadVersionsHelper(Context context) {
		mContext = context;
	}

	public List<PADVersion> getInstalledPadVersion() {
		Log.d(getClass().getName(), "getInstalledPadVersion");
		final List<PADVersion> padVersions = new ArrayList<PADVersion>();
		final PackageManager packageManager = mContext.getPackageManager();
		for (PADVersion version : PADVersion.values()) {
			if (version != null) {
				try {
					packageManager.getApplicationInfo(version.getApplicationPackage(), 0);
					padVersions.add(version);

					Log.v(getClass().getName(), "initElements : added " + version.name());
				} catch (PackageManager.NameNotFoundException e) {
					// this version is not installed, ignored
					Log.v(getClass().getName(), "initElements : ignored " + version.name());
				}
			}
		}
		return padVersions;
	}

	public boolean isPadRunning(PADVersion version) {
		Log.d(getClass().getName(), "isPadRunning");
		final ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		for (int i = 0; i < procInfos.size(); i++) {
			if (procInfos.get(i).processName.equals(version.getApplicationPackage())) {
				return true;
			}
		}
		return false;
	}

	public Drawable getPadIcon(PADVersion version) {
		try {
			return mContext.getPackageManager().getApplicationIcon(version.getApplicationPackage());
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}
}
