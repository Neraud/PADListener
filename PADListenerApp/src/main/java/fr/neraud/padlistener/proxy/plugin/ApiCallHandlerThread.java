package fr.neraud.padlistener.proxy.plugin;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Date;
import java.util.List;

import fr.neraud.padlistener.helper.CaptureNotificationHelper;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.JsonCaptureHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.pad.GetPlayerDataJsonParser;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterProviderHelper;
import fr.neraud.padlistener.service.ListenerService;

/**
 * Thread used to handle processing a call from PAD to Gungho servers
 *
 * @author Neraud
 */
public class ApiCallHandlerThread extends Thread {

	private final Context context;
	private final ApiCallModel callModel;
	private boolean forceAutoStopListenerAfterCapture = false;
	private final CaptureNotificationHelper captureCallback;


	public ApiCallHandlerThread(Context context, ApiCallModel callModel) {
		this.context = context;
		this.callModel = callModel;
		this.captureCallback = new CaptureNotificationHelper(context);
	}

	@Override
	public void run() {
		Log.d(getClass().getName(), "run");

		try {
			switch (callModel.getAction()) {
				case GET_PLAYER_DATA:
					captureCallback.notifyCaptureStarted();

					final GetPlayerDataApiCallResult result = parsePlayerData(callModel);
					savePlayerInfo(result.getPlayerInfo());
					saveMonsters(result.getMonsterCards());
					saveFriends(result.getFriends());

					final JsonCaptureHelper saveHelper = new JsonCaptureHelper(context);
					saveHelper.savePadCapturedData(callModel.getResponseContent());

					final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(context);
					techPrefHelper.setLastCaptureDate(new Date());
					techPrefHelper.setLastCaptureName(result.getPlayerInfo().getName());
					captureCallback.notifyCaptureFinished(result.getPlayerInfo().getName());

					final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(context);
					if ((forceAutoStopListenerAfterCapture || prefHelper.isListenerAutoShutdown()) && techPrefHelper.getLastListenerStartProxyMode().isAutomatic()) {
						stopListener();
					}
					break;
				default:
					Log.d(getClass().getName(), "Ignoring action " + callModel.getAction());
			}
		} catch (final ParsingException e) {
			Log.e(getClass().getName(), "run : parsing error", e);
		}
	}

	private GetPlayerDataApiCallResult parsePlayerData(ApiCallModel callModel) throws ParsingException {
		Log.d(getClass().getName(), "parsePlayerData");
		final GetPlayerDataJsonParser parser = new GetPlayerDataJsonParser(context, callModel.getRegion());
		return parser.parse(callModel.getResponseContent());
	}

	private void savePlayerInfo(CapturedPlayerInfoModel playerInfoModel) {
		Log.d(getClass().getName(), "savePlayerData");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerInfoDescriptor.UriHelper.uriForAll();

		Long fake_id = null;

		final Cursor cursor = cr.query(uri, new String[]{CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName()}, null, null,
				null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				fake_id = cursor.getLong(0);
			}
			cursor.close();
		}

		final ContentValues values = CapturedPlayerInfoProviderHelper.modelToValues(playerInfoModel);

		if (fake_id == null) {
			Log.d(getClass().getName(), "savePlayerData : Insert new data");
			cr.insert(uri, values);
		} else {
			Log.d(getClass().getName(), "savePlayerData : Update existing data");
			cr.update(uri, values, CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " = ?",
					new String[]{fake_id.toString()});
		}
	}

	private void saveMonsters(List<MonsterModel> monsters) {
		Log.d(getClass().getName(), "saveMonsters");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerMonsterDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final MonsterModel monster : monsters) {
			values[i] = CapturedPlayerMonsterProviderHelper.modelToValues(monster);
			i++;
		}
		cr.bulkInsert(uri, values);
	}

	private void saveFriends(List<CapturedFriendModel> friends) {
		Log.d(getClass().getName(), "saveFriends");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerFriendDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[friends.size()];
		int i = 0;
		for (final CapturedFriendModel friend : friends) {
			values[i] = CapturedPlayerFriendProviderHelper.modelToValues(friend);
			i++;
		}
		cr.bulkInsert(uri, values);
	}

	private void stopListener() {
		Log.d(getClass().getName(), "stopListener");
		final Intent serviceIntent = new Intent(context, ListenerService.class);
		context.stopService(serviceIntent);
	}

	public void setForceAutoStopListenerAfterCapture(boolean forceAutoStopListenerAfterCapture) {
		this.forceAutoStopListenerAfterCapture = forceAutoStopListenerAfterCapture;
	}
}
