package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.ui.adapter.ChooseSyncMonstersGroupedAdapter;
import fr.neraud.padlistener.ui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.ui.helper.ChooseSyncGroupedContextMenuHelper;

/**
 * ChooseSync fragment for Monsters set up as grouped
 *
 * @author Neraud
 */
public class ChooseSyncMonstersGroupedFragment extends Fragment {

	private ChooseSyncGroupedContextMenuHelper mMenuHelper;

	@InjectView(R.id.choose_sync_monsters_list)
	ExpandableListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.choose_sync_fragment_monsters_grouped, container, false);
		ButterKnife.inject(this, view);

		registerForContextMenu(mListView);

		final ChooseSyncModel result = (ChooseSyncModel) getArguments().getSerializable(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);
		final String modeName = getArguments().getString(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_MODE_NAME);
		final SyncMode mode = SyncMode.valueOf(modeName);

		final List<ChooseModelContainer<SyncedMonsterModel>> monsters = result.getSyncedMonsters(mode);

		final ChooseSyncMonstersGroupedAdapter adapter = new ChooseSyncMonstersGroupedAdapter(getActivity().getApplicationContext(), monsters);
		mMenuHelper = new ChooseSyncGroupedContextMenuHelper(getActivity(), mode, adapter, result);

		mListView.setAdapter(adapter);

		MyLog.exit();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		mMenuHelper.createContextMenu(menu, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return mMenuHelper.contextItemSelected(item);
	}
}
