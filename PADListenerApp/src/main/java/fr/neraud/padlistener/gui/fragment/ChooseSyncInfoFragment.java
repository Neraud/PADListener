package fr.neraud.padlistener.gui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.AbstractPADListenerActivity;
import fr.neraud.padlistener.gui.constant.GuiScreen;
import fr.neraud.padlistener.gui.helper.ChooseSyncDataPagerHelper;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedUserInfoModel;

/**
 * ChooseSync fragment for the Information tab
 *
 * @author Neraud
 */
public class ChooseSyncInfoFragment extends Fragment {

	private ChooseSyncModel result;
	private int accountId;
	private TextView summaryText;
	private Button syncButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.choose_sync_fragment_info, container, false);
		result = (ChooseSyncModel) getArguments().getSerializable(ChooseSyncDataPagerHelper.ARGUMENT_SYNC_RESULT_NAME);
		accountId = getArguments().getInt(ChooseSyncDataPagerHelper.ARGUMENT_ACCOUNT_ID_NAME);

		summaryText = (TextView) view.findViewById(R.id.choose_sync_summary);

		final TextView unknownMonsters = (TextView) view.findViewById(R.id.choose_sync_unknown_monster_encountred);
		if (result.isHasEncountredUnknownMonster()) {
			unknownMonsters.setVisibility(View.VISIBLE);
			unknownMonsters.setTextColor(Color.RED);
		} else {
			unknownMonsters.setVisibility(View.GONE);
		}

		final View userInfoBlockView = view.findViewById(R.id.choose_sync_userInfo_block);

		final SyncedUserInfoModel userInfoModel = result.getSyncedUserInfoToUpdate().getSyncedModel();
		if (userInfoModel.getCapturedInfo().equals(userInfoModel.getPadherderInfo())) {
			userInfoBlockView.setVisibility(View.GONE);
		} else {
			userInfoBlockView.setVisibility(View.VISIBLE);
			final CheckBox userInfoCheckBox = (CheckBox) view.findViewById(R.id.choose_sync_userInfo_checkbox);
			userInfoCheckBox.setChecked(result.getSyncedUserInfoToUpdate().isChoosen());
			userInfoCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "onClick");
					result.getSyncedUserInfoToUpdate().setChoosen(userInfoCheckBox.isChecked());
					updateCounts();
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

		syncButton = (Button) view.findViewById(R.id.choose_sync_button);

		updateCounts();

		return view;
	}

	private void updateCounts() {
		Log.d(getClass().getName(), "updateCounts");

		final SyncedUserInfoModel userInfoModel = result.getSyncedUserInfoToUpdate().getSyncedModel();
		final boolean hasUserInfoToUpdate = !userInfoModel.getCapturedInfo().equals(userInfoModel.getPadherderInfo());
		final boolean hasUserInfoToUpdateChosen = hasUserInfoToUpdate && result.getSyncedUserInfoToUpdate().isChoosen();
		final int materialToUpdateCount = result.getSyncedMaterialsToUpdate().size();
		final int materialToUpdateChosenCount = countChosenItems(result.getSyncedMaterialsToUpdate());
		final int monsterToUpdateCount = result.getSyncedMonstersToUpdate().size();
		final int monsterToUpdateChosenCount = countChosenItems(result.getSyncedMonstersToUpdate());
		final int monsterlToCreateCount = result.getSyncedMonstersToCreate().size();
		final int monsterlToCreateChosenCount = countChosenItems(result.getSyncedMonstersToCreate());
		final int monsterlToDeleteCount = result.getSyncedMonstersToDelete().size();
		final int monsterlToDeleteChosenCount = countChosenItems(result.getSyncedMonstersToDelete());

		summaryText.setText(getString(R.string.choose_sync_info_summary, materialToUpdateChosenCount, materialToUpdateCount,
				monsterToUpdateChosenCount, monsterToUpdateCount, monsterlToCreateChosenCount, monsterlToCreateCount,
				monsterlToDeleteChosenCount, monsterlToDeleteCount));

		if (materialToUpdateChosenCount > 0 || monsterToUpdateChosenCount > 0 || monsterlToCreateChosenCount > 0
				|| monsterlToDeleteChosenCount > 0 || hasUserInfoToUpdateChosen) {
			syncButton.setEnabled(true);
			syncButton.setText(R.string.choose_sync_info_button_enabled);
			syncButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "onClick");
					syncButton.setClickable(false);

					final Bundle bundle = new Bundle();
					bundle.putSerializable(PushSyncFragment.EXTRA_CHOOSE_SYNC_MODEL_NAME, result);
					bundle.putSerializable(PushSyncFragment.EXTRA_ACCOUNT_ID_NAME, accountId);
					((AbstractPADListenerActivity) getActivity()).goToScreen(GuiScreen.PUSH_SYNC, bundle);
				}
			});
		} else {
			syncButton.setEnabled(false);
			syncButton.setText(R.string.choose_sync_info_button_disabled);
		}
	}

	public void notifySelected() {
		Log.d(getClass().getName(), "notifySelected");
		if (result != null) {
			updateCounts();
		}
	}

	private <T extends Serializable> int countChosenItems(List<ChooseSyncModelContainer<T>> list) {
		int count = 0;
		for (final ChooseSyncModelContainer<?> item : list) {
			if (item.isChoosen()) {
				count++;
			}
		}
		return count;
	}

}
