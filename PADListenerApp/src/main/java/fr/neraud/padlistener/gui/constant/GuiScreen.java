
package fr.neraud.padlistener.gui.constant;

import android.app.Activity;
import fr.neraud.padlistener.gui.ChooseSyncActivity;
import fr.neraud.padlistener.gui.ComputeSyncActivity;
import fr.neraud.padlistener.gui.MainActivity;
import fr.neraud.padlistener.gui.PushSyncActivity;
import fr.neraud.padlistener.gui.SwitchListenerActivity;
import fr.neraud.padlistener.gui.ViewCapturedDataActivity;
import fr.neraud.padlistener.gui.ViewMonsterInfoActivity;

/**
 * Enum of screens in the application
 * 
 * @author Neraud
 */
public enum GuiScreen {

	MAIN(MainActivity.class),
	SWITCH_LISTENER(SwitchListenerActivity.class),
	VIEW_MONSTER_INFO(ViewMonsterInfoActivity.class),
	VIEW_CAPTURED_DATA(ViewCapturedDataActivity.class),
	COMPUTE_SYNC(ComputeSyncActivity.class, true),
	CHOOSE_SYNC(ChooseSyncActivity.class, true),
	PUSH_SYNC(PushSyncActivity.class, true);

	private final Class<? extends Activity> activityClass;
	private final boolean preventFromHistoryStack;

	private GuiScreen(Class<? extends Activity> activityClass) {
		this(activityClass, false);
	}

	private GuiScreen(Class<? extends Activity> activityClass, boolean preventFromHistoryStack) {
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
