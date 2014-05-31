
package fr.neraud.padlistener.http.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.model.UserInfoMaterialModel;
import fr.neraud.padlistener.model.UserInfoModel;
import fr.neraud.padlistener.model.UserInfoMonsterModel;

public class UserInfoJsonParser extends AbstractJsonParser<UserInfoModel> {

	@SuppressLint("UseSparseArrays")
	@Override
	protected UserInfoModel parseJsonObject(JSONObject json) throws JSONException, ParsingException {
		Log.d(getClass().getName(), "parseJsonObject");
		final UserInfoModel userModel = new UserInfoModel();
		/*
		"account_id": 392392294, 
		"country": 4, 
		"rank": 114, 
		"starter_colour": 1, 
		*/

		userModel.setAccountId(json.getInt("account_id"));
		userModel.setCountryCode(json.getInt("country"));
		userModel.setRank(json.getInt("rank"));
		userModel.setStarterColor(json.getInt("starter_colour"));

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

		return userModel;
	}

	@Override
	protected UserInfoModel parseJsonArray(JSONArray json) throws JSONException, ParsingException {
		Log.d(getClass().getName(), "parseJsonArray");

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
		Log.d(getClass().getName(), "parseOneMonster");
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
		"plus_rcv": 0
		*/

		final UserInfoMonsterModel monster = new UserInfoMonsterModel();

		monster.setPadherderId(monsterJson.getLong("id"));
		monster.setId(monsterJson.getInt("monster"));
		monster.setNote(monsterJson.getString("note"));
		monster.setPriority(monsterJson.getInt("priority"));
		monster.setExp(monsterJson.getInt("current_xp"));
		monster.setSkillLevel(monsterJson.getInt("current_skill"));
		monster.setAwakenings(monsterJson.getInt("current_awakening"));
		monster.setTargetEvolutionId(monsterJson.optInt("target_evolution"));
		monster.setPlusHp(monsterJson.optInt("plus_hp"));
		monster.setPlusAtk(monsterJson.optInt("plus_atk"));
		monster.setPlusRcv(monsterJson.optInt("plus_rcv"));

		return monster;
	}
}
