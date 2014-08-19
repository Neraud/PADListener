package fr.neraud.padlistener.helper;

import fr.neraud.padlistener.model.BaseMonsterModel;

/**
 * Helper to determine if monsters are equals
 *
 * @author Neraud
 */
public class MonsterComparatorHelper {

	public static boolean areMonstersEqual(BaseMonsterModel a, BaseMonsterModel b) {
		return a.getIdJp() == b.getIdJp() &&
				a.getExp() == b.getExp() &&
				a.getSkillLevel() == b.getSkillLevel() &&
				a.getPlusHp() == b.getPlusHp() &&
				a.getPlusAtk() == b.getPlusAtk() &&
				a.getPlusRcv() == b.getPlusRcv() &&
				a.getAwakenings() == b.getAwakenings();
	}

}
