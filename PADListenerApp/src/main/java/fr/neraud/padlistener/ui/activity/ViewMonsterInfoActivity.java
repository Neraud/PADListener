package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;

import fr.neraud.padlistener.R;

/**
 * Activity to view monster information fetched from PADherder
 *
 * @author Neraud
 */
public class ViewMonsterInfoActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_monster_info_activity);
	}

}
