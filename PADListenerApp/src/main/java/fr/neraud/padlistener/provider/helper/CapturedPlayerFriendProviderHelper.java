package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;

import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.pad.constant.StartingColor;
import fr.neraud.padlistener.pad.model.PADCapturedFriendModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;

/**
 * Helper for the PlayerMonsterProvider
 *
 * @author Neraud
 */
public class CapturedPlayerFriendProviderHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the CapturedFriendModel
	 */
	public static CapturedFriendModel cursorToModel(Cursor cursor) {
		final CapturedFriendModel model = new CapturedFriendModel();

		model.setId(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.ID));
		model.setName(getString(cursor, CapturedPlayerFriendDescriptor.Fields.NAME));
		model.setRank(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.RANK));
		model.setStartingColor(StartingColor.valueByCode(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.STARTING_COLOR)));
		model.setLastActivityDate(getDate(cursor, CapturedPlayerFriendDescriptor.Fields.LAST_ACTIVITY));
		model.setFavourite(getBoolean(cursor, CapturedPlayerFriendDescriptor.Fields.FAVOURITE));

		return model;
	}

	/**
	 * @param cursor the query result
	 * @return the CapturedFriendFullInfoModel
	 */
	public static CapturedFriendFullInfoModel cursorWithInfoToModel(Cursor cursor) {
		final CapturedFriendFullInfoModel model = new CapturedFriendFullInfoModel();

		final CapturedFriendModel friendModel = cursorToModel(cursor);
		model.setFriendModel(friendModel);

		final BaseMonsterStatsModel leader1 = CapturedPlayerFriendLeaderProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER1_PREFIX);
		model.setLeader1(leader1);

		final BaseMonsterStatsModel leader2 = CapturedPlayerFriendLeaderProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER2_PREFIX);
		model.setLeader2(leader2);

		final MonsterInfoModel leader1MonsterInfo = MonsterInfoProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER1_INFO_PREFIX);
		model.setLeader1Info(leader1MonsterInfo);

		final MonsterInfoModel leader2MonsterInfo = MonsterInfoProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER2_INFO_PREFIX);
		model.setLeader2Info(leader2MonsterInfo);

		return model;
	}

	/**
	 * @param model the CapturedFriendModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(PADCapturedFriendModel model, Long leader1Id, Long leader2Id) {
		final ContentValues values = new ContentValues();

		putValue(values, CapturedPlayerFriendDescriptor.Fields.ID, model.getId());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.NAME, model.getName());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.RANK, model.getRank());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.STARTING_COLOR, model.getStartingColor().getCode());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LAST_ACTIVITY, model.getLastActivityDate());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_ID, leader1Id);
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_ID, leader2Id);

		return values;
	}
}
