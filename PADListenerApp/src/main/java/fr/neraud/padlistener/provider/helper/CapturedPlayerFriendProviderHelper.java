package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;

import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.pad.constant.StartingColor;
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

		final BaseMonsterStatsModel leader1Model = new BaseMonsterStatsModel();
		leader1Model.setIdJp(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_ID_JP));
		leader1Model.setLevel(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_LEVEL));
		leader1Model.setSkillLevel(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_SKILL_LEVEL));
		leader1Model.setPlusHp(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_HP));
		leader1Model.setPlusAtk(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_ATK));
		leader1Model.setPlusRcv(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_RCV));
		leader1Model.setAwakenings(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER1_AWAKENINGS));
		model.setLeader1(leader1Model);

		final BaseMonsterStatsModel leader2Model = new BaseMonsterStatsModel();
		leader2Model.setIdJp(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_ID_JP));
		leader2Model.setLevel(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_LEVEL));
		leader2Model.setSkillLevel(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_SKILL_LEVEL));
		leader2Model.setPlusHp(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_HP));
		leader2Model.setPlusAtk(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_ATK));
		leader2Model.setPlusRcv(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_RCV));
		leader2Model.setAwakenings(getInt(cursor, CapturedPlayerFriendDescriptor.Fields.LEADER2_AWAKENINGS));
		model.setLeader2(leader2Model);

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

		final MonsterInfoModel leader1MonsterInfo = MonsterInfoProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER1_PREFIX);
		model.setLeader1Info(leader1MonsterInfo);
		final MonsterInfoModel leader2MonsterInfo = MonsterInfoProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER2_PREFIX);
		model.setLeader2Info(leader2MonsterInfo);

		return model;
	}

	/**
	 * @param model the CapturedFriendModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(CapturedFriendModel model) {
		final ContentValues values = new ContentValues();

		putValue(values, CapturedPlayerFriendDescriptor.Fields.ID, model.getId());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.NAME, model.getName());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.RANK, model.getRank());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.STARTING_COLOR, model.getStartingColor().getCode());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LAST_ACTIVITY, model.getLastActivityDate());

		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_ID_JP, model.getLeader1().getIdJp());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_LEVEL, model.getLeader1().getLevel());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_SKILL_LEVEL, model.getLeader1().getSkillLevel());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_HP, model.getLeader1().getPlusHp());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_ATK, model.getLeader1().getPlusAtk());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_RCV, model.getLeader1().getPlusRcv());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER1_AWAKENINGS, model.getLeader1().getAwakenings());

		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_ID_JP, model.getLeader2().getIdJp());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_LEVEL, model.getLeader2().getLevel());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_SKILL_LEVEL, model.getLeader2().getSkillLevel());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_HP, model.getLeader2().getPlusHp());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_ATK, model.getLeader2().getPlusAtk());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_RCV, model.getLeader2().getPlusRcv());
		putValue(values, CapturedPlayerFriendDescriptor.Fields.LEADER2_AWAKENINGS, model.getLeader2().getAwakenings());

		return values;
	}
}
