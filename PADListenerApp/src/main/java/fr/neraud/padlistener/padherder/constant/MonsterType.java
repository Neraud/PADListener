package fr.neraud.padlistener.padherder.constant;

/**
 * Enum of monster types
 *
 * @author Neraud
 */
public enum MonsterType {

	EVO_MATERIAL(0, true),
	BALANCED(1, false),
	PHYSICAL(2, false),
	HEALER(3, false),
	DRAGON(4, false),
	GOD(5, false),
	ATTACKER(6, false),
	DEVIL(7, false),
	AWOKEN_SKILL_MATERIAL(12, true),
	PROTECTED(13, false),
	ENHANCE_MATERIAL(14, true);

	// typeId in ParHerder API
	private final int typeId;
	//private final boolean material;

	private MonsterType(int typeId, boolean material) {
		this.typeId = typeId;
		//this.material = material;
	}

	public static MonsterType findById(int id) {
		for (final MonsterType type : values()) {
			if (type.typeId == id) {
				return type;
			}
		}
		return null;
	}

	/*
	public boolean isMaterial() {
		return material;
	}
	*/

	public int getTypeId() {
		return typeId;
	}
}
