
package fr.neraud.padlistener.helper;

import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Helper for monster Exp
 * 
 * @author Neraud
 */
public class MonsterExpHelper {

	private final MonsterInfoModel monsterInfo;
	private Integer expCap = null;

	public MonsterExpHelper(MonsterInfoModel monsterInfo) {
		this.monsterInfo = monsterInfo;
	}

	public int getExpCap() {
		if (expCap == null) {
			expCap = computeExp(monsterInfo.getExpCurve(), monsterInfo.getMaxLevel());
		}

		return expCap;
	}

	/**
	 * Computes the exp for a given exp curve and level
	 * 
	 * @param expCurve
	 * @param level
	 * @see http://pad.wikia.com/wiki/Monster_EXP_Curves
	 * @return
	 */
	public static int computeExp(int expCurve, int level) {
		return (int) Math.round(expCurve * Math.pow((float) (level - 1) / 98, 2.5));
	}
}
