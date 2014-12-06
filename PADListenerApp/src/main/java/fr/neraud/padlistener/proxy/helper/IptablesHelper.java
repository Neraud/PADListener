package fr.neraud.padlistener.proxy.helper;

import android.content.Context;

import eu.chainfire.libsuperuser.Shell;
import fr.neraud.log.MyLog;
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
		MyLog.entry();

		checkRoot();

		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);

		final StringBuilder commandBuilder = new StringBuilder("sh ");
		commandBuilder.append(context.getFilesDir().getPath()).append("/enable_iptables.sh");
		commandBuilder.append(" ").append(context.getFilesDir().getPath());
		commandBuilder.append(" ").append(CHAIN_PREFIX);

		commandBuilder.append(" '");
		for(final String hostname : helper.getAllListenerTargetHostnames()) {
			commandBuilder.append(hostname).append(" ");
		}
		commandBuilder.append("'");

		final int processId = context.getApplicationInfo().uid;
		final String excludedUid = String.valueOf(processId);
		commandBuilder.append(" ").append(excludedUid);

		final String targetCommand = commandBuilder.toString();
		executeCommand(targetCommand);

		MyLog.exit();
	}

	public void deactivateIptables() throws MissingRequirementException, CommandExecutionException {
		MyLog.entry();

		checkRoot();

		final StringBuilder commandBuilder = new StringBuilder("sh ");
		commandBuilder.append(context.getFilesDir().getPath()).append("/disable_iptables.sh");
		commandBuilder.append(" ").append(context.getFilesDir().getPath());
		commandBuilder.append(" ").append(CHAIN_PREFIX);

		final String targetCommand = commandBuilder.toString();
		executeCommand(targetCommand);

		MyLog.exit();
	}

	private void checkRoot() throws MissingRequirementException {
		if (!Shell.SU.available()) {
			MyLog.debug("SU not available");
			throw new MissingRequirementException(MissingRequirementException.Requirement.ROOT);
		} else {
			MyLog.debug("SU available");
		}
	}


	private void executeCommand(String targetCommand) throws CommandExecutionException {
		final SuCommandExecutor executor = new SuCommandExecutor(targetCommand);
		executor.execute();

		final String lastLine = executor.getLogs().get(executor.getLogs().size() - 1);
		if(!"Finished".equals(lastLine)) {
			throw new CommandExecutionException("Script failed : " + lastLine, executor.getLogs());
		}
	}
}
