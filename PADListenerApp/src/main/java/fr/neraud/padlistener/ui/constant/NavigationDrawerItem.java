package fr.neraud.padlistener.ui.constant;

import fr.neraud.padlistener.R;

/**
 * Created by Neraud on 25/09/2014.
 */
public enum NavigationDrawerItem {

	HOME(R.id.drawer_home, R.drawable.ic_drawer_home, R.string.drawer_home_title, UiScreen.HOME),

	VIEW_MONSTER_INFO(R.id.drawer_view_monster_info, R.drawable.ic_drawer_monster_info, R.string.drawer_view_monster_info_title, UiScreen.VIEW_MONSTER_INFO),
	VIEW_CAPTURED_DATA(R.id.drawer_view_captured_data, R.drawable.ic_drawer_captured_data, R.string.drawer_view_captured_data_title, UiScreen.VIEW_CAPTURED_DATA),
	MANAGE_IGNORE_LIST(R.id.drawer_manage_ignore_list, R.drawable.ic_drawer_ignore_list, R.string.drawer_manage_ignore_list_title, UiScreen.MANAGE_IGNORE_LIST),
	FILTER_FRIENDS(R.id.drawer_filter_friends, R.drawable.ic_action_filter_friends, R.string.drawer_filter_friends_title, UiScreen.FILTER_FRIENDS_CHOOSE_LEADERS),

	SETTINGS(R.id.drawer_settings, R.drawable.ic_drawer_settings, R.string.drawer_settings_title, null),
	CHANGELOG(R.id.drawer_changelog, R.drawable.ic_drawer_changelog, R.string.drawer_changelog_title, null),
	MOCK_CAPTURE(R.id.drawer_mock_capture, R.drawable.ic_action_refresh, R.string.drawer_mock_capture_title, null),
	ABOUT(R.id.drawer_about, R.drawable.ic_drawer_about, R.string.drawer_about_title, null);

	private final int itemViewId;
	private final int iconResId;
	private final int titleResId;
	private final UiScreen uiScreen;

	private NavigationDrawerItem(int itemViewId, int iconResId, int titleResId, UiScreen uiScreen) {
		this.itemViewId = itemViewId;
		this.iconResId = iconResId;
		this.titleResId = titleResId;
		this.uiScreen = uiScreen;
	}

	public int getItemViewId() {
		return itemViewId;
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
