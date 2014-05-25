
package fr.neraud.padlistener.gui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ListAdapter;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.gui.helper.ChooseSyncMonstersAdapter;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMonsterModel;

public class ChooseSyncMonstersFragment extends ListFragment {

	public static enum Mode {
		UPDATED,
		CREATED,
		DELETED;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

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

		final ListAdapter adapter = new ChooseSyncMonstersAdapter(getActivity().getApplicationContext(),
		        R.layout.choose_sync_item_monsters, monsters);
		setListAdapter(adapter);
	}
}
