
package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.util.Log;
import fr.neraud.padlistener.exception.RootCommandExecutionException;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.util.RootCommandExecutor;

/**
 * Helper to manipulate iptables
 * 
 * @author Neraud
 */
public class IptablesHelper {

	private final Context context;

	public IptablesHelper(Context context) {
		this.context = context;
	}

	public void activateIptables() throws RootCommandExecutionException {
		Log.d(getClass().getName(), "activateIptables");

		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(context);

		final StringBuilder commandBuilder = new StringBuilder(context.getFilesDir().getPath());
		commandBuilder.append("/enable_iptables.sh");
		commandBuilder.append(" PADListener_CHAIN");
		commandBuilder.append(" ").append(helper.getListenerTargetHostname());
		final int processId = context.getApplicationInfo().uid;
		final String excludedUid = String.valueOf(processId);
		commandBuilder.append(" ").append(excludedUid);

		final String targetCommand = commandBuilder.toString();
		final RootCommandExecutor commandExecutor = new RootCommandExecutor(targetCommand);
		commandExecutor.execute();
	}

	public void deactivateIptables() throws RootCommandExecutionException {
		Log.d(getClass().getName(), "deactivateIptables");

		final StringBuilder commandBuilder = new StringBuilder(context.getFilesDir().getPath());
		commandBuilder.append("/disable_iptables.sh");
		commandBuilder.append(" PADListener_CHAIN");

		final String targetCommand = commandBuilder.toString();
		final RootCommandExecutor commandExecutor = new RootCommandExecutor(targetCommand);
		commandExecutor.execute();
	}
}
