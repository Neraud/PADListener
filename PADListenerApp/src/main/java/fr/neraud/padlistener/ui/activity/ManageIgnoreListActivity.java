package fr.neraud.padlistener.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_ignore_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_manage_ignore_list_clear:
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.manage_ignore_list_clear_dialog_title);
				builder.setMessage(R.string.manage_ignore_list_clear_dialog_content);
				builder.setPositiveButton(R.string.manage_ignore_list_clear_dialog_clear_button, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						final ManageIgnoreListTaskFragment taskFragment = (ManageIgnoreListTaskFragment) getSupportFragmentManager().findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);
						taskFragment.clearIgnoredIds();
					}
				});
				builder.setNegativeButton(R.string.manage_ignore_list_clear_dialog_cancel_button, null);
				builder.create().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
