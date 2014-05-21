
package fr.neraud.padlistener.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import android.util.Log;
import fr.neraud.padlistener.exception.RootCommandExecutionException;

public class RootCommandExecutor {

	private static String TAG = RootCommandExecutor.class.getName();
	private final String command;

	public RootCommandExecutor(String command) {
		this.command = command;
	}

	public void execute() throws RootCommandExecutionException {
		Process p;
		try {
			p = Runtime.getRuntime().exec(new String[] { "su", "-c", "sh" });

			final DataOutputStream stdin = new DataOutputStream(p.getOutputStream());

			Log.d(TAG, "ipTablesForTransparentProxy : calling " + command);

			stdin.writeBytes(command + "\n");
			stdin.writeBytes("exit $?\n");
			final int code = p.waitFor();
			Log.d(TAG, "ipTablesForTransparentProxy : finished : " + code);
			final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				Log.d(TAG, line);
			}

			if (code > 0) {
				throw new RootCommandExecutionException("Iptables modifications failed. Exit code : " + code);
			}
		} catch (final Exception e) {
			throw new RootCommandExecutionException(e);
		}
	}
}
