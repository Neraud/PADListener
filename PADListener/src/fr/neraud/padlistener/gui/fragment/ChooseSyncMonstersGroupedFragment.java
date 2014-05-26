
package fr.neraud.padlistener.gui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper.Mode;
import fr.neraud.padlistener.gui.helper.ChooseSyncMonstersGroupedAdapter;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMonsterModel;

public class ChooseSyncMonstersGroupedFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");
		final View view = inflater.inflate(R.layout.choose_sync_fragment_monsters_grouped, container, false);

		final ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.choose_sync_monsters_list);
		listView.setAdapter(buildAdapter());

		return view;
	}

	private ExpandableListAdapter buildAdapter() {
		Log.d(getClass().getName(), "buildAdapter");

		final ChooseSyncModel result = (ChooseSyncModel) getArguments().getSerializable(
		        ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);
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

		final ExpandableListAdapter adapter = new ChooseSyncMonstersGroupedAdapter(getActivity().getApplicationContext(), monsters);
		return adapter;
	}
}
