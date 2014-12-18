package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendLeaderProviderHelper;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;
import fr.neraud.padlistener.ui.activity.FilterFriendsChooseLeadersActivity;
import fr.neraud.padlistener.ui.adapter.FilterFriendsChooseLeadersAdapter;

/**
 * Created by Neraud on 14/12/2014.
 */
public class FilterFriendsChooseLeadersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int MENU_ID_SELECT_ALL = 1;
	private static final int MENU_ID_DESELECT_ALL = 2;

	private FilterFriendsChooseLeadersAdapter mAdapter;

	@InjectView(R.id.filter_friends_choose_leaders_list)
	ExpandableListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.filter_friends_choose_leaders_fragment, container, false);
		ButterKnife.inject(this, view);

		registerForContextMenu(mListView);

		mAdapter = new FilterFriendsChooseLeadersAdapter(getActivity().getApplicationContext());
		mListView.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		MyLog.exit();
		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerFriendLeaderDescriptor.UriHelper.uriForAllWithInfo(), null, null, null, null);
		MyLog.exit();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();
		final List<ChooseModelContainer<CapturedFriendLeaderModel>> leaders = new ArrayList<>();
		final Map<MonsterInfoModel, List<ChooseModelContainer<CapturedFriendLeaderModel>>> childrenByGroup = new HashMap<>();

		while(data.moveToNext()) {
			final CapturedFriendLeaderModel leader = CapturedPlayerFriendLeaderProviderHelper.cursorWithInfoToModel(data);
			final MonsterInfoModel monsterInfo = MonsterInfoProviderHelper.cursorToModelWithPrefix(data, CapturedPlayerFriendLeaderDescriptor.ALL_WITH_INFO_PREFIX);

			if(!childrenByGroup.containsKey(monsterInfo)) {
				childrenByGroup.put(monsterInfo, new ArrayList<ChooseModelContainer<CapturedFriendLeaderModel>>());
			}
			final ChooseModelContainer<CapturedFriendLeaderModel> container = new ChooseModelContainer<>();
			container.setModel(leader);
			childrenByGroup.get(monsterInfo).add(container);
			leaders.add(container);
		}
		mAdapter.updateLeaderModels(childrenByGroup);

		((FilterFriendsChooseLeadersActivity) getActivity()).setLeaders(leaders);

		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		MyLog.exit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		MyLog.entry("menuInfo = " + menuInfo);

		final MonsterInfoModel monsterInfo = getGroupMonsterItem(menuInfo);

		menu.setHeaderTitle(getActivity().getString(R.string.filter_friends_choose_leaders_context_menu_all_title, monsterInfo.getName()));
		menu.add(0, MENU_ID_DESELECT_ALL, 0, R.string.filter_friends_choose_leaders_context_menu_all_deselect);
		menu.add(0, MENU_ID_SELECT_ALL, 0, R.string.filter_friends_choose_leaders_context_menu_all_select);

		MyLog.exit();
	}

	@Override
	public boolean onContextItemSelected(MenuItem menuItem) {
		MyLog.entry("menuItem = " + menuItem);

		final ExpandableListView.ExpandableListContextMenuInfo listItem = (ExpandableListView.ExpandableListContextMenuInfo) menuItem.getMenuInfo();
		final int groupPosition = ExpandableListView.getPackedPositionGroup(listItem.packedPosition);

		//final MonsterInfoModel monsterInfo = getGroupMonsterItem(menuItem.getMenuInfo());

		switch (menuItem.getItemId()) {
			case MENU_ID_SELECT_ALL:
				mAdapter.flagAllChildren(groupPosition, true);
				break;
			case MENU_ID_DESELECT_ALL:
				mAdapter.flagAllChildren(groupPosition, false);
				break;
			default:
		}

		MyLog.exit();
		return true;
	}

	private MonsterInfoModel getGroupMonsterItem(ContextMenu.ContextMenuInfo menuInfo) {
		MyLog.entry("menuInfo = " + menuInfo);

		final ExpandableListView.ExpandableListContextMenuInfo listItem = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		final int groupPosition = ExpandableListView.getPackedPositionGroup(listItem.packedPosition);
		final MonsterInfoModel result = mAdapter.getGroup(groupPosition);

		MyLog.exit();
		return result;
	}
}
