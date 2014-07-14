package fr.neraud.padlistener.proxy.helper;

import android.util.Log;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by Neraud on 14/07/2014.
 */
public class SuCommandExecutor {

	private final String[] commands;
	private List<String> logs;

	public SuCommandExecutor(String[] commands) {
		this.commands = commands;
	}

	public SuCommandExecutor(String command) {
		this.commands = new String[]{command};
	}

	public boolean execute() {
		logs = Shell.run("su", commands, null, true);

		if (logs == null) {
			return false;
		} else {
			for (final String result : logs) {
				Log.d(getClass().getName(), "executeCommands : " + result);
			}

			return true;
		}
	}

	public List<String> getLogs() {
		return logs;
	}
}
