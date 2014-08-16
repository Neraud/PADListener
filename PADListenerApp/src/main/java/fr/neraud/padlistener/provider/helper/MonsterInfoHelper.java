package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.padherder.constant.MonsterElement;
import fr.neraud.padlistener.padherder.constant.MonsterType;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor.Fields;

/**
 * Helper for the PlayerMonsterProvider
 *
 * @author Neraud
 */
public class MonsterInfoHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the MonsterInfoModel
	 */
	public static MonsterInfoModel cursorToModel(Cursor cursor) {
		final MonsterInfoModel model = new MonsterInfoModel();

		model.setIdJP(getInt(cursor, MonsterInfoDescriptor.Fields.ID_JP));
		model.setIdUS(getInt(cursor, MonsterInfoDescriptor.Fields.ID_US));
		model.setBaseMonsterId(getInt(cursor, MonsterInfoDescriptor.Fields.BASE_ID_JP));
		model.setName(getString(cursor, MonsterInfoDescriptor.Fields.NAME));
		model.setRarity(getInt(cursor, MonsterInfoDescriptor.Fields.RARITY));
		model.setElement1(MonsterElement.findById(getInt(cursor, MonsterInfoDescriptor.Fields.ELEMENT_1)));
		model.setElement2(MonsterElement.findById(getInt(cursor, MonsterInfoDescriptor.Fields.ELEMENT_2)));
		model.setType1(MonsterType.findById(getInt(cursor, MonsterInfoDescriptor.Fields.TYPE_1)));
		model.setType2(MonsterType.findById(getInt(cursor, MonsterInfoDescriptor.Fields.TYPE_2)));
		model.setActiveSkillName(getString(cursor, MonsterInfoDescriptor.Fields.ACTIVE_SKILL_NAME));
		model.setLeaderSkillName(getString(cursor, MonsterInfoDescriptor.Fields.LEADER_SKILL_NAME));
		final List<Integer> awokenSkillIds = new ArrayList<Integer>();
		final String awokenSkillsString = getString(cursor, MonsterInfoDescriptor.Fields.AWOKEN_SKILL_IDS);
		if (awokenSkillsString != null) {
			for (final String id : awokenSkillsString.split(",")) {
				awokenSkillIds.add(Integer.parseInt(id));
			}
		}
		model.setAwokenSkillIds(awokenSkillIds);
		model.setMaxLevel(getInt(cursor, MonsterInfoDescriptor.Fields.MAX_LEVEL));
		model.setExpCurve(getInt(cursor, MonsterInfoDescriptor.Fields.XP_CURVE));
		model.setFeedXp(getInt(cursor, MonsterInfoDescriptor.Fields.FEED_XP));
		model.setEvolutionStage(getInt(cursor, Fields.EVOLUTION_STAGE));
		model.setTeamCost(getInt(cursor, MonsterInfoDescriptor.Fields.TEAM_COST));
		model.setJpOnly(getBoolean(cursor, MonsterInfoDescriptor.Fields.JP_ONLY));
		model.setHpMin(getInt(cursor, MonsterInfoDescriptor.Fields.HP_MIN));
		model.setHpMax(getInt(cursor, MonsterInfoDescriptor.Fields.HP_MAX));
		model.setHpScale(getInt(cursor, MonsterInfoDescriptor.Fields.HP_SCALE));
		model.setAtkMin(getInt(cursor, MonsterInfoDescriptor.Fields.ATK_MIN));
		model.setAtkMax(getInt(cursor, MonsterInfoDescriptor.Fields.ATK_MAX));
		model.setAtkScale(getInt(cursor, MonsterInfoDescriptor.Fields.ATK_SCALE));
		model.setRcvMin(getInt(cursor, MonsterInfoDescriptor.Fields.RCV_MIN));
		model.setRcvMax(getInt(cursor, MonsterInfoDescriptor.Fields.RCV_MAX));
		model.setRcvScale(getInt(cursor, MonsterInfoDescriptor.Fields.RCV_SCALE));
		model.setImage40Url(getString(cursor, MonsterInfoDescriptor.Fields.IMAGE_40_URL));
		model.setImage60Url(getString(cursor, MonsterInfoDescriptor.Fields.IMAGE_60_URL));

		return model;
	}

	/**
	 * @param model the MonsterInfoModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(MonsterInfoModel model) {
		final ContentValues values = new ContentValues();

		putValue(values, MonsterInfoDescriptor.Fields.ID_JP, model.getIdJP());
		putValue(values, MonsterInfoDescriptor.Fields.ID_US, model.getIdUS());
		putValue(values, MonsterInfoDescriptor.Fields.BASE_ID_JP, model.getBaseMonsterId());
		putValue(values, MonsterInfoDescriptor.Fields.NAME, model.getName());
		putValue(values, MonsterInfoDescriptor.Fields.RARITY, model.getRarity());
		putValue(values, MonsterInfoDescriptor.Fields.ELEMENT_1, model.getElement1());
		putValue(values, MonsterInfoDescriptor.Fields.ELEMENT_2, model.getElement2());
		putValue(values, MonsterInfoDescriptor.Fields.TYPE_1, model.getType1());
		putValue(values, MonsterInfoDescriptor.Fields.TYPE_2, model.getType2());
		putValue(values, MonsterInfoDescriptor.Fields.ACTIVE_SKILL_NAME, model.getActiveSkillName());
		putValue(values, MonsterInfoDescriptor.Fields.LEADER_SKILL_NAME, model.getLeaderSkillName());
		if (model.getAwokenSkillIds() != null && !model.getAwokenSkillIds().isEmpty()) {
			final StringBuilder awakenSkillsIds = new StringBuilder();
			for (final Integer id : model.getAwokenSkillIds()) {
				awakenSkillsIds.append(id).append(",");
			}
			awakenSkillsIds.deleteCharAt(awakenSkillsIds.length() - 1);
			putValue(values, MonsterInfoDescriptor.Fields.AWOKEN_SKILL_IDS, awakenSkillsIds.toString());
		}
		putValue(values, MonsterInfoDescriptor.Fields.MAX_LEVEL, model.getMaxLevel());
		putValue(values, MonsterInfoDescriptor.Fields.XP_CURVE, model.getExpCurve());
		putValue(values, MonsterInfoDescriptor.Fields.FEED_XP, model.getFeedXp());
		putValue(values, MonsterInfoDescriptor.Fields.EVOLUTION_STAGE, model.getEvolutionStage());
		putValue(values, MonsterInfoDescriptor.Fields.TEAM_COST, model.getTeamCost());
		putValue(values, MonsterInfoDescriptor.Fields.JP_ONLY, model.isJpOnly());
		putValue(values, MonsterInfoDescriptor.Fields.HP_MIN, model.getHpMin());
		putValue(values, MonsterInfoDescriptor.Fields.HP_MAX, model.getHpMax());
		putValue(values, MonsterInfoDescriptor.Fields.HP_SCALE, model.getHpScale());
		putValue(values, MonsterInfoDescriptor.Fields.ATK_MIN, model.getAtkMin());
		putValue(values, MonsterInfoDescriptor.Fields.ATK_MAX, model.getAtkMax());
		putValue(values, MonsterInfoDescriptor.Fields.ATK_SCALE, model.getAtkScale());
		putValue(values, MonsterInfoDescriptor.Fields.RCV_MIN, model.getRcvMin());
		putValue(values, MonsterInfoDescriptor.Fields.RCV_MAX, model.getRcvMax());
		putValue(values, MonsterInfoDescriptor.Fields.RCV_SCALE, model.getRcvScale());
		putValue(values, MonsterInfoDescriptor.Fields.IMAGE_40_URL, model.getImage40Url());
		putValue(values, MonsterInfoDescriptor.Fields.IMAGE_60_URL, model.getImage60Url());

		return values;
	}

	private static void putValue(ContentValues values, Fields field, MonsterType type) {
		values.put(field.getColName(), type != null ? type.getTypeId() : null);
	}

	private static void putValue(ContentValues values, Fields field, MonsterElement element) {
		values.put(field.getColName(), element != null ? element.getElementId() : null);
	}
}
