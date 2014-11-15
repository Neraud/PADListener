package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.ui.helper.NewMonsterImageHelper;

/**
 * Adaptor to display the choose sync materials
 *
 * @author Neraud
 */
public class ChooseSyncMaterialsAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMaterialModel>> {

	private Integer mDefaultTextColor = null;
	private NewMonsterImageHelper mImageHelper;

	public ChooseSyncMaterialsAdapter(Context context, List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		super(context, 0, syncedMaterialsToUpdate);
		mImageHelper = new NewMonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		final ChooseSyncModelContainer<SyncedMaterialModel> item = super.getItem(position);

		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_materials, null);
			mDefaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_materials_item_quantities)).getTextColors()
					.getDefaultColor();
		}

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_materials_item_checkbox);
		checkBox.setChecked(item.isChosen());
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				item.setChosen(!item.isChosen());
			}
		});

		final ImageView image = mImageHelper.fillImage(view, R.id.choose_sync_materials_item_image, item.getSyncedModel().getDisplayedMonsterInfo());
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				item.setChosen(!item.isChosen());
				checkBox.setChecked(item.isChosen());
			}
		});

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_materials_item_name);
		nameText.setText(getContext().getString(R.string.choose_sync_materials_item_name,
				item.getSyncedModel().getDisplayedMonsterInfo().getIdJP(),
				item.getSyncedModel().getDisplayedMonsterInfo().getName()));

		final TextView quantitiesText = (TextView) view.findViewById(R.id.choose_sync_materials_item_quantities);
		quantitiesText.setText(getContext().getString(R.string.choose_sync_materials_item_quantities,
				item.getSyncedModel().getPadherderInfo(), item.getSyncedModel().getCapturedInfo()));

		if (item.getSyncedModel().getPadherderInfo() < item.getSyncedModel().getCapturedInfo()) {
			quantitiesText.setTextColor(Color.GREEN);
		} else if (item.getSyncedModel().getPadherderInfo() > item.getSyncedModel().getCapturedInfo()) {
			quantitiesText.setTextColor(Color.RED);
		} else {
			// Shouldn't happen
			quantitiesText.setTextColor(mDefaultTextColor);
		}

		return view;
	}
}