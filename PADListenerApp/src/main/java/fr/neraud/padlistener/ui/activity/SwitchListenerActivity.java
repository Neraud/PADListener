package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;

/**
 * Activity to enable/disable the PAD listener
 *
 * @author Neraud
 */
public class SwitchListenerActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switch_listener_activity);
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return null;
	}


	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "switch_listener", 1) {

			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.switch_listener_help_listener_title, R.string.switch_listener_help_listener_content);
				builder.addHelpPage(R.string.switch_listener_help_start_title, R.string.switch_listener_help_start_content);
				builder.addHelpPage(R.string.switch_listener_help_button_title, R.string.switch_listener_help_button_content);
			}

			@Override
			public void buildDeltaHelpPages(PageBuilder builder, int lastDisplayedVersion) {
				switch (lastDisplayedVersion) {
					default:
						buildHelpPages(builder);
				}
			}
		};
	}
}
