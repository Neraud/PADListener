package fr.neraud.padlistener.helper;

import fr.neraud.padlistener.model.BaseMonsterModel;

/**
 * Helper to compare monsters. Not a real comparator.<br/>
 * It is used to sort monsters in the sync process.
 *
 * @author Neraud
 */
public class MonsterComparatorHelper {

	public static enum MonsterComparisonResult {
		// All the fields are lower or equals
		LOWER,
		// Some fields are lower, some are greater
		UNDETERMINED,
		// All the fields are equals
		EQUALS,
		// All the fields are greater or equals
		GREATER
	}

	private static class UndeterminedException extends Exception {

		private static final long serialVersionUID = 1L;

	}

	public static MonsterComparisonResult compareMonsters(BaseMonsterModel a, BaseMonsterModel b) {
		MonsterComparisonResult result = MonsterComparisonResult.EQUALS;

		try {
			result = compareOneProperty(result, a.getExp(), b.getExp());
			result = compareOneProperty(result, a.getSkillLevel(), b.getSkillLevel());
			result = compareOneProperty(result, a.getPlusHp(), b.getPlusHp());
			result = compareOneProperty(result, a.getPlusAtk(), b.getPlusAtk());
			result = compareOneProperty(result, a.getPlusRcv(), b.getPlusRcv());
			result = compareOneProperty(result, a.getAwakenings(), b.getAwakenings());
		} catch (final UndeterminedException e) {
			return MonsterComparisonResult.UNDETERMINED;
		}

		return result;
	}

	private static MonsterComparisonResult compareOneProperty(MonsterComparisonResult previousResult, long a, long b)
			throws UndeterminedException {
		final MonsterComparisonResult newResult = compareNumbers(a, b);
		if (previousResult == newResult) {
			return previousResult;
		} else if (previousResult == MonsterComparisonResult.EQUALS) {
			return newResult;
		} else if (newResult == MonsterComparisonResult.EQUALS) {
			return previousResult;
		} else {
			throw new UndeterminedException();
		}
	}

	private static MonsterComparisonResult compareNumbers(long a, long b) {
		if (a < b) {
			return MonsterComparisonResult.LOWER;
		} else if (a == b) {
			return MonsterComparisonResult.EQUALS;
		} else {
			return MonsterComparisonResult.GREATER;
		}
	}
}
