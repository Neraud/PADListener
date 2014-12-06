package fr.neraud.padlistener.proxy.helper;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import fr.neraud.log.MyLog;

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
		MyLog.entry();

		logs = Shell.run("su", commands, null, true);

		boolean result;
		if (logs == null) {
			result = false;
		} else {
			for (final String line : logs) {
				MyLog.debug(" - " + line);
			}

			result = true;
		}

		MyLog.exit();
		return result;
	}

	public List<String> getLogs() {
		return logs;
	}
}
