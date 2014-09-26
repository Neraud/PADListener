package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;

/**
 * Activity to enable/disable the PAD listener
 *
 * @author Neraud
 */
public class SwitchListenerActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_listener_activity);
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return null;
	}
}
