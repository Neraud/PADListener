package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;

import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;

/**
 * Helper for the PlayerInfoProvider
 *
 * @author Neraud
 */
public class CapturedPlayerInfoProviderHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the PlayerInfoModel
	 */
	public static CapturedPlayerInfoModel cursorToModel(Cursor cursor) {
		final CapturedPlayerInfoModel model = new CapturedPlayerInfoModel();

		model.setLastUpdate(getDate(cursor, CapturedPlayerInfoDescriptor.Fields.LAST_UPDATE));
		model.setFriendMax(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.FRIEND_MAX));
		model.setCardMax(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.CARD_MAX));
		model.setName(getString(cursor, CapturedPlayerInfoDescriptor.Fields.NAME));
		model.setRank(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.RANK));
		model.setExp(getLong(cursor, CapturedPlayerInfoDescriptor.Fields.EXP));
		model.setCurrentLevelExp(getLong(cursor, CapturedPlayerInfoDescriptor.Fields.CURRENT_LEVEL_EXP));
		model.setNextLevelExp(getLong(cursor, CapturedPlayerInfoDescriptor.Fields.NEXT_LEVEL_EXP));
		model.setCostMax(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.COST_MAX));
		model.setStamina(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.STAMINA));
		model.setStaminaMax(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.STAMINA_MAX));
		model.setStones(getInt(cursor, CapturedPlayerInfoDescriptor.Fields.STONES));
		model.setCoins(getLong(cursor, CapturedPlayerInfoDescriptor.Fields.COINS));
		model.setRegion(PADRegion.valueOf(getString(cursor, CapturedPlayerInfoDescriptor.Fields.REGION)));

		return model;
	}

	/**
	 * @param model the CapturedPlayerInfoModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(CapturedPlayerInfoModel model) {
		final ContentValues values = new ContentValues();

		putValue(values, CapturedPlayerInfoDescriptor.Fields.LAST_UPDATE, model.getLastUpdate());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.FRIEND_MAX, model.getFriendMax());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.CARD_MAX, model.getCardMax());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.NAME, model.getName());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.RANK, model.getRank());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.EXP, model.getExp());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.CURRENT_LEVEL_EXP, model.getCurrentLevelExp());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.NEXT_LEVEL_EXP, model.getNextLevelExp());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.COST_MAX, model.getCostMax());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.STAMINA, model.getStamina());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.STAMINA_MAX, model.getStaminaMax());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.STONES, model.getStones());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.COINS, model.getCoins());
		putValue(values, CapturedPlayerInfoDescriptor.Fields.REGION, model.getRegion().name());

		return values;
	}
}
