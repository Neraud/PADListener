package fr.neraud.padlistener.helper;

import java.util.Comparator;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.MonsterModel;

/**
 * BaseMonsterModel comparator.<br/>
 * Compares the fields in the following order :
 * <ul>
 * <li>exp</li>
 * <li>skillLevel</li>
 * <li>plusHp</li>
 * <li>plusAtk</li>
 * <li>plusRcv</li>
 * <li>awakenings</li>
 * </ul>
 *
 * @author Neraud
 */
public class MonsterComparator implements Comparator<MonsterModel> {

	private final MonsterInfoHelper monsterInfoHelper;

	public MonsterComparator(MonsterInfoHelper monsterInfoHelper) {
		this.monsterInfoHelper = monsterInfoHelper;
	}

	@Override
	public int compare(MonsterModel a, MonsterModel b) {
		int result;

		try {
			final MonsterInfoModel aInfo = monsterInfoHelper.getMonsterInfo(a.getIdJp());
			final MonsterInfoModel bInfo = monsterInfoHelper.getMonsterInfo(b.getIdJp());

			result = compareLong(aInfo.getEvolutionStage(), bInfo.getEvolutionStage());
			if (result != 0) {
				return result;
			}
		} catch(UnknownMonsterException e) {
			MyLog.warn("missing monster for id = " + e.getMonsterId());
		}

		result = compareLong(a.getExp(), b.getExp());
		if (result != 0) {
			return result;
		}

		result = compareLong(a.getSkillLevel(), b.getSkillLevel());
		if (result != 0) {
			return result;
		}

		result = compareLong(a.getPlusHp(), b.getPlusHp());
		if (result != 0) {
			return result;
		}

		result = compareLong(a.getPlusAtk(), b.getPlusAtk());
		if (result != 0) {
			return result;
		}
		result = compareLong(a.getPlusRcv(), b.getPlusRcv());

		if (result != 0) {
			return result;
		}
		result = compareLong(a.getAwakenings(), b.getAwakenings());

		return result;
	}

	private int compareLong(long a, long b) {
		if (a < b) {
			return -1;
		} else if (a > b) {
			return 1;
		} else {
			return 0;
		}
	}

}
