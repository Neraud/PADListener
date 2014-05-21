
package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;
import fr.neraud.padlistener.model.PlayerInfoModel;
import fr.neraud.padlistener.provider.descriptor.PlayerInfoDescriptor;

/**
 * Helper for the PlayerInfoProvider
 * 
 * @author Neraud
 */
public class PlayerInfoHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the PlayerInfoModel
	 */
	public static PlayerInfoModel cursorToModel(Cursor cursor) {
		final PlayerInfoModel model = new PlayerInfoModel();

		model.setLastUpdate(getDate(cursor, PlayerInfoDescriptor.Fields.LAST_UPDATE));
		model.setFriendMax(getInt(cursor, PlayerInfoDescriptor.Fields.FRIEND_MAX));
		model.setCardMax(getInt(cursor, PlayerInfoDescriptor.Fields.CARD_MAX));
		model.setName(getString(cursor, PlayerInfoDescriptor.Fields.NAME));
		model.setRank(getInt(cursor, PlayerInfoDescriptor.Fields.RANK));
		model.setExp(getLong(cursor, PlayerInfoDescriptor.Fields.EXP));
		model.setCurrentLevelExp(getLong(cursor, PlayerInfoDescriptor.Fields.CURRENT_LEVEL_EXP));
		model.setNextLevelExp(getLong(cursor, PlayerInfoDescriptor.Fields.NEXT_LEVEL_EXP));
		model.setCostMax(getInt(cursor, PlayerInfoDescriptor.Fields.COST_MAX));
		model.setStamina(getInt(cursor, PlayerInfoDescriptor.Fields.STAMINA));
		model.setStaminaMax(getInt(cursor, PlayerInfoDescriptor.Fields.STAMINA_MAX));
		model.setStones(getInt(cursor, PlayerInfoDescriptor.Fields.STONES));
		model.setCoins(getLong(cursor, PlayerInfoDescriptor.Fields.COINS));

		return model;
	}

	/**
	 * @param status the PlayerInfoModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(PlayerInfoModel model) {
		final ContentValues values = new ContentValues();

		putValue(values, PlayerInfoDescriptor.Fields.LAST_UPDATE, model.getLastUpdate());
		putValue(values, PlayerInfoDescriptor.Fields.FRIEND_MAX, model.getFriendMax());
		putValue(values, PlayerInfoDescriptor.Fields.CARD_MAX, model.getCardMax());
		putValue(values, PlayerInfoDescriptor.Fields.NAME, model.getName());
		putValue(values, PlayerInfoDescriptor.Fields.RANK, model.getRank());
		putValue(values, PlayerInfoDescriptor.Fields.EXP, model.getExp());
		putValue(values, PlayerInfoDescriptor.Fields.CURRENT_LEVEL_EXP, model.getCurrentLevelExp());
		putValue(values, PlayerInfoDescriptor.Fields.NEXT_LEVEL_EXP, model.getNextLevelExp());
		putValue(values, PlayerInfoDescriptor.Fields.COST_MAX, model.getCostMax());
		putValue(values, PlayerInfoDescriptor.Fields.STAMINA, model.getStamina());
		putValue(values, PlayerInfoDescriptor.Fields.STAMINA_MAX, model.getStaminaMax());
		putValue(values, PlayerInfoDescriptor.Fields.STONES, model.getStones());
		putValue(values, PlayerInfoDescriptor.Fields.COINS, model.getCoins());

		return values;
	}
}
