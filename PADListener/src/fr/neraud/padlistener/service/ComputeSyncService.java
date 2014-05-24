
package fr.neraud.padlistener.service;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import fr.neraud.padlistener.helper.SyncHelper;
import fr.neraud.padlistener.http.client.RestClient;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.exception.ProcessException;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.http.model.RestRequest;
import fr.neraud.padlistener.http.parser.UserInfoJsonParser;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.SyncComputeResultModel;
import fr.neraud.padlistener.model.UserInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterHelper;
import fr.neraud.padlistener.provider.helper.MonsterInfoHelper;

public class ComputeSyncService extends AbstractRestIntentService<UserInfoModel, SyncComputeResultModel> {

	public static String SYNC_RESULT_BUNDLE_KEY = "sync_result";

	public ComputeSyncService() {
		super("ComputeSyncService");
	}

	@Override
	protected RestClient createRestClient() {
		return new RestClient(PadHerderDescriptor.serverUrl);
	}

	@Override
	protected RestRequest createRestRequest() {
		return PadHerderDescriptor.RequestHelper.initRequestForGetUserInfo(getApplicationContext());
	}

	@Override
	protected UserInfoModel parseResult(String responseContent) throws ParsingException {
		Log.d(getClass().getName(), "parseResult");
		final UserInfoJsonParser parser = new UserInfoJsonParser();
		final UserInfoModel userInfo = parser.parse(responseContent);
		return userInfo;
	}

	@Override
	protected SyncComputeResultModel processResult(UserInfoModel padInfo) throws ProcessException {
		Log.d(getClass().getName(), "processResult");
		final List<CapturedMonsterCardModel> capturedMonsters = extractCapturedPlayerMonster();
		final CapturedPlayerInfoModel capturedInfo = extractCapturedPlayerInfo();
		final List<MonsterInfoModel> monsterInfos = extractMonsterInfo();

		final SyncHelper helper = new SyncHelper(monsterInfos);
		final SyncComputeResultModel syncResult = helper.sync(capturedMonsters, capturedInfo, padInfo);

		return syncResult;
	}

	private List<CapturedMonsterCardModel> extractCapturedPlayerMonster() {
		Log.d(getClass().getName(), "extractCapturedPlayerMonster");
		final Uri uri = CapturedPlayerMonsterDescriptor.UriHelper.uriForAll();
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);

		final List<CapturedMonsterCardModel> capturedMonsters = new ArrayList<CapturedMonsterCardModel>();

		if (cursor.moveToFirst()) {
			do {
				final CapturedMonsterCardModel model = CapturedPlayerMonsterHelper.cursorToModel(cursor);
				capturedMonsters.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return capturedMonsters;
	}

	private CapturedPlayerInfoModel extractCapturedPlayerInfo() {
		Log.d(getClass().getName(), "extractCapturedPlayerInfo");
		final Uri uri = CapturedPlayerInfoDescriptor.UriHelper.uriForAll();
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		CapturedPlayerInfoModel model = null;
		if (cursor.moveToFirst()) {
			model = CapturedPlayerInfoHelper.cursorToModel(cursor);
		}
		cursor.close();
		return model;
	}

	private List<MonsterInfoModel> extractMonsterInfo() {
		Log.d(getClass().getName(), "extractMonsterInfo");
		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);

		final List<MonsterInfoModel> monsterInfos = new ArrayList<MonsterInfoModel>();

		if (cursor.moveToFirst()) {
			do {
				final MonsterInfoModel model = MonsterInfoHelper.cursorToModel(cursor);
				monsterInfos.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return monsterInfos;
	}
}
