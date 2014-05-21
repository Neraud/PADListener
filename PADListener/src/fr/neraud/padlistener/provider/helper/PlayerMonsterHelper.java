
package fr.neraud.padlistener.provider.helper;

import android.content.ContentValues;
import android.database.Cursor;
import fr.neraud.padlistener.model.MonsterCardModel;
import fr.neraud.padlistener.provider.descriptor.PlayerMonsterDescriptor;

/**
 * Helper for the PlayerMonsterProvider
 * 
 * @author Neraud
 */
public class PlayerMonsterHelper extends BaseProviderHelper {

	/**
	 * @param cursor the query result
	 * @return the MonsterCardModel
	 */
	public static MonsterCardModel cursorToModel(Cursor cursor) {
		final MonsterCardModel model = new MonsterCardModel();
		try {
			model.setId(getInt(cursor, PlayerMonsterDescriptor.Fields.MONSTER_ID));
			model.setExp(getInt(cursor, PlayerMonsterDescriptor.Fields.EXP));
			model.setLevel(getInt(cursor, PlayerMonsterDescriptor.Fields.LEVEL));
			model.setSkillLevel(getInt(cursor, PlayerMonsterDescriptor.Fields.SKILL_LEVEL));
			model.setPlusHp(getInt(cursor, PlayerMonsterDescriptor.Fields.PLUS_HP));
			model.setPlusAtk(getInt(cursor, PlayerMonsterDescriptor.Fields.PLUS_ATK));
			model.setPlusRcv(getInt(cursor, PlayerMonsterDescriptor.Fields.PLUS_RCV));
			model.setAwakenings(getInt(cursor, PlayerMonsterDescriptor.Fields.AWAKENINGS));
		} catch (final Throwable t) {
			t.printStackTrace();
		}
		return model;
	}

	/**
	 * @param status the MonsterCardModel
	 * @return the filled ContentValues
	 */
	public static ContentValues modelToValues(MonsterCardModel model) {
		final ContentValues values = new ContentValues();

		putValue(values, PlayerMonsterDescriptor.Fields.MONSTER_ID, model.getId());
		putValue(values, PlayerMonsterDescriptor.Fields.EXP, model.getExp());
		putValue(values, PlayerMonsterDescriptor.Fields.LEVEL, model.getLevel());
		putValue(values, PlayerMonsterDescriptor.Fields.SKILL_LEVEL, model.getSkillLevel());
		putValue(values, PlayerMonsterDescriptor.Fields.PLUS_HP, model.getPlusHp());
		putValue(values, PlayerMonsterDescriptor.Fields.PLUS_ATK, model.getPlusAtk());
		putValue(values, PlayerMonsterDescriptor.Fields.PLUS_RCV, model.getPlusRcv());
		putValue(values, PlayerMonsterDescriptor.Fields.AWAKENINGS, model.getAwakenings());

		return values;
	}
}
