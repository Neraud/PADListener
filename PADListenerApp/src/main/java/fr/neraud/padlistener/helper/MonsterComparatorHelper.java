package fr.neraud.padlistener.helper;

import java.util.Map;

import fr.neraud.padlistener.model.BaseMonsterModel;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Helper to determine if monsters are equals
 *
 * @author Neraud
 */
public class MonsterComparatorHelper {

	public static boolean areMonstersEqual(Map<Integer, MonsterInfoModel> monsterInfoById, BaseMonsterModel a, BaseMonsterModel b) {
		return a.getIdJp() == b.getIdJp() &&
				a.getExp() == b.getExp() &&
				a.getSkillLevel() == b.getSkillLevel() &&
				a.getPlusHp() == b.getPlusHp() &&
				a.getPlusAtk() == b.getPlusAtk() &&
				a.getPlusRcv() == b.getPlusRcv() &&
				a.getAwakenings() == b.getAwakenings();
	}

}
