
package fr.neraud.padlistener.gui.constant;

import android.app.Activity;
import fr.neraud.padlistener.gui.ComputeSyncActivity;
import fr.neraud.padlistener.gui.MainActivity;
import fr.neraud.padlistener.gui.SwitchListenerActivity;
import fr.neraud.padlistener.gui.SyncResultActivity;
import fr.neraud.padlistener.gui.ViewCapturedDataActivity;
import fr.neraud.padlistener.gui.ViewMonsterInfoActivity;

public enum GuiScreen {

	MAIN(MainActivity.class),
	SWITCH_LISTENER(SwitchListenerActivity.class),
	VIEW_MONSTER_INFO(ViewMonsterInfoActivity.class),
	VIEW_CAPTURED_DATA(ViewCapturedDataActivity.class),
	COMPUTE_SYNC(ComputeSyncActivity.class),
	SYNC_RESULT(SyncResultActivity.class);

	private final Class<? extends Activity> activityClass;

	private GuiScreen(Class<? extends Activity> activityClass) {
		this.activityClass = activityClass;
	}

	public Class<? extends Activity> getActivityClass() {
		return activityClass;
	}

}
