
package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.util.Log;
import fr.neraud.padlistener.R;

/**
 * Activity to push the sync result to PADherder
 * 
 * @author Neraud
 */
public class PushSyncActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_sync_activity);
	}

}
