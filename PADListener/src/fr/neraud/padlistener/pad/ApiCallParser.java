
package fr.neraud.padlistener.pad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerInfoHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterHelper;

public class ApiCallParser extends Thread {

	private final Context context;
	private final ApiCallModel callModel;

	public ApiCallParser(Context context, ApiCallModel callModel) {
		this.context = context;
		this.callModel = callModel;
	}

	@Override
	public void run() {
		Log.d(ApiCallParser.class.getName(), "run");

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
				Log.d(ApiCallParser.class.getName(), "Ingoring action " + callModel.getAction());
			}
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static GetPlayerDataApiCallResult parsePlayerData(ApiCallModel callModel) throws JSONException {
		Log.d(ApiCallParser.class.getName(), "parsePlayerData");
		final JSONObject result = new JSONObject(callModel.getResponseContent());
		final GetPlayerDataApiCallResult model = new GetPlayerDataApiCallResult();
		final int res = result.getInt("res");
		model.setRes(res);

		if (model.isResOk()) {
			final CapturedPlayerInfoModel playerInfo = new CapturedPlayerInfoModel();
			// "friendMax": 30, "cardMax": 30, "name": "NeraudMule", "lv": 19, "exp": 29209, "cost": 32, "sta": 26, "sta_max": 26, "gold": 5, "coin": 63468, "curLvExp": 27188, "nextLvExp": 30954,
			playerInfo.setLastUpdate(new Date());
			playerInfo.setFriendMax(result.getInt("friendMax"));
			playerInfo.setCardMax(result.getInt("cardMax"));
			playerInfo.setName(result.getString("name"));
			playerInfo.setRank(result.getInt("lv"));
			playerInfo.setExp(result.getLong("exp"));
			playerInfo.setCostMax(result.getInt("cost"));
			playerInfo.setStamina(result.getInt("sta"));
			playerInfo.setStaminaMax(result.getInt("sta_max"));
			playerInfo.setStones(result.getInt("gold"));
			playerInfo.setCoins(result.getLong("coin"));
			playerInfo.setCurrentLevelExp(result.getLong("curLvExp"));
			playerInfo.setNextLevelExp(result.getLong("nextLvExp"));

			model.setPlayerInfo(playerInfo);

			// "card"

			final List<CapturedMonsterCardModel> monsters = new ArrayList<CapturedMonsterCardModel>();
			final JSONArray carsResults = result.getJSONArray("card");
			for (int i = 0; i < carsResults.length(); i++) {
				final JSONObject cardResult = (JSONObject) carsResults.get(i);
				//"cuid": 1, "exp": 15939, "lv": 16, "slv": 1, "mcnt": 11, "no": 3, "plus": [0, 0, 0, 0]
				final CapturedMonsterCardModel monster = new CapturedMonsterCardModel();
				monster.setExp(cardResult.getLong("exp"));
				monster.setLevel(cardResult.getInt("lv"));
				monster.setSkillLevel(cardResult.getInt("slv"));
				monster.setId(cardResult.getInt("no"));
				final JSONArray plusResults = cardResult.getJSONArray("plus");
				monster.setPlusHp(plusResults.getInt(0));
				monster.setPlusAtk(plusResults.getInt(1));
				monster.setPlusRcv(plusResults.getInt(2));
				monster.setAwakenings(plusResults.getInt(3));

				monsters.add(monster);
			}
			model.setMonsterCards(monsters);
		}

		return model;
	}

	private void savePlayerInfo(CapturedPlayerInfoModel playerInfoModel) {
		Log.d(ApiCallParser.class.getName(), "savePlayerData");

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
			Log.d(ApiCallParser.class.getName(), "savePlayerData : Insert new data");
			cr.insert(uri, values);
		} else {
			Log.d(ApiCallParser.class.getName(), "savePlayerData : Update existing data");
			cr.update(uri, values, CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " = ?",
			        new String[] { fake_id.toString() });
		}
	}

	private void saveMonsters(List<CapturedMonsterCardModel> monsters) {
		Log.d(ApiCallParser.class.getName(), "saveMonsters");

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
