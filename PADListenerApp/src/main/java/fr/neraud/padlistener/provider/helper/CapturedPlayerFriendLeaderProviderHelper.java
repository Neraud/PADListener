package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.pad.model.PADCapturedFriendModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;

/**
 * Helper for the PlayerMonsterProvider
 *
 * @author Neraud
 */
public class CapturedPlayerFriendLeaderProviderHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the CapturedFriendLeaderModel
	 */
	public static CapturedFriendLeaderModel cursorWithInfoToModel(Cursor cursor) {
		return cursorToModelWithPrefix(cursor, null);
	}

	/**
	 * @param cursor the query result
	 * @param prefix the prefix used for col names
	 * @return the BaseMonsterStatsModel
	 */
	public static CapturedFriendLeaderModel cursorToModelWithPrefix(Cursor cursor, String prefix) {
		final CapturedFriendLeaderModel model = new CapturedFriendLeaderModel();

		model.setIdJp(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.ID_JP));
		model.setFriendId(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.PLAYER_ID));
		model.setLevel(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.LEVEL));
		model.setSkillLevel(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.SKILL_LEVEL));
		model.setPlusHp(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_HP));
		model.setPlusAtk(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_ATK));
		model.setPlusRcv(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_RCV));
		model.setAwakenings(getInt(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.AWAKENINGS));
		model.setLastSeen(getDate(cursor, prefix, CapturedPlayerFriendLeaderDescriptor.Fields.LAST_SEEN));

		return model;
	}

	/**
	 * @param model the CapturedFriendModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(PADCapturedFriendModel friend, BaseMonsterStatsModel model, Date lastSeen) {
		final ContentValues values = new ContentValues();

		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.PLAYER_ID, friend.getId());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.LAST_SEEN, lastSeen);

		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.ID_JP, model.getIdJp());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.LEVEL, model.getLevel());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.SKILL_LEVEL, model.getSkillLevel());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_HP, model.getPlusHp());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_ATK, model.getPlusAtk());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_RCV, model.getPlusRcv());
		putValue(values, CapturedPlayerFriendLeaderDescriptor.Fields.AWAKENINGS, model.getAwakenings());

		return values;
	}
}
