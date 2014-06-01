
package fr.neraud.padlistener.proxy.plugin;

import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.pad.GetPlayerDataJsonParser;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterHelper;

/**
 * Thread used to handle processing a call from PAD to Gunho servers
 * 
 * @author Neraud
 */
public class ApiCallHandlerThread extends Thread {

	private final Context context;
	private final ApiCallModel callModel;

	public ApiCallHandlerThread(Context context, ApiCallModel callModel) {
		this.context = context;
		this.callModel = callModel;
	}

	@Override
	public void run() {
		Log.d(getClass().getName(), "run");

		try {
			switch (callModel.getAction()) {
			case GET_PLAYER_DATA:
				final GetPlayerDataApiCallResult result = parsePlayerData(callModel);
				savePlayerInfo(result.getPlayerInfo());
				saveMonsters(result.getMonsterCards());
				new TechnicalSharedPreferencesHelper(context).setLastCaptureDate(new Date());
				showToast(context.getString(R.string.toast_data_captured, result.getPlayerInfo().getName()));
				break;
			default:
				Log.d(getClass().getName(), "Ingoring action " + callModel.getAction());
			}
		} catch (final ParsingException e) {
			Log.e(getClass().getName(), "run : parsing error", e);
		}
	}

	private GetPlayerDataApiCallResult parsePlayerData(ApiCallModel callModel) throws ParsingException {
		Log.d(getClass().getName(), "parsePlayerData");
		final GetPlayerDataJsonParser parser = new GetPlayerDataJsonParser();
		return parser.parse(callModel.getResponseContent());
	}

	private void savePlayerInfo(CapturedPlayerInfoModel playerInfoModel) {
		Log.d(getClass().getName(), "savePlayerData");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerInfoDescriptor.UriHelper.uriForAll();

		Long fake_id = null;

		final Cursor cursor = cr.query(uri, new String[] { CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName() }, null, null,
		        null);
		if (cursor != null && cursor.moveToNext()) {
			fake_id = cursor.getLong(0);
		}
		final ContentValues values = CapturedPlayerInfoHelper.modelToValues(playerInfoModel);

		if (fake_id == null) {
			Log.d(getClass().getName(), "savePlayerData : Insert new data");
			cr.insert(uri, values);
		} else {
			Log.d(getClass().getName(), "savePlayerData : Update existing data");
			cr.update(uri, values, CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " = ?",
			        new String[] { fake_id.toString() });
		}
	}

	private void saveMonsters(List<CapturedMonsterCardModel> monsters) {
		Log.d(getClass().getName(), "saveMonsters");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = CapturedPlayerMonsterDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final CapturedMonsterCardModel monster : monsters) {
			values[i] = CapturedPlayerMonsterHelper.modelToValues(monster);
			i++;
		}
		cr.bulkInsert(uri, values);
	}

	private void showToast(final String toastMessage) {
		new Handler(context.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
			}
		});
	}
}
