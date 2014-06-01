
package fr.neraud.padlistener.http.parser.pad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.AbstractJsonParser;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;

/**
 * JSON parser used to parse PAD GetPlayerData
 * 
 * @author Neraud
 */
public class GetPlayerDataJsonParser extends AbstractJsonParser<GetPlayerDataApiCallResult> {

	@Override
	protected GetPlayerDataApiCallResult parseJsonObject(JSONObject json) throws JSONException, ParsingException {
		Log.d(getClass().getName(), "parseJsonObject");
		final GetPlayerDataApiCallResult model = new GetPlayerDataApiCallResult();
		final int res = json.getInt("res");
		model.setRes(res);

		if (model.isResOk()) {
			final CapturedPlayerInfoModel playerInfo = new CapturedPlayerInfoModel();
			// "friendMax": 30, "cardMax": 30, "name": "NeraudMule", "lv": 19, "exp": 29209, "cost": 32, "sta": 26, "sta_max": 26, "gold": 5, "coin": 63468, "curLvExp": 27188, "nextLvExp": 30954,
			playerInfo.setLastUpdate(new Date());
			playerInfo.setFriendMax(json.getInt("friendMax"));
			playerInfo.setCardMax(json.getInt("cardMax"));
			playerInfo.setName(json.getString("name"));
			playerInfo.setRank(json.getInt("lv"));
			playerInfo.setExp(json.getLong("exp"));
			playerInfo.setCostMax(json.getInt("cost"));
			playerInfo.setStamina(json.getInt("sta"));
			playerInfo.setStaminaMax(json.getInt("sta_max"));
			playerInfo.setStones(json.getInt("gold"));
			playerInfo.setCoins(json.getLong("coin"));
			playerInfo.setCurrentLevelExp(json.getLong("curLvExp"));
			playerInfo.setNextLevelExp(json.getLong("nextLvExp"));

			model.setPlayerInfo(playerInfo);

			// "card"
			final List<CapturedMonsterCardModel> monsters = new ArrayList<CapturedMonsterCardModel>();
			final JSONArray carsResults = json.getJSONArray("card");
			for (int i = 0; i < carsResults.length(); i++) {
				final JSONObject cardResult = (JSONObject) carsResults.get(i);
				final CapturedMonsterCardModel monster = parseMonster(cardResult);

				monsters.add(monster);
			}
			model.setMonsterCards(monsters);
		}

		return model;
	}

	private CapturedMonsterCardModel parseMonster(final JSONObject cardResult) throws JSONException {
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
		return monster;
	}

	@Override
	protected GetPlayerDataApiCallResult parseJsonArray(JSONArray json) throws JSONException, ParsingException {
		throw new ParsingException("Cannot parse JSONArray, JSONObject expected");
	}

}
