
package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.util.Log;
import fr.neraud.padlistener.R;

public class SwitchListenerActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_listener_activity);
	}

}
