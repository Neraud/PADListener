
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * Adaptor to display the choose sync materials
 * 
 * @author Neraud
 */
public class ChooseSyncMaterialsAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMaterialModel>> {

	private final int layout;
	private Integer defaultTextColor = null;

	public ChooseSyncMaterialsAdapter(Context context, int layout,
	        List<ChooseSyncModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		super(context, layout, 0, syncedMaterialsToUpdate);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getContext());
		final ChooseSyncModelContainer<SyncedMaterialModel> item = super.getItem(position);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout, null);
			defaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_materials_item_quantities)).getTextColors()
			        .getDefaultColor();
		}

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_materials_item_checkbox);
		checkBox.setChecked(item.isChoosen());
		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				item.setChoosen(!item.isChoosen());
			}
		});

		final ImageView image = (ImageView) view.findViewById(R.id.choose_sync_materials_item_image);
		try {
			final InputStream is = getContext().getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(item.getSyncedModel().getMonsterInfo().getIdJP()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			image.setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			image.setImageResource(R.drawable.no_monster_image);
		}
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				item.setChoosen(!item.isChoosen());
				checkBox.setChecked(item.isChoosen());
			}
		});

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_materials_item_name);
		nameText.setText(getContext().getString(R.string.choose_sync_materials_item_name,
		        item.getSyncedModel().getMonsterInfo().getId(prefHelper.getPlayerRegion()),
		        item.getSyncedModel().getMonsterInfo().getName()));

		final TextView quantitiesText = (TextView) view.findViewById(R.id.choose_sync_materials_item_quantities);
		quantitiesText.setText(getContext().getString(R.string.choose_sync_materials_item_quantities,
		        item.getSyncedModel().getPadherderInfo(), item.getSyncedModel().getCapturedInfo()));

		if (item.getSyncedModel().getPadherderInfo() < item.getSyncedModel().getCapturedInfo()) {
			quantitiesText.setTextColor(Color.GREEN);
		} else if (item.getSyncedModel().getPadherderInfo() > item.getSyncedModel().getCapturedInfo()) {
			quantitiesText.setTextColor(Color.RED);
		} else {
			// Shouldn't happen
			quantitiesText.setTextColor(defaultTextColor);
		}

		return view;
	}
}
