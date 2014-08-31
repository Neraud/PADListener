package fr.neraud.padlistener.http.parser.pad;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.helper.MonsterIdConverterHelper;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.AbstractJsonParser;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.pad.constant.StartingColor;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;

/**
 * JSON parser used to parse PAD GetPlayerData
 *
 * @author Neraud
 */
public class GetPlayerDataJsonParser extends AbstractJsonParser<GetPlayerDataApiCallResult> {

	private final PADRegion region;
	private final MonsterIdConverterHelper idConverter;

	public GetPlayerDataJsonParser(Context context, PADRegion region) {
		this.region = region;
		this.idConverter = new MonsterIdConverterHelper(context, region);
	}

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
			playerInfo.setRegion(region);

			model.setPlayerInfo(playerInfo);

			// "card"
			final List<CapturedMonsterCardModel> monsters = new ArrayList<CapturedMonsterCardModel>();
			final JSONArray cardResults = json.getJSONArray("card");
			for (int i = 0; i < cardResults.length(); i++) {
				final JSONObject cardResult = (JSONObject) cardResults.get(i);
				final CapturedMonsterCardModel monster = parseMonster(cardResult);

				monsters.add(monster);
			}
			model.setMonsterCards(monsters);

			// "friends"
			final List<CapturedFriendModel> friends = new ArrayList<CapturedFriendModel>();
			final JSONArray friendResults = json.getJSONArray("friends");
			for (int i = 0; i < friendResults.length(); i++) {
				final JSONArray friendResult = (JSONArray) friendResults.get(i);
				final CapturedFriendModel friend = parseFriend(friendResult);

				friends.add(friend);
			}
			model.setFriends(friends);
		}

		return model;
	}

	private CapturedMonsterCardModel parseMonster(final JSONObject cardResult) throws JSONException {
		//"cuid": 1, "exp": 15939, "lv": 16, "slv": 1, "mcnt": 11, "no": 3, "plus": [0, 0, 0, 0]
		final CapturedMonsterCardModel monster = new CapturedMonsterCardModel();
		monster.setExp(cardResult.getLong("exp"));
		monster.setLevel(cardResult.getInt("lv"));
		monster.setSkillLevel(cardResult.getInt("slv"));
		final int origId = cardResult.getInt("no");
		int idJp = -1;
		try {
			idJp = idConverter.getMonsterRefIdByCapturedId(origId);
		} catch (UnknownMonsterException e) {
			Log.w(getClass().getName(), "parseMonster : " + e.getMessage());
		}
		monster.setIdJp(idJp);
		final JSONArray plusResults = cardResult.getJSONArray("plus");
		monster.setPlusHp(plusResults.getInt(0));
		monster.setPlusAtk(plusResults.getInt(1));
		monster.setPlusRcv(plusResults.getInt(2));
		monster.setAwakenings(plusResults.getInt(3));
		return monster;
	}


	private CapturedFriendModel parseFriend(final JSONArray friendResult) throws JSONException {
		//[4, 333300602, "NeraudMule", 17, 1, "140829151957", 9, 29, 6, 1, 0, 0, 0, 0, 2, 15, 1, 0, 0, 0, 0, 2, 15, 1, 0, 0, 0, 0]
		final CapturedFriendModel friend = new CapturedFriendModel();
		friend.setId(friendResult.getLong(1));
		friend.setName(friendResult.getString(2));
		friend.setRank(friendResult.getInt(3));
		friend.setStartingColor(StartingColor.valueByCode(friendResult.getInt(4)));
		final String lastActivityDateString = friendResult.getString(5);
		try {
			final DateFormat parseFormat = new SimpleDateFormat("yyMMddHHmmss");
			parseFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			final Date lastActivityDate = parseFormat.parse(lastActivityDateString);
			friend.setLastActivityDate(lastActivityDate);
		} catch (ParseException e) {
			Log.w(getClass().getName(), "parseFriend : error parsing lastActivityDate : " + e.getMessage());
		}

		final CapturedFriendLeaderModel leader1 = new CapturedFriendLeaderModel();

		final int origId1 = friendResult.getInt(14);
		int idJp1 = -1;
		try {
			idJp1 = idConverter.getMonsterRefIdByCapturedId(origId1);
		} catch (UnknownMonsterException e) {
			Log.w(getClass().getName(), "parseFriend : leader 1 : " + e.getMessage());
		}

		leader1.setIdJp(idJp1);
		leader1.setLevel(friendResult.getInt(15));
		leader1.setSkillLevel(friendResult.getInt(16));
		leader1.setPlusHp(friendResult.getInt(17));
		leader1.setPlusAtk(friendResult.getInt(18));
		leader1.setPlusRcv(friendResult.getInt(19));
		leader1.setAwakenings(friendResult.getInt(20));
		friend.setLeader1(leader1);

		final CapturedFriendLeaderModel leader2 = new CapturedFriendLeaderModel();

		final int origId2 = friendResult.getInt(21);
		int idJp2 = -1;
		try {
			idJp2 = idConverter.getMonsterRefIdByCapturedId(origId2);
		} catch (UnknownMonsterException e) {
			Log.w(getClass().getName(), "parseFriend : leader 2 : " + e.getMessage());
		}

		leader2.setIdJp(idJp2);
		leader2.setLevel(friendResult.getInt(22));
		leader2.setSkillLevel(friendResult.getInt(23));
		leader2.setPlusHp(friendResult.getInt(24));
		leader2.setPlusAtk(friendResult.getInt(25));
		leader2.setPlusRcv(friendResult.getInt(26));
		leader2.setAwakenings(friendResult.getInt(27));
		friend.setLeader2(leader2);

		return friend;
	}

	@Override
	protected GetPlayerDataApiCallResult parseJsonArray(JSONArray json) throws JSONException, ParsingException {
		throw new ParsingException("Cannot parse JSONArray, JSONObject expected");
	}

}

