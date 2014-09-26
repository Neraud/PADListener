package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;

/**
 * Activity to manage the ignore list
 *
 * @author Neraud
 */
public class ManageIgnoreListActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_ignore_list_activity);
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.MANAGE_IGNORE_LIST;
	}
}
