package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;

/**
 * Activity to push the sync result to PADherder
 *
 * @author Neraud
 */
public class PushSyncActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_sync_activity);

		MyLog.exit();
	}

}
