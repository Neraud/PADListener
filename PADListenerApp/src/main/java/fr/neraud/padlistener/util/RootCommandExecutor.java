package fr.neraud.padlistener.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import fr.neraud.padlistener.exception.RootCommandExecutionException;

/**
 * Helper to execute a shell command as root
 *
 * @author Neraud
 */
public class RootCommandExecutor {

	private final String command;

	public RootCommandExecutor(String command) {
		this.command = command;
	}

	public void execute() throws RootCommandExecutionException {
		Process p;
		try {
			p = Runtime.getRuntime().exec(new String[]{"su", "-c", "sh"});

			final DataOutputStream stdin = new DataOutputStream(p.getOutputStream());

			Log.d(getClass().getName(), "ipTablesForTransparentProxy : calling " + command);

			stdin.writeBytes(command + "\n");
			stdin.writeBytes("exit $?\n");
			final int code = p.waitFor();
			Log.d(getClass().getName(), "ipTablesForTransparentProxy : finished : " + code);
			final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				Log.d(getClass().getName(), line);
			}

			if (code > 0) {
				throw new RootCommandExecutionException("Iptables modifications failed. Exit code : " + code);
			}
		} catch (final Exception e) {
			throw new RootCommandExecutionException(e);
		}
	}
}
