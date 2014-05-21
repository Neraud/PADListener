
package fr.neraud.padlistener.gui;

import android.app.Activity;
import android.os.Bundle;
import fr.neraud.padlistener.gui.fragment.SettingsFragment;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}
}
