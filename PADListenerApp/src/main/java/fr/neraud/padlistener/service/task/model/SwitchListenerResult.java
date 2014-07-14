package fr.neraud.padlistener.service.task.model;

import java.util.List;

/**
 * Created by Neraud on 13/07/2014.
 */
public class SwitchListenerResult {

	private boolean isSuccess;
	private List<String> logs;
	private Exception error;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}
}
