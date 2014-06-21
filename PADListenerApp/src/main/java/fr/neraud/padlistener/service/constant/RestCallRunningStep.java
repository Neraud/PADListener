
package fr.neraud.padlistener.service.constant;

/**
 * Enum of Rest steps during the Running state
 * 
 * @author Neraud
 */
public enum RestCallRunningStep {

	STARTED,
	RESPONSE_RECEIVED,
	RESPONSE_PARSED;

	private RestCallRunningStep() {
	}

}
