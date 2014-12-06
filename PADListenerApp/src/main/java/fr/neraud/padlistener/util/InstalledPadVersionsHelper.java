package fr.neraud.padlistener.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
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
		MyLog.entry();

		final List<PADVersion> padVersions = new ArrayList<PADVersion>();
		final PackageManager packageManager = mContext.getPackageManager();
		for (PADVersion version : PADVersion.values()) {
			if (version != null) {
				try {
					packageManager.getApplicationInfo(version.getApplicationPackage(), 0);
					padVersions.add(version);

					MyLog.debug("added " + version.name());
				} catch (PackageManager.NameNotFoundException e) {
					// this version is not installed, ignored
					MyLog.debug("ignored " + version.name());
				}
			}
		}

		MyLog.exit();
		return padVersions;
	}

	public boolean isPadRunning(PADVersion version) {
		MyLog.entry();

		final ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		boolean running = false;
		for (int i = 0; i < procInfos.size(); i++) {
			if (procInfos.get(i).processName.equals(version.getApplicationPackage())) {
				running = true;
				break;
			}
		}

		MyLog.exit();
		return running;
	}

	public Drawable getPadIcon(PADVersion version) {
		try {
			return mContext.getPackageManager().getApplicationIcon(version.getApplicationPackage());
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}
}
