package fr.neraud.padlistener.ui.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.InstallationHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.provider.sqlite.PADListenerSQLiteOpenHelper;
import fr.neraud.padlistener.service.InstallMonsterImagesService;
import fr.neraud.padlistener.service.InstallMonsterInfoService;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.constant.UiScreen;
import fr.neraud.padlistener.ui.helper.ChangeLogHelper;
import fr.neraud.padlistener.util.VersionUtil;

/**
 * Base class of all activities.<br/>
 * Handles the navDrawer
 *
 * @author Neraud
 */
public abstract class AbstractPADListenerActivity extends FragmentActivity {

	// delay to launch nav drawer item, to allow close animation to play
	private static final int NAVDRAWER_LAUNCH_DELAY = 250;

	private Handler handler;

	// Navigation drawer:
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	private Map<NavigationDrawerItem, View> navDrawerItems = new HashMap<NavigationDrawerItem, View>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		handler = new Handler();

		// Init DB so it upgrades if necessary
		PADListenerSQLiteOpenHelper.getInstance(getApplicationContext()).getReadableDatabase();

		new ChangeLogHelper(this).displayWhatsNew();

		handleInstallIfNecessary();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onPostCreate");
		super.onPostCreate(savedInstanceState);

		initNavDrawer();
	}

	@Override
	protected void onPostResume() {
		Log.d(getClass().getName(), "onPostResume");
		super.onPostResume();

		// re-select the correct entry in the NavDrawer
		setSelectedNavDrawerItem(getSelfNavDrawerItem());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (drawerToggle != null) {
			drawerToggle.onConfigurationChanged(newConfig);
		}
	}

	private void handleInstallIfNecessary() {
		Log.d(getClass().getName(), "handleInstallIfNecessary");
		final InstallationHelper installHelper = new InstallationHelper(getApplicationContext());
		if (installHelper.needsInstall()) {
			Log.d(getClass().getName(), "handleInstallIfNecessary : starting install");
			startService(new Intent(getApplicationContext(), InstallMonsterInfoService.class));
			startService(new Intent(getApplicationContext(), InstallMonsterImagesService.class));
			final TechnicalSharedPreferencesHelper prefHelper = new TechnicalSharedPreferencesHelper(getApplicationContext());
			prefHelper.setHasBeenInstalled(true);

			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.install_dialog_title);
			builder.setMessage(R.string.install_dialog_message);
			builder.setPositiveButton(R.string.install_dialog_button_ok, null);
			builder.setCancelable(false);
			builder.create().show();
		}
	}

	private void initNavDrawer() {
		Log.d(getClass().getName(), "initNavDrawer");
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(getTitle());
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(getApplicationInfo().labelRes));
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);

		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
		final ActionBar ab = getActionBar();
		if (ab != null) {
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setHomeButtonEnabled(true);
		}

		navDrawerItems.clear();
		final ViewGroup drawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_container);
		drawerItemsListContainer.removeAllViews();

		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.HOME);
		addNavDrawerSeparator(drawerItemsListContainer);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.VIEW_MONSTER_INFO);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.VIEW_CAPTURED_DATA);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.MANAGE_IGNORE_LIST);
		addNavDrawerSeparator(drawerItemsListContainer);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.SETTINGS);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.CHANGELOG);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.ABOUT);
		addNavDrawerItem(drawerItemsListContainer, NavigationDrawerItem.HELP);

		drawerToggle.syncState();
	}

	private void addNavDrawerSeparator(ViewGroup container) {
		Log.d(getClass().getName(), "addNavDrawerSeparator");
		final View view = getLayoutInflater().inflate(R.layout.navdrawer_separator, container, false);
		container.addView(view);
	}

	private void addNavDrawerItem(ViewGroup container, final NavigationDrawerItem item) {
		Log.d(getClass().getName(), "addNavDrawerItem : " + item);
		final View view = getLayoutInflater().inflate(R.layout.navdrawer_item, container, false);
		container.addView(view);

		navDrawerItems.put(item, view);
		final ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		final TextView titleView = (TextView) view.findViewById(R.id.title);

		// set icon and text
		iconView.setVisibility(item.getIconResId() > 0 ? View.VISIBLE : View.GONE);
		if (item.getIconResId() > 0) {
			iconView.setImageResource(item.getIconResId());
		}
		titleView.setText(getString(item.getTitleResId()));
		Log.d(getClass().getName(), "addNavDrawerItem : text = " + getString(item.getTitleResId()));

		final boolean selected = item == getSelfNavDrawerItem();

		formatNavDrawerItem(view, selected);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNavDrawerItemClicked(item);
			}
		});
	}

	private void formatNavDrawerItem(View view, boolean selected) {
		final ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		final TextView titleView = (TextView) view.findViewById(R.id.title);

		// configure its appearance according to whether or not it's selected
		titleView.setTextColor(selected ?
				getResources().getColor(R.color.navdrawer_text_color_selected) :
				getResources().getColor(R.color.navdrawer_text_color));
		iconView.setColorFilter(selected ?
				getResources().getColor(R.color.navdrawer_icon_tint_selected) :
				getResources().getColor(R.color.navdrawer_icon_tint));
	}

	private void setSelectedNavDrawerItem(NavigationDrawerItem selectedItem) {
		for (final NavigationDrawerItem item : navDrawerItems.keySet()) {
			final View view = navDrawerItems.get(item);
			formatNavDrawerItem(view, item == selectedItem);
		}
	}

	private void onNavDrawerItemClicked(final NavigationDrawerItem item) {
		Log.d(getClass().getName(), "onNavDrawerItemClicked : " + item);

		if (item != getSelfNavDrawerItem()) {
			setSelectedNavDrawerItem(item);

			// launch the target Activity after a short delay, to allow the close animation to play
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					goToNavDrawerItem(item);
				}
			}, NAVDRAWER_LAUNCH_DELAY);
		}

		drawerLayout.closeDrawer(Gravity.START);
	}

	private void goToNavDrawerItem(NavigationDrawerItem item) {
		if (item != getSelfNavDrawerItem()) {
			if (item.getUiScreen() != null) {
				goToScreen(item.getUiScreen());
				if (getSelfNavDrawerItem() != NavigationDrawerItem.HOME) {
					finish();
				}
			} else {
				switch (item) {
					case SETTINGS:
						final Intent settingsIntent = new Intent(this, SettingsActivity.class);
						Log.d(getClass().getName(), "onOptionsItemSelected : going to settings : " + settingsIntent);
						startActivity(settingsIntent);
						break;
					case CHANGELOG:
						new ChangeLogHelper(this).displayChangeLog();
						break;
					case ABOUT:
						openAboutDialog();
						break;
					case HELP:
						// TODO
						break;
				}
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(getClass().getName(), "onOptionsItemSelected");
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
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

	public void goToScreen(UiScreen screen) {
		goToScreen(screen, null);
	}

	public void goToScreen(UiScreen screen, Bundle extras) {
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

	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return null;
	}
}
