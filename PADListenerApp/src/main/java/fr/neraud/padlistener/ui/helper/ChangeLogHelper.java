package fr.neraud.padlistener.ui.helper;

import android.content.Context;

import de.cketti.library.changelog.ChangeLog;
import fr.neraud.log.MyLog;

/**
 * Helper to display the changelog
 * <p/>
 * Created by Neraud on 21/06/2014.
 */
public class ChangeLogHelper {

	private final Context context;

	public ChangeLogHelper(Context context) {
		this.context = context;
	}

	public void displayWhatsNew() {
		MyLog.entry();
		final ChangeLog cl = new ChangeLog(context);
		if (cl.isFirstRun()) {
			cl.getLogDialog().show();
		}
		MyLog.exit();
	}

	public void displayChangeLog() {
		MyLog.entry();
		final ChangeLog cl = new ChangeLog(context);
		cl.getFullLogDialog().show();
		MyLog.exit();
	}
}
