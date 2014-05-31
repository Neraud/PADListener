
package fr.neraud.padlistener.gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.constant.GuiScreen;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.InstallMonsterImagesService;
import fr.neraud.padlistener.service.InstallMonsterInfoService;

public class AbstractPADListenerActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		final TechnicalSharedPreferencesHelper prefHelper = new TechnicalSharedPreferencesHelper(getApplicationContext());
		if (!prefHelper.isHasBeenInstalled()) {
			Log.d(getClass().getName(), "onCreate : starting install");
			startService(new Intent(getApplicationContext(), InstallMonsterInfoService.class));
			startService(new Intent(getApplicationContext(), InstallMonsterImagesService.class));
			prefHelper.setHasBeenInstalled(true);

			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.install_dialog_title);
			builder.setMessage(R.string.install_dialog_message);
			builder.setPositiveButton(R.string.install_dialog_button_ok, null);
			builder.create().show();
		}
	}

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

	public void goToScreen(GuiScreen screen) {
		goToScreen(screen, null);
	}

	public void goToScreen(GuiScreen screen, Bundle extras) {
		Log.d(getClass().getName(), "onCreate");
		final Intent intent = new Intent(getApplicationContext(), screen.getActivityClass());
		if (extras != null) {
			intent.putExtras(extras);
		}
		if (screen.isPreventFromHistoryStack()) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		}
		startActivity(intent);
	}

}
