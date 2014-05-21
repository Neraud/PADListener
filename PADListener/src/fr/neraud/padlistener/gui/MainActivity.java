
package fr.neraud.padlistener.gui;

import android.content.Intent;
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

	public void clickOnMyMenu(MainMenuFragment.MainMenuEntry entry) {
		Log.d(getClass().getName(), "clickOnMyMenu : " + entry);
		switch (entry) {
		case SWITCH_LISTENER:
			startActivity(new Intent(getApplicationContext(), SwitchListenerActivity.class));
			break;
		case VIEW_MONSTER_INFO:
			startActivity(new Intent(getApplicationContext(), ViewMonsterInfoActivity.class));
			break;
		case VIEW_CAPTURED_DATA:
			startActivity(new Intent(getApplicationContext(), ViewCapturedDataActivity.class));
			break;
		case VIEW_PADHERDER:
			//TODO
			break;
		case COMPUTE_SYNC:
			//TODO
			break;
		default:
			break;
		}
	}

}
