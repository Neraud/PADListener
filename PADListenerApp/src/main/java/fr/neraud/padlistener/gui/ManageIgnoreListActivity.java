package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import fr.neraud.padlistener.R;

/**
 * Activity to manage the ignore list
 *
 * @author Neraud
 */
public class ManageIgnoreListActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_ignore_list_activity);
	}
}
