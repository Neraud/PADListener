
package fr.neraud.padlistener.gui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.service.PushSyncService;

public class ChooseSyncInfoFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.choose_sync_fragment_info, container, false);
		final ChooseSyncModel result = (ChooseSyncModel) getArguments().getSerializable(
		        ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);

		final int materialToUpdateCount = result.getSyncedMaterialsToUpdate().size();
		final int monsterToUpdateCount = result.getSyncedMonstersToUpdate().size();
		final int monsterlToCreateCount = result.getSyncedMonstersToCreate().size();
		final int monsterlToDeleteCount = result.getSyncedMonstersToDelete().size();

		final TextView summaryText = (TextView) view.findViewById(R.id.choose_sync_summary);
		summaryText.setText(getString(R.string.choose_sync_info_summary, materialToUpdateCount, monsterToUpdateCount,
		        monsterlToCreateCount, monsterlToDeleteCount));

		final Button syncButton = (Button) view.findViewById(R.id.choose_sync_button);
		syncButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				syncButton.setClickable(false);
				final Intent intent = new Intent(getActivity(), PushSyncService.class);
				intent.putExtra(PushSyncService.EXTRA_CHOOSE_SYNC_MODEL_NAME, result);
				getActivity().startService(intent);
			}
		});

		return view;
	}
}
