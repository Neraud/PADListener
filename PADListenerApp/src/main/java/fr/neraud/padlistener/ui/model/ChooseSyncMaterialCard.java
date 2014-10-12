package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;

/**
 * Created by Neraud on 12/10/2014.
 */
public class ChooseSyncMaterialCard extends AbstractMonsterCard {

	private ChooseSyncModelContainer<SyncedMaterialModel> mItem;

	public ChooseSyncMaterialCard(Context context, ChooseSyncModelContainer<SyncedMaterialModel> item) {
		super(context, item.getSyncedModel().getDisplayedMonsterInfo(), R.layout.card_choose_sync_material);
		this.mItem = item;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.card_choose_sync_material_checkbox);
		checkBox.setChecked(mItem.isChosen());
		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				mItem.setChosen(!mItem.isChosen());
			}
		});

		final ImageView imageView = fillImage(view);

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				mItem.setChosen(!mItem.isChosen());
				checkBox.setChecked(mItem.isChosen());
			}
		});

		final TextView nameText = (TextView) view.findViewById(R.id.card_choose_sync_materials_name);
		nameText.setText(getContext().getString(R.string.choose_sync_materials_name,
				mItem.getSyncedModel().getDisplayedMonsterInfo().getIdJP(),
				mItem.getSyncedModel().getDisplayedMonsterInfo().getName()));

		final TextView quantitiesText = (TextView) view.findViewById(R.id.card_choose_sync_materials_quantities);
		quantitiesText.setText(getContext().getString(R.string.choose_sync_materials_quantities,
				mItem.getSyncedModel().getPadherderInfo(), mItem.getSyncedModel().getCapturedInfo()));

		if (mItem.getSyncedModel().getPadherderInfo() < mItem.getSyncedModel().getCapturedInfo()) {
			quantitiesText.setTextColor(Color.GREEN);
		} else if (mItem.getSyncedModel().getPadherderInfo() > mItem.getSyncedModel().getCapturedInfo()) {
			quantitiesText.setTextColor(Color.RED);
		} else {
			// Shouldn't happen
			//quantitiesText.setTextColor(mDefaultTextColor);
		}
	}
}
