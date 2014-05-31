
package fr.neraud.padlistener.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import fr.neraud.padlistener.constant.InstallAsset;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.padherder.MonsterInfoJsonParser;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoHelper;

public class InstallMonsterInfoService extends IntentService {

	public InstallMonsterInfoService() {
		super("InstallMonsterInfoService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");
		InputStream inputStream = null;
		Scanner scanner = null;
		try {
			inputStream = getAssets().open(InstallAsset.MONSTER_INFO.getFileName());
			scanner = new Scanner(inputStream).useDelimiter("\\A");
			final String stringResult = scanner.hasNext() ? scanner.next() : "";
			final List<MonsterInfoModel> monsters = parseData(stringResult);
			saveData(monsters);
			Log.d(getClass().getName(), "onHandleIntent : data saved");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
				}
			}
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	private List<MonsterInfoModel> parseData(String responseContent) throws ParsingException {
		Log.d(getClass().getName(), "parseData");
		final MonsterInfoJsonParser parser = new MonsterInfoJsonParser();
		final List<MonsterInfoModel> monsters = parser.parse(responseContent);
		return monsters;
	}

	private void saveData(List<MonsterInfoModel> monsters) {
		Log.d(getClass().getName(), "saveData");
		final ContentResolver cr = getContentResolver();
		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final MonsterInfoModel monster : monsters) {
			values[i] = MonsterInfoHelper.modelToValues(monster);
			i++;
		}
		cr.bulkInsert(uri, values);
	}

}
