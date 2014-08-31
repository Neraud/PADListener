package fr.neraud.padlistener.pad.constant;

/**
 * Created by Neraud on 30/08/2014.
 */
public enum StartingColor {

	RED(1),
	BLUE(2),
	GREEN(3);

	private final int code;

	private StartingColor(int code) {
		this.code = code;
	}

	public static StartingColor valueByCode(int code) {
		for (StartingColor color : values()) {
			if (color.code == code) {
				return color;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}
}
