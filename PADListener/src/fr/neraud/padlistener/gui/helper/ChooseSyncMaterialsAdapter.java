
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

public class ChooseSyncMaterialsAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMaterialModel>> {

	private final int layout;

	public ChooseSyncMaterialsAdapter(Context context, int layout,
	        List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		super(context, layout, 0, syncedMaterialsToUpdate);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		final ChooseSyncModelContainer<SyncedMaterialModel> item = super.getItem(position);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout, null);
		}

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_materials_item_checkbox);
		checkBox.setChecked(item.isChoosen());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.d(getClass().getName(), "onCheckedChanged");
				item.setChoosen(isChecked);
			}
		});

		try {
			final InputStream is = getContext().getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(item.getSyncedModel().getMonsterInfo().getId()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			((ImageView) view.findViewById(R.id.choose_sync_materials_item_image)).setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			((ImageView) view.findViewById(R.id.choose_sync_materials_item_image)).setImageResource(R.drawable.no_monster_image);
		}

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_materials_item_name);
		nameText.setText(getContext().getString(R.string.choose_sync_materials_item_name,
		        item.getSyncedModel().getMonsterInfo().getName()));

		final TextView quantitiesText = (TextView) view.findViewById(R.id.choose_sync_materials_item_quantities);
		quantitiesText.setText(getContext().getString(R.string.choose_sync_materials_item_quantities,
		        item.getSyncedModel().getPadherderQuantity(), item.getSyncedModel().getCapturedQuantity()));

		return view;
	}
}
