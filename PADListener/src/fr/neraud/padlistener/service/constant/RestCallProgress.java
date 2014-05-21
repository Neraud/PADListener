
package fr.neraud.padlistener.service.constant;

public enum RestCallProgress {

	STARTED(RestCallState.RUNNING),
	RESPONSE_RECEIVED(RestCallState.RUNNING),
	RESPONSE_PARSED(RestCallState.RUNNING),

	DATA_PROCESSED(RestCallState.SUCCESSED),

	REST_CALL_ERROR(RestCallState.FAILED),
	PARSING_ERROR(RestCallState.FAILED),
	PROCESS_ERROR(RestCallState.FAILED);

	private final RestCallState state;

	public RestCallState getState() {
		return state;
	}

	private RestCallProgress(RestCallState state) {
		this.state = state;
	}

}
