
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

public class ChooseSyncMonstersAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMonsterModel>> {

	private final int layout;

	public ChooseSyncMonstersAdapter(Context context, int layout,
	        List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate) {
		super(context, layout, 0, syncedMonstersToUpdate);
		this.layout = layout;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		final ChooseSyncModelContainer<SyncedMonsterModel> item = super.getItem(position);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout, null);
		}

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_monsters_item_checkbox);
		checkBox.setChecked(item.isChoosen());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.d(getClass().getName(), "onCheckedChanged");
				item.setChoosen(isChecked);
			}
		});

		final ImageView image = (ImageView) view.findViewById(R.id.choose_sync_monsters_item_image);
		try {
			final InputStream is = getContext().getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(item.getSyncedModel().getMonsterInfo().getId()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			image.setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			image.setImageResource(R.drawable.no_monster_image);
		}
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				checkBox.setChecked(!checkBox.isChecked());
			}
		});

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_name);
		nameText.setText(getContext().getString(R.string.choose_sync_monsters_item_name,
		        item.getSyncedModel().getMonsterInfo().getName()));

		return view;
	}
}
