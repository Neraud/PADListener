package fr.neraud.padlistener.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.helper.NotificationHelper;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.ui.constant.NavigationDrawerItem;
import fr.neraud.padlistener.ui.fragment.FilterFriendsListUselessFragment;
import fr.neraud.padlistener.ui.helper.BaseHelpManager;

/**
 * Created by Neraud on 14/12/2014.
 */
public class FilterFriendsListUselessActivity extends AbstractPADListenerActivity {

	public static final String EXTRA_USEFUL_FRIEND_IDS_NAME = "useful_friend_ids";
	private HashSet<Long> mUsefulFriendIds;
	private FilterFriendsListUselessFragment mFragment;

	public static void addUsefulFriendIds(Bundle bundle, HashSet<Long> uselessFriendIds) {
		bundle.putSerializable(EXTRA_USEFUL_FRIEND_IDS_NAME, uselessFriendIds);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();

		super.onCreate(savedInstanceState);
		mUsefulFriendIds = (HashSet<Long>) getIntent().getExtras().getSerializable(FilterFriendsListUselessActivity.EXTRA_USEFUL_FRIEND_IDS_NAME);

		setContentView(R.layout.filter_friends_list_useless_activity);

		mFragment = (FilterFriendsListUselessFragment) getSupportFragmentManager().findFragmentById(R.id.filter_friends_list_useless_fragment);

		MyLog.exit();
	}

	@Override
	protected NavigationDrawerItem getSelfNavDrawerItem() {
		return NavigationDrawerItem.FILTER_FRIENDS;
	}

	protected BaseHelpManager getHelpManager() {
		return new BaseHelpManager(this, "filter_friends_list_useless", 1) {
			@Override
			public void buildHelpPages(PageBuilder builder) {
				builder.addHelpPage(R.string.filter_friends_list_useless_help_summary_title, R.string.filter_friends_list_useless_help_summary_content);
				builder.addHelpPage(R.string.filter_friends_list_useless_help_filter_title, R.string.filter_friends_list_useless_help_filter_content, R.id.filter_friends_list_useless_filter, null);
				builder.addHelpPage(R.string.filter_friends_list_useless_help_notifications_title, R.string.filter_friends_list_useless_help_notifications_content, R.id.filter_friends_list_useless_generate_notifications, null);
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
		getMenuInflater().inflate(R.menu.filter_friends_list_useless, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.filter_friends_list_useless_filter:
				mFragment.filterResults(!mFragment.isOnlyNotFavourite());
				invalidateOptionsMenu();
				return true;
			case R.id.filter_friends_list_useless_generate_notifications:
				generateNotifications();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public boolean onPrepareOptionsMenu(final Menu menu) {
		MyLog.entry();

		final int iconId = mFragment.isOnlyNotFavourite() ? R.drawable.ic_action_not_favourite : R.drawable.ic_action_undetermined_favourite;
		menu.findItem(R.id.filter_friends_list_useless_filter).setIcon(iconId);

		final boolean result = super.onPrepareOptionsMenu(menu);
		MyLog.exit();
		return result;
	}

	private void generateNotifications() {
		MyLog.entry();

		final Set<CapturedFriendModel> uselessFriends = mFragment.extractUselessFriend();

		if (uselessFriends.isEmpty()) {
			Toast.makeText(this, R.string.filter_friends_list_useless_no_friend_to_delete_toast, Toast.LENGTH_SHORT).show();
		} else {
			final NotificationHelper helper = new NotificationHelper(this, MyNotification.USELESS_FRIEND_BASE, R.string.filter_friends_list_useless_notification_title);
			int i = 0;
			for (CapturedFriendModel uselessFriend : uselessFriends) {
				final String content = getString(R.string.filter_friends_list_useless_notification_content, uselessFriend.getName());
				helper.displayNotification(content, i);
				i++;
			}
		}

		MyLog.exit();
	}

	public HashSet<Long> getUsefulFriendIds() {
		return mUsefulFriendIds;
	}
}
