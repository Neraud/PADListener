
package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.util.Log;
import fr.neraud.padlistener.exception.RootCommandExecutionException;
import fr.neraud.padlistener.util.RootCommandExecutor;

public class IptablesHelper {

	private static String TAG = IptablesHelper.class.getName();

	private final Context context;

	public IptablesHelper(Context context) {
		this.context = context;
	}

	public void activateIptables() throws RootCommandExecutionException {
		Log.d(TAG, "activateIptables");

		final StringBuilder commandBuilder = new StringBuilder(context.getFilesDir().getPath());
		commandBuilder.append("/enable_iptables.sh");
		commandBuilder.append(" PADListener_CHAIN");
		commandBuilder.append(" api-na-adr-pad.gungho.jp");
		final int processId = context.getApplicationInfo().uid;
		final String excludedUid = String.valueOf(processId);
		commandBuilder.append(" ").append(excludedUid);

		final String targetCommand = commandBuilder.toString();
		final RootCommandExecutor commandExecutor = new RootCommandExecutor(targetCommand);
		commandExecutor.execute();
	}

	public void deactivateIptables() throws RootCommandExecutionException {
		Log.d(TAG, "deactivateIptables");

		final StringBuilder commandBuilder = new StringBuilder(context.getFilesDir().getPath());
		commandBuilder.append("/disable_iptables.sh");
		commandBuilder.append(" PADListener_CHAIN");

		final String targetCommand = commandBuilder.toString();
		final RootCommandExecutor commandExecutor = new RootCommandExecutor(targetCommand);
		commandExecutor.execute();
	}
}
