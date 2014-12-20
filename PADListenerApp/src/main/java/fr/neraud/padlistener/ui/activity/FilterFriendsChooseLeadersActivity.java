package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.constant.UiScreen;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;

/**
 * Created by Neraud on 14/12/2014.
 */
public class FilterFriendsChooseLeadersActivity extends AbstractPADListenerActivity {

	private List<ChooseModelContainer<CapturedFriendLeaderModel>> mLeaders = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_friends_choose_leaders_activity);

		MyLog.exit();
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.FILTER_FRIENDS;
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "filter_friends_choose_leaders", 1) {
			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.filter_friends_choose_leaders_help_summary_title, R.string.filter_friends_choose_leaders_help_summary_content);
				builder.addHelpPage(R.string.filter_friends_choose_leaders_help_selection_title, R.string.filter_friends_choose_leaders_help_selection_content);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filter_friends_choose_leaders, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.filter_friends_choose_leaders_filter:
				filterLeaders();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void filterLeaders() {
		MyLog.entry();

		if (mLeaders != null) {
			final HashSet<Long> usefulFriendIds = new HashSet<>();
			for (ChooseModelContainer<CapturedFriendLeaderModel> container : mLeaders) {
				if (container.isChosen()) {
					usefulFriendIds.add(container.getModel().getFriendId());
				}
			}

			final Bundle bundle = new Bundle();
			FilterFriendsListUselessActivity.addUsefulFriendIds(bundle, usefulFriendIds);
			goToScreen(UiScreen.FILTER_FRIENDS_LIST_USELESS, bundle);
		}

		MyLog.exit();
	}

	public void setLeaders(List<ChooseModelContainer<CapturedFriendLeaderModel>> leaders) {
		mLeaders = leaders;
	}
}
