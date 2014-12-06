package fr.neraud.padlistener.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Activity to manage the ignore list
 *
 * @author Neraud
 */
public class ManageIgnoreListActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_ignore_list_activity);

		MyLog.exit();
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.MANAGE_IGNORE_LIST;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_ignore_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_manage_ignore_list_clear:
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.manage_ignore_list_clear_dialog_title);
				builder.setMessage(R.string.manage_ignore_list_clear_dialog_content);
				builder.setPositiveButton(R.string.manage_ignore_list_clear_dialog_clear_button, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						final ManageIgnoreListTaskFragment taskFragment = (ManageIgnoreListTaskFragment) getSupportFragmentManager().findFragmentByTag(ManageIgnoreListTaskFragment.TAG_TASK_FRAGMENT);
						taskFragment.clearIgnoredIds();
					}
				});
				builder.setNegativeButton(R.string.manage_ignore_list_clear_dialog_cancel_button, null);
				builder.create().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "manage_ignore_list", 1) {

			@Override
			public void buildHelpPages(PageBuilder builder) {
				final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
				final int previousSelected = viewPager.getCurrentItem();

				builder.addHelpPage(R.string.manage_ignore_list_help_ignore_list_title, R.string.manage_ignore_list_help_ignore_list_content);
				builder.addHelpPage(R.string.manage_ignore_list_help_manual_add_title, R.string.manage_ignore_list_help_manual_add_content);
				builder.addHelpPage(R.string.manage_ignore_list_help_manual_remove_title, R.string.manage_ignore_list_help_manual_remove_content, new ShowcaseHelpPageModel.HelpPageListener() {
					@Override
					public void onPreDisplay() {
						viewPager.setCurrentItem(0);
					}

					@Override
					public void onPostDisplay() {
					}
				});
				builder.addHelpPage(R.string.manage_ignore_list_help_quick_actions_title, R.string.manage_ignore_list_help_quick_actions_content, new ShowcaseHelpPageModel.HelpPageListener() {
					@Override
					public void onPreDisplay() {
						viewPager.setCurrentItem(1);
					}

					@Override
					public void onPostDisplay() {
						viewPager.setCurrentItem(previousSelected);
					}
				});
				builder.addHelpPage(R.string.manage_ignore_list_help_clear_title, R.string.manage_ignore_list_help_clear_content, R.id.menu_manage_ignore_list_clear, null);
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
