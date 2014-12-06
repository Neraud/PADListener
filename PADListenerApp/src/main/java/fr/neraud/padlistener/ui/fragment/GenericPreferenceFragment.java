package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;

/**
 * Generic PreferenceFragment handling settings from different XML files
 *
 * @author Neraud
 */
public class GenericPreferenceFragment extends PreferenceFragment {

	private static final String PREFERENCE_LISTENER_NAME = "prefs_listener";
	private static final String PREFERENCE_SYNC_NAME = "prefs_sync";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);

		int preferenceFileToLoad = -1;
		final String settings = getArguments().getString("settings");
		if (PREFERENCE_LISTENER_NAME.equals(settings)) {
			preferenceFileToLoad = R.xml.preference_fragment_listener;
		} else if (PREFERENCE_SYNC_NAME.equals(settings)) {
			preferenceFileToLoad = R.xml.preference_fragment_sync;
		}

		addPreferencesFromResource(preferenceFileToLoad);
		MyLog.exit();
	}
}
