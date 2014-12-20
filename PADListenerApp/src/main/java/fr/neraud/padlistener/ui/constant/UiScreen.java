package fr.neraud.padlistener.ui.constant;

import android.app.Activity;

import fr.neraud.padlistener.ui.activity.ChooseSyncActivity;
import fr.neraud.padlistener.ui.activity.ComputeSyncActivity;
import fr.neraud.padlistener.ui.activity.FilterFriendsChooseLeadersActivity;
import fr.neraud.padlistener.ui.activity.FilterFriendsListUselessActivity;
import fr.neraud.padlistener.ui.activity.HomeActivity;
import fr.neraud.padlistener.ui.activity.ManageIgnoreListActivity;
import fr.neraud.padlistener.ui.activity.PushSyncActivity;
import fr.neraud.padlistener.ui.activity.SwitchListenerActivity;
import fr.neraud.padlistener.ui.activity.ViewCapturedDataActivity;
import fr.neraud.padlistener.ui.activity.ViewMonsterInfoActivity;

/**
 * Enum of screens in the application
 *
 * @author Neraud
 */
public enum UiScreen {

	HOME(HomeActivity.class),
	SWITCH_LISTENER(SwitchListenerActivity.class),
	VIEW_MONSTER_INFO(ViewMonsterInfoActivity.class),
	VIEW_CAPTURED_DATA(ViewCapturedDataActivity.class),
	MANAGE_IGNORE_LIST(ManageIgnoreListActivity.class),
	FILTER_FRIENDS_CHOOSE_LEADERS(FilterFriendsChooseLeadersActivity.class),
	FILTER_FRIENDS_LIST_USELESS(FilterFriendsListUselessActivity.class),
	COMPUTE_SYNC(ComputeSyncActivity.class, true),
	CHOOSE_SYNC(ChooseSyncActivity.class, true),
	PUSH_SYNC(PushSyncActivity.class, true);

	private final Class<? extends Activity> activityClass;
	private final boolean preventFromHistoryStack;

	private UiScreen(Class<? extends Activity> activityClass) {
		this(activityClass, false);
	}

	private UiScreen(Class<? extends Activity> activityClass, boolean preventFromHistoryStack) {
		this.activityClass = activityClass;
		this.preventFromHistoryStack = preventFromHistoryStack;
	}

	public Class<? extends Activity> getActivityClass() {
		return activityClass;
	}

	public boolean isPreventFromHistoryStack() {
		return preventFromHistoryStack;
	}

}
