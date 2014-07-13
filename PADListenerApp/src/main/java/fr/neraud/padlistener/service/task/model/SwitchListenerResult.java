package fr.neraud.padlistener.service.task.model;

/**
 * Created by Neraud on 13/07/2014.
 */
public class SwitchListenerResult {

	private boolean isSuccess;
	private Exception error;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}
}
