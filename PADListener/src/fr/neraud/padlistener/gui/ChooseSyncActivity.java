
package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.util.Log;
import fr.neraud.padlistener.R;

public class ChooseSyncActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_sync);
	}

}
