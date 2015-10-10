package fr.neraud.padlistener.http.parser.padherder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.AbstractJsonParser;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.padherder.constant.MonsterElement;
import fr.neraud.padlistener.padherder.constant.MonsterType;

/**
 * JSON parser used to parse PADherder monster api
 *
 * @author Neraud
 */
public class MonsterInfoJsonParser extends AbstractJsonParser<List<MonsterInfoModel>> {

	@Override
	protected List<MonsterInfoModel> parseJsonObject(JSONObject json) throws JSONException, ParsingException {
		throw new ParsingException("Cannot parse JSONObject, JSONArray expected");
	}

	@Override
	protected List<MonsterInfoModel> parseJsonArray(JSONArray json) throws JSONException, ParsingException {
		MyLog.entry();

		final List<MonsterInfoModel> monsterList = new ArrayList<MonsterInfoModel>();
		for (int i = 0; i < json.length(); i++) {
			final JSONObject monsterJson = (JSONObject) json.get(i);
			final MonsterInfoModel monsterModel = parseOneMonster(monsterJson);
			monsterList.add(monsterModel);
		}

		MyLog.exit();
		return monsterList;
	}

	private MonsterInfoModel parseOneMonster(JSONObject monsterJson) throws JSONException {
		MyLog.entry();

		final MonsterInfoModel monster = new MonsterInfoModel();

		monster.setIdJP(monsterJson.getInt("id"));
		if (monsterJson.has("us_id")) {
			monster.setIdUS(monsterJson.getInt("us_id"));
		} else /*if (!monsterJson.getBoolean("jp_only"))*/ {
			monster.setIdUS(monsterJson.getInt("id"));
		}
		monster.setName(monsterJson.getString("name"));
		monster.setRarity(monsterJson.getInt("rarity"));
		monster.setElement1(MonsterElement.findById(monsterJson.getInt("element")));
		monster.setElement2(MonsterElement.findById(monsterJson.optInt("element2", -1)));
		monster.setType1(MonsterType.findById(monsterJson.getInt("type")));
		if(monsterJson.has("type2")) {
			monster.setType2(MonsterType.findById(monsterJson.optInt("type2")));
		}
		monster.setActiveSkillName(monsterJson.optString("active_skill"));
		monster.setLeaderSkillName(monsterJson.optString("leader_skill"));

		final JSONArray awokenSkillArray = monsterJson.getJSONArray("awoken_skills");
		final List<Integer> awokenSkillIds = new ArrayList<Integer>();
		for (int i = 0; i < awokenSkillArray.length(); i++) {
			awokenSkillIds.add(awokenSkillArray.getInt(i));
		}
		monster.setAwokenSkillIds(awokenSkillIds);
		monster.setMaxLevel(monsterJson.getInt("max_level"));
		monster.setExpCurve(monsterJson.getInt("xp_curve"));
		monster.setFeedXp(monsterJson.getInt("feed_xp"));
		monster.setTeamCost(monsterJson.getInt("team_cost"));
		monster.setHpMin(monsterJson.getInt("hp_min"));
		monster.setHpMax(monsterJson.getInt("hp_max"));
		monster.setHpScale(monsterJson.getInt("hp_scale"));
		monster.setAtkMin(monsterJson.getInt("atk_min"));
		monster.setAtkMax(monsterJson.getInt("atk_max"));
		monster.setAtkScale(monsterJson.getInt("atk_scale"));
		monster.setRcvMin(monsterJson.getInt("rcv_min"));
		monster.setRcvMax(monsterJson.getInt("rcv_max"));
		monster.setRcvScale(monsterJson.getInt("rcv_scale"));
		monster.setJpOnly(monsterJson.getBoolean("jp_only"));
		monster.setImage40Url(monsterJson.getString("image40_href"));
		monster.setImage60Url(monsterJson.getString("image60_href"));

		MyLog.exit();
		return monster;
	}
}
