package fr.neraud.padlistener.padherder.constant;

/**
 * Enum of monster types
 *
 * @author Neraud
 */
public enum MonsterType {

	EVO_MATERIAL(0),
	BALANCED(1),
	PHYSICAL(2),
	HEALER(3),
	DRAGON(4),
	GOD(5),
	ATTACKER(6),
	DEVIL(7),
	AWOKEN_SKILL_MATERIAL(12),
	PROTECTED(13),
	ENHANCE_MATERIAL(14);

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
		return null;
	}

	public int getTypeId() {
		return typeId;
	}
}
