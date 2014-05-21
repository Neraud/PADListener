
package fr.neraud.padlistener.gui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.neraud.padlistener.R;

public class AbstractPADListenerActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean consumed = false;
		switch (item.getItemId()) {
		case R.id.menu_settings:
			consumed = true;
			final Intent settingsIntent = new Intent(this, SettingsActivity.class);
			Log.d(getClass().getName(), "onOptionsItemSelected : going to settings : " + settingsIntent);
			startActivity(settingsIntent);
			break;
		case R.id.menu_fetch_monster_info:
			consumed = true;
			final Intent fetchMonsterInfoIntent = new Intent(this, FetchPadHerderMonsterInfoActivity.class);
			Log.d(getClass().getName(), "onOptionsItemSelected : going to fetchMonsterInfo : " + fetchMonsterInfoIntent);
			startActivity(fetchMonsterInfoIntent);
			break;
		default:
			Log.d(getClass().getName(), "onOptionsItemSelected : unknown item " + item.getItemId());
			break;
		}

		if (!consumed) {
			consumed = super.onOptionsItemSelected(item);
		}
		return consumed;
	}
}
