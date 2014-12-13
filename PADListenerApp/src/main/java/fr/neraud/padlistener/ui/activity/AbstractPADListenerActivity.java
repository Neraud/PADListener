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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.provider.sqlite.PADListenerSQLiteOpenHelper;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.constant.UiScreen;
import fr.neraud.padlistener.ui.fragment.MonsterInfoRefreshDialogFragment;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;
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

	static class NavDrawerItemViewHolder {

		@InjectView(R.id.icon)
		ImageView iconView;

		@InjectView(R.id.title)
		TextView titleView;

		public NavDrawerItemViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	private Handler handler;

	// Navigation drawer:
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	private Map<NavigationDrawerItem, View> navDrawerItems = new HashMap<NavigationDrawerItem, View>();

	private BaseHelpManager mHelpManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);

		handler = new Handler();
		mHelpManager = getHelpManager();

		// Init DB so it upgrades if necessary
		PADListenerSQLiteOpenHelper.getInstance(getApplicationContext()).getReadableDatabase();

		new ChangeLogHelper(this).displayWhatsNew();

		updateMonsterInfoInNecessary();

		MyLog.exit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onPostCreate(savedInstanceState);

		initNavDrawer();

		if (mHelpManager != null) {
			mHelpManager.showHelpFirstTime();
		}

		MyLog.exit();
	}

	@Override
	protected void onPostResume() {
		MyLog.entry();

		super.onPostResume();

		// re-select the correct entry in the NavDrawer
		setSelectedNavDrawerItem(getSelfNavDrawerItem());

		MyLog.exit();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (drawerToggle != null) {
			drawerToggle.onConfigurationChanged(newConfig);
		}
	}

	private void updateMonsterInfoInNecessary() {
		MyLog.entry();

		final TechnicalSharedPreferencesHelper prefHelper = new TechnicalSharedPreferencesHelper(this);

		final Date lastRefresh = prefHelper.getMonsterInfoRefreshDate();
		final Date now = new Date();
		final long daysAgo = TimeUnit.MILLISECONDS.toDays(now.getTime() - lastRefresh.getTime());

		if (daysAgo >= 7) {
			MyLog.debug("starting install");
			final MonsterInfoRefreshDialogFragment fragment = new MonsterInfoRefreshDialogFragment();
			fragment.show(getSupportFragmentManager(), "view_monster_info_refresh");
		}

		MyLog.exit();
	}

	private void initNavDrawer() {
		MyLog.entry();

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

		drawerToggle.syncState();

		MyLog.exit();
	}

	private void addNavDrawerSeparator(ViewGroup container) {
		MyLog.entry();

		final View view = getLayoutInflater().inflate(R.layout.navdrawer_separator, container, false);
		container.addView(view);

		MyLog.exit();
	}

	private void addNavDrawerItem(ViewGroup container, final NavigationDrawerItem item) {
		MyLog.entry("item = " + item);

		final View view = getLayoutInflater().inflate(R.layout.navdrawer_item, container, false);
		view.setId(item.getItemViewId());
		container.addView(view);

		navDrawerItems.put(item, view);
		final NavDrawerItemViewHolder viewHolder = new NavDrawerItemViewHolder(view);

		// set icon and text
		viewHolder.iconView.setVisibility(item.getIconResId() > 0 ? View.VISIBLE : View.GONE);
		if (item.getIconResId() > 0) {
			viewHolder.iconView.setImageResource(item.getIconResId());
		}
		viewHolder.titleView.setText(getString(item.getTitleResId()));
		MyLog.debug("text = " + getString(item.getTitleResId()));

		final boolean selected = item == getSelfNavDrawerItem();

		formatNavDrawerItem(viewHolder, selected);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNavDrawerItemClicked(item);
			}
		});

		MyLog.exit();
	}

	private void formatNavDrawerItem(NavDrawerItemViewHolder viewHolder, boolean selected) {
		// configure its appearance according to whether or not it's selected
		viewHolder.titleView.setTextColor(selected ?
				getResources().getColor(R.color.navdrawer_text_color_selected) :
				getResources().getColor(R.color.navdrawer_text_color));
		viewHolder.iconView.setColorFilter(selected ?
				getResources().getColor(R.color.navdrawer_icon_tint_selected) :
				getResources().getColor(R.color.navdrawer_icon_tint));
	}

	private void setSelectedNavDrawerItem(NavigationDrawerItem selectedItem) {
		MyLog.entry(" - " + selectedItem);

		for (final NavigationDrawerItem item : navDrawerItems.keySet()) {
			final View view = navDrawerItems.get(item);
			final NavDrawerItemViewHolder viewHolder = new NavDrawerItemViewHolder(view);
			formatNavDrawerItem(viewHolder, item == selectedItem);
		}

		MyLog.exit();
	}

	private void onNavDrawerItemClicked(final NavigationDrawerItem item) {
		MyLog.entry(" - " + item);

		if (item != getSelfNavDrawerItem()) {
			// if it is a new screen, highlight the item in the drawer
			if (item.getUiScreen() != null) {
				setSelectedNavDrawerItem(item);
			}
			// else it's a dialog, so keep the previous highlighted item

			// launch the target Activity after a short delay, to allow the close animation to play
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					goToNavDrawerItem(item);
				}
			}, NAVDRAWER_LAUNCH_DELAY);
		}

		drawerLayout.closeDrawer(Gravity.START);

		MyLog.exit();
	}

	private void goToNavDrawerItem(NavigationDrawerItem item) {
		MyLog.entry(" - " + item);

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
						MyLog.debug("going to settings : " + settingsIntent);
						startActivity(settingsIntent);
						break;
					case CHANGELOG:
						new ChangeLogHelper(this).displayChangeLog();
						break;
					case ABOUT:
						openAboutDialog();
						break;
				}
			}
		}

		MyLog.exit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.entry();

		if (mHelpManager != null) {
			getMenuInflater().inflate(R.menu.help, menu);
		}
		final boolean result = super.onCreateOptionsMenu(menu);

		MyLog.exit();
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyLog.entry(" - " + item);

		boolean result;
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (drawerToggle.onOptionsItemSelected(item)) {
			result = true;
		} else {
			switch (item.getItemId()) {
				case R.id.menu_common_help:
					if (mHelpManager != null) {
						// Close the drawer by default, so that the help screen is displayed over the activity
						getDrawerLayout().closeDrawer(Gravity.START);
						mHelpManager.showHelp();
					}
					result = true;
					break;
				default:
					result = super.onOptionsItemSelected(item);
			}
		}

		MyLog.exit();
		return result;
	}

	private void openAboutDialog() {
		MyLog.entry();

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

		MyLog.exit();
	}

	protected BaseHelpManager getHelpManager() {
		// override to add help pages
		return null;
	}

	public void goToScreen(UiScreen screen) {
		goToScreen(screen, null);
	}

	public void goToScreen(UiScreen screen, Bundle extras) {
		MyLog.entry(screen.toString());

		final Intent intent = new Intent(getApplicationContext(), screen.getActivityClass());
		if (extras != null) {
			intent.putExtras(extras);
		}
		if (screen.isPreventFromHistoryStack()) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		}

		// TODO handle wide screens with a different layout ?
		startActivity(intent);

		MyLog.exit();
	}

	public void onBackPressed() {
		MyLog.entry();

		if(mHelpManager != null && mHelpManager.isShowingHelp()) {
			mHelpManager.closeHelp();
		} else {
			super.onBackPressed();
		}

		MyLog.exit();
	}

	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return null;
	}

	protected DrawerLayout getDrawerLayout() {
		return drawerLayout;
	}
}
