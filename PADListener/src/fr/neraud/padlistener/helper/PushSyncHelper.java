
package fr.neraud.padlistener.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import fr.neraud.padlistener.http.client.RestClient;
import fr.neraud.padlistener.http.exception.HttpCallException;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.http.model.MyHttpRequest;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.model.SyncedUserInfoModel;

public class PushSyncHelper {

	private final Context context;
	private final RestClient client;

	public PushSyncHelper(Context context) {
		this.context = context;
		client = new RestClient(PadHerderDescriptor.serverUrl);
	}

	public void pushMaterialToUpdate(SyncedMaterialModel model) throws JSONException, HttpCallException {
		Log.d(getClass().getName(), "pushMaterialUpdate : " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForPatchMaterial(context,
		        model.getPadherderId());

		// FIXME : doesn't work :/

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
	}

	public void pushMonsterToUpdate(SyncedMonsterModel model) throws JSONException, HttpCallException {
		Log.d(getClass().getName(), "pushMonsterToUpdate : " + model);

		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForPatchMonster(context,
		        model.getPadherderId());

		/*
		{
		"monster": 4,
		...
		}
		*/
		final JSONObject json = new JSONObject();
		json.put("monster", model.getMonsterInfo().getId());
		if (model.getCapturedInfo().getExp() != model.getPadherderInfo().getExp()) {
			json.put("current_xp", model.getCapturedInfo().getExp());
		}
		if (model.getCapturedInfo().getSkillLevel() != model.getPadherderInfo().getSkillLevel()) {
			json.put("current_skill", model.getCapturedInfo().getSkillLevel());
		}
		if (model.getCapturedInfo().getAwakenings() != model.getPadherderInfo().getAwakenings()) {
			json.put("current_awakning", model.getCapturedInfo().getAwakenings());
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

		httpRequest.setBody(json.toString());

		client.call(httpRequest);
	}

	public void pushMonsterToCreate(SyncedMonsterModel model) throws JSONException, HttpCallException {
		Log.d(getClass().getName(), "pushMonsterToCreate : " + model);
		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForPostMonster(context);

		/*
		{
		"monster": 4,
		...
		}
		*/
		final JSONObject json = new JSONObject();
		json.put("monster", model.getMonsterInfo().getId());
		json.put("current_xp", model.getCapturedInfo().getExp());
		json.put("current_skill", model.getCapturedInfo().getSkillLevel());
		json.put("current_awakning", model.getCapturedInfo().getAwakenings());
		json.put("plus_hp", model.getCapturedInfo().getPlusHp());
		json.put("plus_atk", model.getCapturedInfo().getPlusAtk());
		json.put("plus_rcv", model.getCapturedInfo().getPlusRcv());

		httpRequest.setBody(json.toString());

		client.call(httpRequest);
	}

	public void pushMonsterToDelete(SyncedMonsterModel model) throws HttpCallException {
		Log.d(getClass().getName(), "pushMonsterToDelete : " + model);
		final MyHttpRequest httpRequest = PadHerderDescriptor.RequestHelper.initRequestForDeleteMonster(context,
		        model.getPadherderId());

		client.call(httpRequest);
	}

	public void pushUserInfoToUpdate(SyncedUserInfoModel model) {
		Log.d(getClass().getName(), "pushUserInfoToUpdate : " + model);

		// TODO : implement sync rank when PADHerder API allows it
	}

}
