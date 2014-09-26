package fr.neraud.padlistener.ui.constant;

import fr.neraud.padlistener.R;

/**
 * Created by Neraud on 25/09/2014.
 */
public enum NavigationDrawerItem {

	HOME(R.drawable.ic_drawer_home, R.string.drawer_home_title, UiScreen.HOME),

	VIEW_MONSTER_INFO(R.drawable.ic_drawer_monster_info, R.string.drawer_view_monster_info_title, UiScreen.VIEW_MONSTER_INFO),
	VIEW_CAPTURED_DATA(R.drawable.ic_drawer_captured_data, R.string.drawer_view_captured_data_title, UiScreen.VIEW_CAPTURED_DATA),
	MANAGE_IGNORE_LIST(R.drawable.ic_drawer_ignore_list, R.string.drawer_manage_ignore_list_title, UiScreen.MANAGE_IGNORE_LIST),

	SETTINGS(R.drawable.ic_drawer_settings, R.string.drawer_settings_title, null),
	CHANGELOG(R.drawable.ic_drawer_changelog, R.string.drawer_changelog_title, null),
	ABOUT(R.drawable.ic_drawer_about, R.string.drawer_about_title, null),
	HELP(R.drawable.ic_drawer_help, R.string.drawer_help_title, null);


	private final int iconResId;
	private final int titleResId;
	private final UiScreen uiScreen;

	private NavigationDrawerItem(int iconResId, int titleResId, UiScreen uiScreen) {
		this.iconResId = iconResId;
		this.titleResId = titleResId;
		this.uiScreen = uiScreen;
	}

	public int getIconResId() {
		return iconResId;
	}

	public int getTitleResId() {
		return titleResId;
	}

	public UiScreen getUiScreen() {
		return uiScreen;
	}
}
