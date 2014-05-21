
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
import android.util.Log;
import fr.neraud.padlistener.model.MonsterCardModel;
import fr.neraud.padlistener.model.PlayerInfoModel;
import fr.neraud.padlistener.pad.model.ApiCallModel;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;
import fr.neraud.padlistener.provider.descriptor.PlayerInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.PlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.helper.PlayerInfoHelper;
import fr.neraud.padlistener.provider.helper.PlayerMonsterHelper;

public class ApiCallParser extends Thread {

	private static String TAG = ApiCallParser.class.getName();
	private final Context context;
	private final ApiCallModel callModel;

	public ApiCallParser(Context context, ApiCallModel callModel) {
		this.context = context;
		this.callModel = callModel;
	}

	@Override
	public void run() {
		Log.d(TAG, "run");

		try {
			switch (callModel.getAction()) {
			case GET_PLAYER_DATA:
				final GetPlayerDataApiCallResult result = parsePlayerData(callModel);
				savePlayerInfo(result.getPlayerInfo());
				saveMonsters(result.getMonsterCards());
				break;
			default:
				Log.d(TAG, "Ingoring action " + callModel.getAction());
			}
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static GetPlayerDataApiCallResult parsePlayerData(ApiCallModel callModel) throws JSONException {
		Log.d(TAG, "parsePlayerData");
		final JSONObject result = new JSONObject(callModel.getResponseContent());
		final GetPlayerDataApiCallResult model = new GetPlayerDataApiCallResult();
		final int res = result.getInt("res");
		model.setRes(res);

		if (model.isResOk()) {
			final PlayerInfoModel playerInfo = new PlayerInfoModel();
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

			final List<MonsterCardModel> monsters = new ArrayList<MonsterCardModel>();
			final JSONArray carsResults = result.getJSONArray("card");
			for (int i = 0; i < carsResults.length(); i++) {
				final JSONObject cardResult = (JSONObject) carsResults.get(i);
				//"cuid": 1, "exp": 15939, "lv": 16, "slv": 1, "mcnt": 11, "no": 3, "plus": [0, 0, 0, 0]
				final MonsterCardModel monster = new MonsterCardModel();
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

	private void savePlayerInfo(PlayerInfoModel playerInfoModel) {
		Log.d(TAG, "savePlayerData");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = PlayerInfoDescriptor.UriHelper.uriForAll();

		Long fake_id = null;

		final Cursor cursor = cr.query(uri, new String[] { PlayerInfoDescriptor.Fields.FAKE_ID.getColName() }, null, null, null);
		if (cursor != null && cursor.moveToNext()) {
			fake_id = cursor.getLong(0);
		}
		final ContentValues values = PlayerInfoHelper.modelToValues(playerInfoModel);

		if (fake_id == null) {
			Log.d(TAG, "savePlayerData : Insert new data");
			cr.insert(uri, values);
		} else {
			Log.d(TAG, "savePlayerData : Update existing data");
			cr.update(uri, values, PlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " = ?", new String[] { fake_id.toString() });
		}
	}

	private void saveMonsters(List<MonsterCardModel> monsters) {
		Log.d(TAG, "saveMonsters");

		final ContentResolver cr = context.getContentResolver();
		final Uri uri = PlayerMonsterDescriptor.UriHelper.uriForAll();

		cr.delete(uri, null, null);
		final ContentValues[] values = new ContentValues[monsters.size()];
		int i = 0;
		for (final MonsterCardModel monster : monsters) {
			values[i] = PlayerMonsterHelper.modelToValues(monster);
			i++;
		}
		cr.bulkInsert(uri, values);
	}
}
