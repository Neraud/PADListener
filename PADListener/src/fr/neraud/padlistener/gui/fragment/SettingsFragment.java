
package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import fr.neraud.padlistener.R;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}
}
