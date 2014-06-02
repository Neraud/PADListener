
package fr.neraud.padlistener.gui.fragment;

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
import fr.neraud.padlistener.gui.AbstractPADListenerActivity;
import fr.neraud.padlistener.gui.constant.GuiScreen;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;

/**
 * ChooseSync fragment for the Information tab
 * 
 * @author Neraud
 */
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

		final View userInfoBlockView = view.findViewById(R.id.choose_sync_userInfo_block);
		userInfoBlockView.setVisibility(View.GONE);

		final boolean hasUserInfoToSync = false;
		// TODO : enable sync rank when PADHerder API allows it
		/*
		final SyncedUserInfoModel userInfoModel = result.getSyncedUserInfoToUpdate().getSyncedModel();
		if (userInfoModel.getCapturedInfo().equals(userInfoModel.getPadherderInfo())) {
			userInfoBlockView.setVisibility(View.GONE);
		} else {
			hasUserInfoToSync = true;
			userInfoBlockView.setVisibility(View.VISIBLE);
			final CheckBox userInfoCheckBox = (CheckBox) view.findViewById(R.id.choose_sync_userInfo_checkbox);
			userInfoCheckBox.setChecked(true);
			userInfoCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.d(getClass().getName(), "onCheckedChanged");
					result.getSyncedUserInfoToUpdate().setChoosen(isChecked);
				}
			});

			final TextView userInfoText = (TextView) view.findViewById(R.id.choose_sync_userInfo_text);
			userInfoText.setText(getString(R.string.choose_sync_userInfo_text, userInfoModel.getPadherderInfo(),
			        userInfoModel.getCapturedInfo()));

			if (userInfoModel.getPadherderInfo() < userInfoModel.getCapturedInfo()) {
				userInfoText.setTextColor(Color.GREEN);
			} else if (userInfoModel.getPadherderInfo() > userInfoModel.getCapturedInfo()) {
				userInfoText.setTextColor(Color.RED);
			}
		}
		*/

		final Button syncButton = (Button) view.findViewById(R.id.choose_sync_button);

		// TODO check choosen items instead
		if (materialToUpdateCount > 0 || monsterToUpdateCount > 0 || monsterlToCreateCount > 0 || monsterlToDeleteCount > 0
		        || hasUserInfoToSync) {
			syncButton.setEnabled(true);
			syncButton.setText(R.string.choose_sync_info_button_enabled);
			syncButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "onClick");
					syncButton.setClickable(false);

					final Bundle bundle = new Bundle();
					bundle.putSerializable(PushSyncFragment.EXTRA_CHOOSE_SYNC_MODEL_NAME, result);
					((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.PUSH_SYNC, bundle);
				}
			});
		} else {
			syncButton.setEnabled(false);
			syncButton.setText(R.string.choose_sync_info_button_disabled);
		}

		return view;
	}
}
