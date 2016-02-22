package fr.neraud.padlistener.http.parser.padherder;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.AbstractJsonParser;
import fr.neraud.padlistener.model.UserInfoMaterialModel;
import fr.neraud.padlistener.model.UserInfoModel;
import fr.neraud.padlistener.model.UserInfoMonsterModel;
import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * JSON parser used to parse PADherder user api
 *
 * @author Neraud
 */
public class UserInfoJsonParser extends AbstractJsonParser<UserInfoModel> {

	@SuppressLint("UseSparseArrays")
	@Override
	protected UserInfoModel parseJsonObject(JSONObject json) throws JSONException, ParsingException {
		MyLog.entry();

		final UserInfoModel userModel = new UserInfoModel();

		/*
		"profile": {
			"id": 15458,
			"url": "https://www.padherder.com/user-api/profile/15458/",
			"public": true,
			"display_name": "",
			"profile_text": "",
			"account_id": 0,
			"country": 2,
			"rank": 6,
			"starter_colour": 0,
			"team_group_1": "Team Group 1",
			"team_group_2": "Team Group 2",
			"team_group_3": "Team Group 3",
			"team_group_4": "Team Group 4",
			"team_group_5": "Team Group 5"
		}
		*/

		final JSONObject profileJson = json.getJSONObject("profile");

		userModel.setProfileApiId(profileJson.getInt("id"));
		userModel.setAccountId(profileJson.getInt("account_id"));
		userModel.setCountryCode(profileJson.getInt("country"));
		userModel.setRank(profileJson.getInt("rank"));
		userModel.setStarterColor(profileJson.getInt("starter_colour"));

		final List<UserInfoMaterialModel> materials = new ArrayList<UserInfoMaterialModel>();
		final JSONArray materialsArray = json.getJSONArray("materials");
		for (int i = 0; i < materialsArray.length(); i++) {
			final JSONObject materialJson = (JSONObject) materialsArray.get(i);
			final UserInfoMaterialModel material = parseOneMaterial(materialJson);
			materials.add(material);
		}
		userModel.setMaterials(materials);

		final List<UserInfoMonsterModel> monsters = new ArrayList<UserInfoMonsterModel>();
		final JSONArray monstersArray = json.getJSONArray("monsters");
		for (int i = 0; i < monstersArray.length(); i++) {
			final JSONObject monsterJson = (JSONObject) monstersArray.get(i);
			final UserInfoMonsterModel monsterModel = parseOneMonster(monsterJson);
			monsters.add(monsterModel);
		}
		userModel.setMonsters(monsters);

		MyLog.debug("userModel = " + userModel);

		MyLog.exit();
		return userModel;
	}

	@Override
	protected UserInfoModel parseJsonArray(JSONArray json) throws JSONException, ParsingException {
		throw new ParsingException("Cannot parse JSONArray, JSONObject expected");
	}

	private UserInfoMaterialModel parseOneMaterial(JSONObject materialJson) throws JSONException {
		/*
		"id": 1,
		"monster": 162, 
		"count": 4
		*/

		final UserInfoMaterialModel material = new UserInfoMaterialModel();
		material.setPadherderId(materialJson.getLong("id"));
		material.setId(materialJson.getInt("monster"));
		material.setQuantity(materialJson.getInt("count"));

		return material;
	}

	private UserInfoMonsterModel parseOneMonster(JSONObject monsterJson) throws JSONException {
		MyLog.entry();

		/*
		"id": 650,
		"monster": 4, 
		"note": "", 
		"priority": 2, 
		"current_xp": 98324, 
		"current_skill": 3, 
		"current_awakening": 2, 
		"target_level": 99, 
		"target_evolution": 480, 
		"plus_hp": 3, 
		"plus_atk": 3, 
		"plus_rcv": 0,
        "pad_id": 784
		*/

		final UserInfoMonsterModel monster = new UserInfoMonsterModel();

		if (monsterJson.has("pad_id")) {
			monster.setCardId(monsterJson.getLong("pad_id"));
		}
		monster.setPadherderId(monsterJson.getLong("id"));
		monster.setIdJp(monsterJson.getInt("monster"));
		monster.setNote(monsterJson.getString("note"));
		monster.setPriority(MonsterPriority.findByValue(monsterJson.getInt("priority")));
		monster.setExp(monsterJson.getInt("current_xp"));
		monster.setSkillLevel(monsterJson.getInt("current_skill"));
		monster.setAwakenings(monsterJson.getInt("current_awakening"));
		monster.setTargetEvolutionIdJp(monsterJson.optInt("target_evolution"));
		monster.setPlusHp(monsterJson.optInt("plus_hp"));
		monster.setPlusAtk(monsterJson.optInt("plus_atk"));
		monster.setPlusRcv(monsterJson.optInt("plus_rcv"));

		MyLog.exit();
		return monster;
	}
}
