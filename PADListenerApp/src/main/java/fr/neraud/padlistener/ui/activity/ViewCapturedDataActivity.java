package fr.neraud.padlistener.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.JsonCaptureHelper;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Activity to view captured data
 *
 * @author Neraud
 */
public class ViewCapturedDataActivity extends AbstractPADListenerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_captured_data_activity);
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.VIEW_CAPTURED_DATA;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_captured_data, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_view_captured_data_action_share:
				final JsonCaptureHelper jsonCaptureHelper = new JsonCaptureHelper(this);
				if (jsonCaptureHelper.hasPadCapturedData()) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					//sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					sendIntent.setType("text/plain");

					Uri uri = Uri.fromFile(jsonCaptureHelper.getPadCapturedDataFile());
					sendIntent.putExtra(Intent.EXTRA_STREAM, uri);

					startActivity(Intent.createChooser(sendIntent, getText(R.string.view_captured_info_item_share_dialog_label)));
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "view_captured_data", 1) {

			@Override
			public void buildHelpPages(PageBuilder builder) {
				final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
				Log.d(getClass().getName(), "buildHelpPages : viewPager = " + viewPager);
				final int previousSelected = viewPager.getCurrentItem();

				builder.addHelpPage(R.string.view_captured_data_help_captured_data_title, R.string.view_captured_data_help_captured_data_content);
				builder.addHelpPage(R.string.view_captured_data_help_player_title, R.string.view_captured_data_help_player_content, new ShowcaseHelpPageModel.HelpPageListener() {
					@Override
					public void onPreDisplay() {
						viewPager.setCurrentItem(0);
					}

					@Override
					public void onPostDisplay() {
					}
				});
				builder.addHelpPage(R.string.view_captured_data_help_monsters_title, R.string.view_captured_data_help_monsters_content, new ShowcaseHelpPageModel.HelpPageListener() {
					@Override
					public void onPreDisplay() {
						viewPager.setCurrentItem(1);
					}

					@Override
					public void onPostDisplay() {
					}
				});
				builder.addHelpPage(R.string.view_captured_data_help_friends_title, R.string.view_captured_data_help_friends_content, new ShowcaseHelpPageModel.HelpPageListener() {
					@Override
					public void onPreDisplay() {
						viewPager.setCurrentItem(2);
					}

					@Override
					public void onPostDisplay() {
						viewPager.setCurrentItem(previousSelected);
					}
				});
				builder.addHelpPage(R.string.view_captured_data_help_share_title, R.string.view_captured_data_help_share_content, R.id.menu_view_captured_data_action_share, null);

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