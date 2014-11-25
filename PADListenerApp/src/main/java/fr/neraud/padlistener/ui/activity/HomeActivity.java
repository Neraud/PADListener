package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Home activity
 *
 * @author Neraud
 */
public class HomeActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.HOME;
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "home", 1) {
			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.home_help_welcome_title, R.string.home_help_welcome_content);
				builder.addHelpPage(R.string.home_help_help_title, R.string.home_help_help_content, R.id.menu_common_help, null);
				builder.addHelpPage(R.string.home_help_main_title, R.string.home_help_main_content);
				builder.addHelpPage(R.string.home_help_capture_title, R.string.home_help_capture_content);
				builder.addHelpPage(R.string.home_help_capture_manual_title, R.string.home_help_capture_manual_content, R.id.home_capture_manual_button, null);
				builder.addHelpPage(R.string.home_help_capture_auto_title, R.string.home_help_capture_auto_content, R.id.home_capture_auto_button, null);
				builder.addHelpPage(R.string.home_help_sync_manual_title, R.string.home_help_sync_manual_content, R.id.home_sync_manual_button, null);
				builder.addHelpPage(R.string.home_help_sync_auto_title, R.string.home_help_sync_auto_content, R.id.home_sync_auto_button, null);
				builder.addHelpPage(R.string.home_help_capture_and_sync_auto_title, R.string.home_help_capture_and_sync_auto_content, R.id.home_capture_and_sync_auto_button, null);
				builder.addHelpPage(R.string.home_help_drawer_title, R.string.home_help_drawer_content, ActionViewTarget.Type.TITLE, null);
				builder.addHelpPage(R.string.home_help_settings_title, R.string.home_help_settings_content, R.id.drawer_settings, new ShowcaseHelpPageModel.HelpPageListener() {
					@Override
					public void onPreDisplay() {
						getDrawerLayout().openDrawer(Gravity.START);
					}

					@Override
					public void onPostDisplay() {
						getDrawerLayout().closeDrawer(Gravity.START);
					}
				});
			}

			@Override
			public void buildDeltaHelpPages(PageBuilder builder, int lastDisplayedVersion) {
				switch (lastDisplayedVersion) {
					default: buildHelpPages(builder);
				}
			}
		};
	}

}
