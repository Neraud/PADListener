package fr.neraud.padlistener.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Created by Neraud on 19/08/2014.
 */
public class MonsterInfoHelper {

	// MonsterId (JP) -> Monster Info
	private final Map<Integer, MonsterInfoModel> monsterInfoById;

	public MonsterInfoHelper(Map<Integer, MonsterInfoModel> monsterInfoById) {
		this.monsterInfoById = monsterInfoById;
	}

	public MonsterInfoHelper(List<MonsterInfoModel> monsterInfos) {
		monsterInfoById = new HashMap<Integer, MonsterInfoModel>();
		for (final MonsterInfoModel monsterInfo : monsterInfos) {
			monsterInfoById.put(monsterInfo.getIdJP(), monsterInfo);
		}
	}

	private boolean checkMonsterExists(Integer monsterId) {
		return monsterId != null && monsterId > 0 && monsterInfoById.containsKey(monsterId) && monsterInfoById.get(monsterId) != null;
	}

	public MonsterInfoModel getMonsterInfo(Integer monsterId) throws UnknownMonsterException {
		if (checkMonsterExists(monsterId)) {
			return monsterInfoById.get(monsterId);
		} else {
			throw new UnknownMonsterException(monsterId);
		}
	}
}
