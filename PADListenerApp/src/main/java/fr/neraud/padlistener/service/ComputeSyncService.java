package fr.neraud.padlistener.service;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.JsonCaptureHelper;
import fr.neraud.padlistener.helper.SyncHelper;
import fr.neraud.padlistener.http.client.RestClient;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.exception.ProcessException;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.http.parser.padherder.UserInfoJsonParser;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.ComputeSyncResultModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.model.UserInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterProviderHelper;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Service used to compute sync
 *
 * @author Neraud
 */
public class ComputeSyncService extends AbstractRestIntentService<ComputeSyncResultModel> {

	public static final String EXTRA_ACCOUNT_ID_NAME = "accountId";
	private int accountId;

	private class FetchUserInfoTask extends RestTask<UserInfoModel> {

		@Override
		protected RestClient createRestClient() {
			return new RestClient(getApplicationContext(), PadHerderDescriptor.serverUrl);
		}

		@Override
		protected MyHttpRequest createMyHttpRequest() {
			return PadHerderDescriptor.RequestHelper.initRequestForGetUserInfo(getApplicationContext(), accountId);
		}

		@Override
		protected UserInfoModel parseResult(String responseContent) throws ParsingException {
			MyLog.entry();

			final JsonCaptureHelper saveHelper = new JsonCaptureHelper(getApplicationContext());
			saveHelper.savePadHerderUserInfo(responseContent);

			final UserInfoJsonParser parser = new UserInfoJsonParser();
			final UserInfoModel result = parser.parse(responseContent);

			MyLog.exit();
			return result;
		}
	}

	public ComputeSyncService() {
		super("ComputeSyncService");
	}

	protected List<RestTask<?>> createRestTasks() {
		final List<RestTask<?>> tasks = new ArrayList<RestTask<?>>();
		tasks.add(new FetchUserInfoTask());
		return tasks;
	}

	@Override
	protected void initParams(Intent intent) {
		accountId = intent.getIntExtra(ComputeSyncService.EXTRA_ACCOUNT_ID_NAME, 0);
	}

	@Override
	protected ComputeSyncResultModel processResult(List results) throws ProcessException {
		MyLog.entry();

		final UserInfoModel padInfo = (UserInfoModel) results.get(0);

		final List<MonsterModel> capturedMonsters = extractCapturedPlayerMonster();
		final CapturedPlayerInfoModel capturedInfo = extractCapturedPlayerInfo();
		final List<MonsterInfoModel> monsterInfos = extractMonsterInfo();

		final SyncHelper helper = new SyncHelper(getApplicationContext(), monsterInfos);

		final ComputeSyncResultModel result = helper.sync(capturedMonsters, capturedInfo, padInfo);

		MyLog.exit();
		return result;
	}

	private List<MonsterModel> extractCapturedPlayerMonster() {
		MyLog.entry();

		final Uri uri = CapturedPlayerMonsterDescriptor.UriHelper.uriForAll();
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);

		final List<MonsterModel> capturedMonsters = new ArrayList<MonsterModel>();

		if (cursor.moveToFirst()) {
			do {
				final MonsterModel model = CapturedPlayerMonsterProviderHelper.cursorToModel(cursor);
				capturedMonsters.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();

		MyLog.exit();
		return capturedMonsters;
	}

	private CapturedPlayerInfoModel extractCapturedPlayerInfo() {
		MyLog.entry();

		final Uri uri = CapturedPlayerInfoDescriptor.UriHelper.uriForAll();
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		CapturedPlayerInfoModel model = null;
		if (cursor.moveToFirst()) {
			model = CapturedPlayerInfoProviderHelper.cursorToModel(cursor);
		}
		cursor.close();

		MyLog.exit();
		return model;
	}

	private List<MonsterInfoModel> extractMonsterInfo() {
		MyLog.entry();

		final Uri uri = MonsterInfoDescriptor.UriHelper.uriForAll();
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);

		final List<MonsterInfoModel> monsterInfos = new ArrayList<MonsterInfoModel>();

		if (cursor.moveToFirst()) {
			do {
				final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);
				monsterInfos.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();

		MyLog.exit();
		return monsterInfos;
	}
}
