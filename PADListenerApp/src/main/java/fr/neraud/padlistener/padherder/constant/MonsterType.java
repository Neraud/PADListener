package fr.neraud.padlistener.padherder.constant;

import fr.neraud.log.MyLog;

/**
 * Enum of monster types
 *
 * @author Neraud
 */
public enum MonsterType {

	UNKNOWN(-1),

	EVO_MATERIAL(0),
	BALANCED(1),
	PHYSICAL(2),
	HEALER(3),
	DRAGON(4),
	GOD(5),
	ATTACKER(6),
	DEVIL(7),
	MACHINE(8),
	AWOKEN_SKILL_MATERIAL(12),
	PROTECTED(13),
	ENHANCE_MATERIAL(14),
	VENDOR(15);

	// typeId in ParHerder API
	private final int typeId;

	MonsterType(int typeId) {
		this.typeId = typeId;
	}

	public static MonsterType findById(int id) {
		for (final MonsterType type : values()) {
			if (type.typeId == id) {
				return type;
			}
		}

		MyLog.warn("Monster type " + id + " is unknown");
		return MonsterType.UNKNOWN;
	}

	public int getTypeId() {
		return typeId;
	}
}
