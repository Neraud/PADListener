package fr.neraud.padlistener.helper;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.neraud.padlistener.constant.InstallAsset;

/**
 * Helper to manage the installation of monster information and images
 * Created by Neraud on 26/06/2014.
 */
public class InstallationHelper {

	private final Context context;

	public InstallationHelper(Context context) {
		this.context = context;
	}

	/**
	 * @return true if the app needs to install the bundled data
	 */
	public boolean needsInstall() {
		Log.d(getClass().getName(), "needsInstall");
		final TechnicalSharedPreferencesHelper prefHelper = new TechnicalSharedPreferencesHelper(context);
		if (prefHelper.isHasBeenInstalled()) {
			return needsInstallForOneAsset(InstallAsset.MONSTER_INFO_DATE, prefHelper.getMonsterInfoRefreshDate())
					|| needsInstallForOneAsset(InstallAsset.MONSTER_IMAGES_DATE, prefHelper.getMonsterImagesRefreshDate());
		} else {
			Log.d(getClass().getName(), "needsInstall : not installed");
			return true;
		}

	}

	private boolean needsInstallForOneAsset(InstallAsset asset, Date lastRefreshDate) {
		if (lastRefreshDate == null) return true;

		final Date assetDate = extractBundledMonsterDataDate(asset);
		boolean needsInstallForAsset = assetDate != null && assetDate.after(lastRefreshDate);
		Log.d(getClass().getName(), "needsInstallForOneAsset : comparing " + assetDate + " with " + lastRefreshDate + " -> " + needsInstallForAsset);
		return needsInstallForAsset;
	}

	private Date extractBundledMonsterDataDate(InstallAsset asset) {
		InputStream inputStreamDate = null;
		Date dataDate = null;
		try {
			inputStreamDate = context.getAssets().open(asset.getFileName());
			final List<String> dateLines = IOUtils.readLines(inputStreamDate);
			final String dateLine = dateLines.get(0);
			dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateLine);
			Log.d(getClass().getName(), "extractBundledMonsterDataDate : dataDate = " + dataDate);
			return dataDate;
		} catch (final Exception e) {
			Log.e(getClass().getName(), "extractBundledMonsterDataDate : error extraction date", e);
		} finally {
			if (inputStreamDate != null) {
				try {
					inputStreamDate.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "extractBundledMonsterDataDate : error closing in stream");
				}
			}
		}
		return dataDate;
	}
}
