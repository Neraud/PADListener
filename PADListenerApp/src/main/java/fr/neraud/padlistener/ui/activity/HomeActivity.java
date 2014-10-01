package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.helper.ShowcaseViewHelper;

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

	@Override
	protected void buildHelpPages(ShowcaseViewHelper.PageBuilder builder) {
		builder.addHelpPage(R.string.home_help_welcome_title, R.string.home_help_welcome_content);
		builder.addHelpPage(R.string.home_help_help_title, R.string.home_help_help_content, R.id.menu_common_help, null);
		builder.addHelpPage(R.string.home_help_main_title, R.string.home_help_main_content);
		builder.addHelpPage(R.string.home_help_capture_title, R.string.home_help_capture_content);
		builder.addHelpPage(R.string.home_help_drawer_title, R.string.home_help_drawer_content, ActionViewTarget.Type.TITLE, null);
		builder.addHelpPage(R.string.home_help_settings_title, R.string.home_help_settings_content, R.id.drawer_settings, new ShowcaseViewHelper.HelpPageListener() {
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
}
