
package fr.neraud.padlistener.gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.constant.GuiScreen;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.provider.sqlite.PADListenerSQLiteOpenHelper;
import fr.neraud.padlistener.service.InstallMonsterImagesService;
import fr.neraud.padlistener.service.InstallMonsterInfoService;
import fr.neraud.padlistener.util.VersionUtil;

/**
 * Base class of all activities.<br/>
 * Handles the menu
 * 
 * @author Neraud
 */
public class AbstractPADListenerActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		// Init DB so it upgrades if necessary
		new PADListenerSQLiteOpenHelper(this).getReadableDatabase().close();

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
			builder.setCancelable(false);
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
		case R.id.menu_about:
			consumed = true;
			openAboutDialog();
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

	private void openAboutDialog() {
		Log.d(getClass().getName(), "openAboutDialog");
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getString(R.string.about_dialog_title, VersionUtil.getVersion(this)));

		final SpannableString message = new SpannableString(getString(R.string.about_dialog_message, VersionUtil.getVersion(this)));
		Linkify.addLinks(message, Linkify.ALL);

		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.about_dialog_button, null);
		final AlertDialog aboutDialog = builder.create();
		aboutDialog.show();

		((TextView) aboutDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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

		// TODO handle wide screens with a different layout ?
		startActivity(intent);
	}

}
