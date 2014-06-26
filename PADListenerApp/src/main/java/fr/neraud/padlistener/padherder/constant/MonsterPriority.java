package fr.neraud.padlistener.padherder.constant;

import fr.neraud.padlistener.R;

/**
 * Enum of monster priorities
 * Created by Neraud on 22/06/2014.
 */
public enum MonsterPriority {

	HIGH(3, R.string.monster_priority_high),
	MED(2, R.string.monster_priority_med),
	LOW(1, R.string.monster_priority_low),
	ZERO(0, R.string.monster_priority_zero);

	// priority in ParHerder API
	private final int value;
	private final int labelResId;

	private MonsterPriority(int value, int labelResId) {
		this.value = value;
		this.labelResId = labelResId;
	}

	public int getValue() {
		return value;
	}

	public int getLabelResId() {
		return labelResId;
	}

	public static MonsterPriority findByValue(int priorityValue) {
		for (MonsterPriority priority : values()) {
			if (priority.getValue() == priorityValue) {
				return priority;
			}
		}

		return null;
	}

	public static MonsterPriority findByOrdinal(int ordinal) {
		for (MonsterPriority priority : values()) {
			if (priority.ordinal() == ordinal) {
				return priority;
			}
		}

		return null;
	}
}
