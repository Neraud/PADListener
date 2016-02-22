package fr.neraud.padlistener.helper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.http.client.RestClient;
import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.SyncedUserInfoModel;

/**
 * Helper to push sync to PADherder.
 *
 * @author Neraud
 */
public class PushSyncHelper {

	private final Context context;
	private final RestClient client;
	private final int accountId;

	public PushSyncHelper(Context context, int accountId) {
		this.context = context;
		this.accountId = accountId;
		client = new RestClient(context, PadHerderDescriptor.serverUrl);
	}

	public void pushMaterialToUpdate(SyncedMaterialModel model) throws JSONException, HttpCallException {
		MyLog.entry("model = " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForPatchMaterial(context, accountId,
				model.getPadherderId());

		/*
		{
		"count": 1,
		}
		*/
		final JSONObject json = new JSONObject();
		//json.put("id", model.getPadherderId());
		//json.put("monster", model.getMonsterInfo().getId());
		json.put("count", model.getCapturedInfo());

		httpRequest.setBody(json.toString());

		client.call(httpRequest);

		MyLog.exit();
	}

	public void pushMonster(SyncMode mode, SyncedMonsterModel model) throws JSONException, HttpCallException {
		switch (mode) {
			case UPDATED:
				pushMonsterToUpdate(model);
				break;
			case CREATED:
				pushMonsterToCreate(model);
				break;
			case DELETED:
				pushMonsterToDelete(model);
				break;
		}
	}

	private void pushMonsterToUpdate(SyncedMonsterModel model) throws JSONException, HttpCallException {
		MyLog.entry("model = " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForPatchMonster(context, accountId,
				model.getPadherderId());

		/*
		{
		"monster": 4,
		...
		}
		*/
		final JSONObject json = new JSONObject();
		json.put("monster", model.getCapturedInfo().getIdJp());

		if (model.getCapturedInfo().getCardId() != null && !model.getCapturedInfo().getCardId().equals(model.getPadherderInfo().getCardId())) {
			json.put("pad_id", model.getCapturedInfo().getCardId());
		}

		if (model.getCapturedInfo().getIdJp() != model.getPadherderInfo().getIdJp()) {
			// Monster evolution, reset the target to the max
			json.put("target_level", model.getCapturedMonsterInfo().getMaxLevel());
		}

		if(model.getCapturedInfo().getIdJp() == model.getPadherderInfo().getTargetEvolutionIdJp()) {
			// When the monster is at the target, reset it to null
			json.put("target_evolution", "null");
		}

		if (model.getCapturedInfo().getExp() != model.getPadherderInfo().getExp()) {
			json.put("current_xp", model.getCapturedInfo().getExp());
		}
		if (model.getCapturedInfo().getSkillLevel() != model.getPadherderInfo().getSkillLevel()) {
			json.put("current_skill", model.getCapturedInfo().getSkillLevel());
		}
		if (model.getCapturedInfo().getAwakenings() != model.getPadherderInfo().getAwakenings()) {
			json.put("current_awakening", model.getCapturedInfo().getAwakenings());
		}
		if (model.getCapturedInfo().getPlusHp() != model.getPadherderInfo().getPlusHp()) {
			json.put("plus_hp", model.getCapturedInfo().getPlusHp());
		}
		if (model.getCapturedInfo().getPlusAtk() != model.getPadherderInfo().getPlusAtk()) {
			json.put("plus_atk", model.getCapturedInfo().getPlusAtk());
		}
		if (model.getCapturedInfo().getPlusRcv() != model.getPadherderInfo().getPlusRcv()) {
			json.put("plus_rcv", model.getCapturedInfo().getPlusRcv());
		}
		if (model.getCapturedInfo().getPriority() != model.getPadherderInfo().getPriority()) {
			json.put("priority", model.getCapturedInfo().getPriority().getValue());
		}
		if (!StringUtils.equals(model.getCapturedInfo().getNote(), model.getPadherderInfo().getNote())) {
			json.put("note", model.getCapturedInfo().getNote());
		}

		httpRequest.setBody(json.toString());

		client.call(httpRequest);

		MyLog.exit();
	}

	private void pushMonsterToCreate(SyncedMonsterModel model) throws JSONException, HttpCallException {
		MyLog.entry("model = " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForPostMonster(context, accountId);

		/*
		{
		"monster": 4,
		...
		}
		*/
		final JSONObject json = new JSONObject();
		json.put("monster", model.getDisplayedMonsterInfo().getIdJP());
		if (model.getCapturedInfo().getCardId() != null) {
			json.put("pad_id", model.getCapturedInfo().getCardId());
		}
		json.put("current_xp", model.getCapturedInfo().getExp());
		json.put("current_skill", model.getCapturedInfo().getSkillLevel());
		json.put("current_awakening", model.getCapturedInfo().getAwakenings());
		json.put("plus_hp", model.getCapturedInfo().getPlusHp());
		json.put("plus_atk", model.getCapturedInfo().getPlusAtk());
		json.put("plus_rcv", model.getCapturedInfo().getPlusRcv());
		json.put("priority", model.getCapturedInfo().getPriority().getValue());
		json.put("note", model.getCapturedInfo().getNote());

		httpRequest.setBody(json.toString());

		client.call(httpRequest);

		MyLog.exit();
	}

	private void pushMonsterToDelete(SyncedMonsterModel model) throws HttpCallException {
		MyLog.entry("model = " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForDeleteMonster(context, accountId,
				model.getPadherderId());

		client.call(httpRequest);

		MyLog.exit();
	}

	public void pushUserInfoToUpdate(SyncedUserInfoModel model) throws HttpCallException, JSONException {
		MyLog.entry("model = " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForUpdateUserInfo(context, accountId,
				model.getProfileApiId());

		/*
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
		
		*/

		final JSONObject json = new JSONObject();
		json.put("rank", model.getCapturedInfo());

		httpRequest.setBody(json.toString());

		client.call(httpRequest);

		MyLog.exit("model = " + model);
	}

}
