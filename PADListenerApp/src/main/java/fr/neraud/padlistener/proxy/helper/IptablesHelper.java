package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.util.Log;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import fr.neraud.padlistener.exception.CommandExecutionException;
import fr.neraud.padlistener.exception.MissingRequirementException;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;

/**
 * Helper to manipulate iptables
 *
 * @author Neraud
 */
public class IptablesHelper {

	private static final String CHAIN_PREFIX = "PADListener_CHAIN";
	private final Context context;

	public IptablesHelper(Context context) {
		this.context = context;
	}


	public void activateIptables() throws MissingRequirementException, CommandExecutionException {
		Log.d(getClass().getName(), "activateIptables");
		checkRoot();

		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);

		final StringBuilder commandBuilder = new StringBuilder("sh ");
		commandBuilder.append(context.getFilesDir().getPath()).append("/enable_iptables.sh");
		commandBuilder.append(" ").append(context.getFilesDir().getPath());
		commandBuilder.append(" ").append(CHAIN_PREFIX);
		commandBuilder.append(" ").append(helper.getListenerTargetHostname());
		final int processId = context.getApplicationInfo().uid;
		final String excludedUid = String.valueOf(processId);
		commandBuilder.append(" ").append(excludedUid);

		final String targetCommand = commandBuilder.toString();
		executeCommand(targetCommand);
	}

	public void deactivateIptables() throws MissingRequirementException,CommandExecutionException {
		Log.d(getClass().getName(), "deactivateIptables");
		checkRoot();

		final StringBuilder commandBuilder = new StringBuilder("sh ");
		commandBuilder.append(context.getFilesDir().getPath()).append("/disable_iptables.sh");
		commandBuilder.append(" ").append(context.getFilesDir().getPath());
		commandBuilder.append(" ").append(CHAIN_PREFIX);

		final String targetCommand = commandBuilder.toString();
		executeCommand(targetCommand);
	}

	private void checkRoot() throws MissingRequirementException {
		if(!Shell.SU.available()) {
			Log.d(getClass().getName(), "checkRoot : SU not available");
			throw new MissingRequirementException(MissingRequirementException.Requirement.ROOT);
		} else {
			Log.d(getClass().getName(), "checkRoot : SU available");
		}
	}

	private void executeCommand(String command) throws CommandExecutionException {
		final List<String> results = Shell.run("su", new String[]{command}, null, true);

		if(results == null) {
			throw new CommandExecutionException("Error executing script");
		}

		String lastLine = "";
		for (final String result : results) {
			Log.d(getClass().getName(), "executeCommands : " + result);
			lastLine = result;
		}

		if(!"Finished".equals(lastLine)) {
			throw new CommandExecutionException("Script failed : " + lastLine);
		}
	}

}
