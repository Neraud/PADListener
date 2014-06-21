package fr.neraud.padlistener.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.neraud.padlistener.constant.InstallAsset;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.util.MonsterImageAssetHelper;

/**
 * IntentService used in the installation phase to copy monster images bundled in the assets
 *
 * @author Neraud
 */
public class InstallMonsterImagesService extends IntentService {

	public InstallMonsterImagesService() {
		super("InstallMonsterInfoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");
		final MonsterImageAssetHelper helper = new MonsterImageAssetHelper(getApplicationContext());
		try {
			helper.copyMonsterImagesFromAssets();
			saveDate();
			Log.d(getClass().getName(), "onHandleIntent : images copied");
		} catch (final IOException e) {
			Log.e(getClass().getName(), "onHandleIntent : error installing monster images", e);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void saveDate() {
		Log.d(getClass().getName(), "saveDate");
		InputStream inputStreamDate = null;
		try {
			inputStreamDate = getAssets().open(InstallAsset.MONSTER_IMAGES_DATE.getFileName());
			final List<String> dateLines = IOUtils.readLines(inputStreamDate);
			final String dateLine = dateLines.get(0);
			final Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateLine);
			Log.d(getClass().getName(), "saveDate : dataDate = " + dataDate);
			new TechnicalSharedPreferencesHelper(getApplicationContext()).setMonsterImagesRefreshDate(dataDate);
		} catch (final Exception e) {
			Log.e(getClass().getName(), "saveDate : error extraction date", e);
		} finally {
			if (inputStreamDate != null) {
				try {
					inputStreamDate.close();
				} catch (final IOException e) {
				}
			}
		}
	}
}
