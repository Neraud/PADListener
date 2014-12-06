package fr.neraud.padlistener.ui.activity;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity;

import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.fragment.GenericPreferenceFragment;
import fr.neraud.padlistener.ui.fragment.PADherderAccountsPreferenceFragment;

/**
 * Activity handling settings
 *
 * @author Neraud
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onBuildHeaders(List<PreferenceActivity.Header> target) {
		MyLog.entry();
		loadHeadersFromResource(R.xml.preference_headers, target);
		MyLog.exit();
	}

	@SuppressLint("Override")
	protected boolean isValidFragment(String fragmentName) {
		MyLog.entry("fragmentName = " + fragmentName);

		boolean valid = false;
		if (GenericPreferenceFragment.class.getName().equals(fragmentName)) {
			valid = true;
		} else if(PADherderAccountsPreferenceFragment.class.getName().equals(fragmentName)) {
			valid = true;
		}

		MyLog.exit();
		return valid;
	}
}
