package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.fragment.ViewMonsterInfoRefreshDialogFragment;

/**
 * Activity to view monster information fetched from PADherder
 *
 * @author Neraud
 */
public class ViewMonsterInfoActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_monster_info_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_monster_info, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.view_monster_info_action_refresh:
				FragmentManager fm = getSupportFragmentManager();
				final ViewMonsterInfoRefreshDialogFragment fragment = new ViewMonsterInfoRefreshDialogFragment();
				fragment.show(fm, "view_monster_info_refresh");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
