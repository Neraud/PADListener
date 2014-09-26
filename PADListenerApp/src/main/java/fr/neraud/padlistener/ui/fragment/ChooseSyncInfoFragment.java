package fr.neraud.padlistener.ui.fragment;

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
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedUserInfoModel;
import fr.neraud.padlistener.ui.activity.AbstractPADListenerActivity;
import fr.neraud.padlistener.ui.constant.UiScreen;
import fr.neraud.padlistener.ui.helper.ChooseSyncDataPagerHelper;

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

		final TextView unknownMonsters = (TextView) view.findViewById(R.id.choose_sync_unknown_monster_encountered);
		if (result.isHasEncounteredUnknownMonster()) {
			unknownMonsters.setVisibility(View.VISIBLE);
			unknownMonsters.setTextColor(Color.RED);
		} else {
			unknownMonsters.setVisibility(View.GONE);
		}

		final View userInfoBlockView = view.findViewById(R.id.choose_sync_userInfo_block);

		final SyncedUserInfoModel userInfoModel = result.getSyncedUserInfoToUpdate().getSyncedModel();
		if (userInfoModel.hasDataToSync()) {
			userInfoBlockView.setVisibility(View.VISIBLE);
			final CheckBox userInfoCheckBox = (CheckBox) view.findViewById(R.id.choose_sync_userInfo_checkbox);
			userInfoCheckBox.setChecked(result.getSyncedUserInfoToUpdate().isChosen());
			userInfoCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "onClick");
					result.getSyncedUserInfoToUpdate().setChosen(userInfoCheckBox.isChecked());
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
		} else {
			userInfoBlockView.setVisibility(View.GONE);
		}

		syncButton = (Button) view.findViewById(R.id.choose_sync_button);

		updateCounts();

		return view;
	}

	private void updateCounts() {
		Log.d(getClass().getName(), "updateCounts");

		final boolean hasUserInfoToUpdate = result.getSyncedUserInfoToUpdate().getSyncedModel().hasDataToSync();
		final boolean hasUserInfoToUpdateChosen = hasUserInfoToUpdate && result.getSyncedUserInfoToUpdate().isChosen();
		final int materialToUpdateCount = result.getSyncedMaterialsToUpdate().size();
		final int materialToUpdateChosenCount = countChosenItems(result.getSyncedMaterialsToUpdate());
		final int monsterToUpdateCount = result.getSyncedMonsters(SyncMode.UPDATED).size();
		final int monsterToUpdateChosenCount = countChosenItems(result.getSyncedMonsters(SyncMode.UPDATED));
		final int monsterToCreateCount = result.getSyncedMonsters(SyncMode.CREATED).size();
		final int monsterToCreateChosenCount = countChosenItems(result.getSyncedMonsters(SyncMode.CREATED));
		final int monsterToDeleteCount = result.getSyncedMonsters(SyncMode.DELETED).size();
		final int monsterToDeleteChosenCount = countChosenItems(result.getSyncedMonsters(SyncMode.DELETED));

		summaryText.setText(getString(R.string.choose_sync_info_summary, materialToUpdateChosenCount, materialToUpdateCount,
				monsterToUpdateChosenCount, monsterToUpdateCount, monsterToCreateChosenCount, monsterToCreateCount,
				monsterToDeleteChosenCount, monsterToDeleteCount));

		if (materialToUpdateChosenCount > 0 || monsterToUpdateChosenCount > 0 || monsterToCreateChosenCount > 0
				|| monsterToDeleteChosenCount > 0 || hasUserInfoToUpdateChosen) {
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
					((AbstractPADListenerActivity) getActivity()).goToScreen(UiScreen.PUSH_SYNC, bundle);
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
			if (item.isChosen()) {
				count++;
			}
		}
		return count;
	}

}
