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
import java.util.Map;
import java.util.Scanner;

import fr.neraud.padlistener.constant.InstallAsset;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.padherder.MonsterEvolutionJsonParser;
import fr.neraud.padlistener.http.parser.padherder.MonsterInfoJsonParser;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.service.helper.UpdateMonsterInfoHelper;

/**
 * IntentService used in the installation phase to load monster information bundled in the assets
 *
 * @author Neraud
 */
public class InstallMonsterInfoService extends IntentService {

	public InstallMonsterInfoService() {
		super("InstallMonsterInfoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");

		try {
			final List<MonsterInfoModel> monsters = extractMonsterInfo();
			final Map<Integer, Integer> evolutions = extractMonsterEvolutions();

			final UpdateMonsterInfoHelper updateHelper = new UpdateMonsterInfoHelper(getApplicationContext());
			updateHelper.mergeAndSaveMonsterInfo(monsters, evolutions);

			saveDate();

			Log.d(getClass().getName(), "onHandleIntent : data saved");
		} catch (final Exception e) {
			Log.e(getClass().getName(), "onHandleIntent : error installing monster info", e);
		}
	}

	private List<MonsterInfoModel> extractMonsterInfo() throws IOException, ParsingException {
		Log.d(getClass().getName(), "extractMonsterInfo");
		InputStream inputStream = null;
		Scanner scanner = null;
		try {
			inputStream = getAssets().open(InstallAsset.MONSTER_INFO.getFileName());
			scanner = new Scanner(inputStream).useDelimiter("\\A");
			final String stringResult = scanner.hasNext() ? scanner.next() : "";
			final MonsterInfoJsonParser parser = new MonsterInfoJsonParser();
			return parser.parse(stringResult);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "onHandleIntent : error closing in stream");
				}
			}
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	private Map<Integer, Integer> extractMonsterEvolutions() throws IOException, ParsingException {
		Log.d(getClass().getName(), "extractMonsterEvolutions");
		InputStream inputStream = null;
		Scanner scanner = null;
		try {
			inputStream = getAssets().open(InstallAsset.MONSTER_EVOLUTIONS.getFileName());
			scanner = new Scanner(inputStream).useDelimiter("\\A");
			final String stringResult = scanner.hasNext() ? scanner.next() : "";
			final MonsterEvolutionJsonParser parser = new MonsterEvolutionJsonParser();
			return parser.parse(stringResult);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "extractMonsterEvolutions : error closing in stream");
				}
			}
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void saveDate() {
		Log.d(getClass().getName(), "saveDate");
		InputStream inputStreamDate = null;
		try {
			inputStreamDate = getAssets().open(InstallAsset.MONSTER_INFO_DATE.getFileName());
			final List<String> dateLines = IOUtils.readLines(inputStreamDate);
			final String dateLine = dateLines.get(0);
			final Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateLine);
			Log.d(getClass().getName(), "saveDate : dataDate = " + dataDate);
			new TechnicalSharedPreferencesHelper(getApplicationContext()).setMonsterInfoRefreshDate(dataDate);
		} catch (final Exception e) {
			Log.e(getClass().getName(), "saveDate : error extraction date", e);
		} finally {
			if (inputStreamDate != null) {
				try {
					inputStreamDate.close();
				} catch (final IOException e) {
					Log.w(getClass().getName(), "saveDate : error closing in stream");
				}
			}
		}
	}

}
