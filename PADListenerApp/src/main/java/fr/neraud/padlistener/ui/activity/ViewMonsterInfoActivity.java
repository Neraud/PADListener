package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.fragment.MonsterInfoRefreshDialogFragment;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;

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

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.VIEW_MONSTER_INFO;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_monster_info, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_view_monster_info_action_refresh:
				final MonsterInfoRefreshDialogFragment fragment = new MonsterInfoRefreshDialogFragment();
				fragment.show(getSupportFragmentManager(), "view_monster_info_refresh");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "view_monster_info", 1) {
			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.view_monster_info_help_monster_info_title, R.string.view_monster_info_help_monster_info_content);
				builder.addHelpPage(R.string.view_monster_info_help_data_title, R.string.view_monster_info_help_data_content, R.id.menu_view_monster_info_action_refresh, null);
				builder.addHelpPage(R.string.view_monster_info_help_images_title, R.string.view_monster_info_help_images_content);
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
