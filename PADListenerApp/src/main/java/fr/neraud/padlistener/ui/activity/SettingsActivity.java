package fr.neraud.padlistener.ui.activity;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.util.List;

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
		Log.d(getClass().getName(), "onBuildHeaders");
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@SuppressLint("Override")
	protected boolean isValidFragment(String fragmentName) {
		Log.d(getClass().getName(), "isValidFragment : " + fragmentName);
		if (GenericPreferenceFragment.class.getName().equals(fragmentName)) {
			return true;
		} else if(PADherderAccountsPreferenceFragment.class.getName().equals(fragmentName)) {
			return true;
		}
		return false;
	}
}
