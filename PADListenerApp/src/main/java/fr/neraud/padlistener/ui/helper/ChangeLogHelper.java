package fr.neraud.padlistener.ui.helper;

import android.content.Context;
import android.util.Log;

import de.cketti.library.changelog.ChangeLog;

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
		Log.d(getClass().getName(), "displayWhatsNew");
		final ChangeLog cl = new ChangeLog(context);
		if (cl.isFirstRun()) {
			cl.getLogDialog().show();
		}
	}

	public void displayChangeLog() {
		Log.d(getClass().getName(), "displayChangeLog");
		final ChangeLog cl = new ChangeLog(context);
		cl.getFullLogDialog().show();
	}
}
