package fr.neraud.padlistener.constant;

import java.util.TimeZone;

public enum PADRegion {

	JP(TimeZone.getTimeZone("Japan")),
	US(TimeZone.getTimeZone("America/Los_Angeles"));

	private final TimeZone timeZone;

	private PADRegion(TimeZone timeZone) {
		this.timeZone=timeZone;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}
}
