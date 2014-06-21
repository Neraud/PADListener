
package fr.neraud.padlistener.service.constant;

/**
 * Enum of states for a Rest request
 * 
 * @author Neraud
 */
public enum RestCallState {

	RUNNING(1),
	FAILED(2),
	SUCCESSED(0);

	private final int code;

	private RestCallState(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static RestCallState findByCode(int code) {
		for (final RestCallState state : values()) {
			if (state.code == code) {
				return state;
			}
		}
		return null;
	}
}
