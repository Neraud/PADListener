package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper.Mode;
import fr.neraud.padlistener.gui.helper.ChooseSyncGroupedContextMenuHelper;
import fr.neraud.padlistener.gui.helper.ChooseSyncMonstersGroupedAdapter;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMonsterModel;

/**
 * ChooseSync fragment for Monsters set up as grouped
 *
 * @author Neraud
 */
public class ChooseSyncMonstersGroupedFragment extends Fragment {

	private ChooseSyncGroupedContextMenuHelper menuHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");
		final View view = inflater.inflate(R.layout.choose_sync_fragment_monsters_grouped, container, false);

		final ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.choose_sync_monsters_list);
		registerForContextMenu(listView);

		final ChooseSyncModel result = (ChooseSyncModel) getArguments().getSerializable(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);
		final String modeName = getArguments().getString(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_MODE_NAME);
		final Mode mode = Mode.valueOf(modeName);

		List<ChooseSyncModelContainer<SyncedMonsterModel>> monsters = null;
		switch (mode) {
			case UPDATED:
				monsters = result.getSyncedMonstersToUpdate();
				break;
			case CREATED:
				monsters = result.getSyncedMonstersToCreate();
				break;
			case DELETED:
				monsters = result.getSyncedMonstersToDelete();
				break;
			default:
				break;
		}

		final BaseExpandableListAdapter adapter = new ChooseSyncMonstersGroupedAdapter(getActivity().getApplicationContext(), monsters);
		menuHelper = new ChooseSyncGroupedContextMenuHelper(getActivity(), mode, adapter, result);

		listView.setAdapter(adapter);

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menuHelper.createContextMenu(menu, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return menuHelper.contextItemSelected(item);
	}
}
