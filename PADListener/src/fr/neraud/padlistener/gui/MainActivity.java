
package fr.neraud.padlistener.gui;

import android.os.Bundle;
import android.util.Log;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.fragment.MainMenuFragment;

public class MainActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void clickOnMainMenu(MainMenuFragment.MainMenuEntry entry) {
		Log.d(getClass().getName(), "clickOnMainMenu : " + entry);
		switch (entry) {
		case SWITCH_LISTENER:
			goToScreen(SwitchListenerActivity.class);
			break;
		case VIEW_MONSTER_INFO:
			goToScreen(ViewMonsterInfoActivity.class);
			break;
		case VIEW_CAPTURED_DATA:
			goToScreen(ViewCapturedDataActivity.class);
			break;
		/*
		case VIEW_PADHERDER:
			//TODO
			break;
		*/
		case COMPUTE_SYNC:
			goToScreen(ComputeSyncActivity.class);
			break;
		default:
			break;
		}
	}
}
