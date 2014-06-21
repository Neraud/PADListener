package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import fr.neraud.padlistener.R;

/**
 * Activity to view captured data
 *
 * @author Neraud
 */
public class ViewCapturedDataActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_captured_data_activity);
	}

}
