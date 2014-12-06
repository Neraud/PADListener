package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListAdapter;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.ui.adapter.ChooseSyncMaterialsAdapter;
import fr.neraud.padlistener.ui.helper.ChooseSyncDataPagerHelper;

/**
 * ChooseSync fragment for the MaterialsUpdated tab
 *
 * @author Neraud
 */
public class ChooseSyncMaterialsFragment extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);
		final ChooseSyncModel result = (ChooseSyncModel) getArguments().getSerializable(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);
		final ListAdapter adapter = new ChooseSyncMaterialsAdapter(getActivity().getApplicationContext(), result.getSyncedMaterialsToUpdate());
		setListAdapter(adapter);
		MyLog.exit();
	}
}
