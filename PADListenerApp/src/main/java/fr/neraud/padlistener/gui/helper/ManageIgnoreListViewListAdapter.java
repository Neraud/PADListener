package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * Created by Neraud on 16/08/2014.
 */
public class ManageIgnoreListViewListAdapter extends ArrayAdapter<MonsterInfoModel> {

	private final ManageIgnoreListTaskFragment taskFragment;

	public ManageIgnoreListViewListAdapter(Context context, ManageIgnoreListTaskFragment taskFragment) {
		super(context, R.layout.manage_ignore_list_view_list_item);
		this.taskFragment = taskFragment;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");

		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_view_list_item, parent, false);
		}

		final MonsterInfoModel item = super.getItem(position);

		final ImageView image = (ImageView) view.findViewById(R.id.manage_ignore_list_view_list_item_image);
		try {
			final InputStream is = getContext().getContentResolver().openInputStream(
					MonsterInfoDescriptor.UriHelper.uriForImage(item.getIdJP()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			image.setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			image.setImageResource(R.drawable.no_monster_image);
		}

		final Button removeButton = (Button) view.findViewById(R.id.manage_ignore_list_view_list_item_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "removeButton.onClick");
				taskFragment.removeIgnoredIds(item.getIdJP());
			}
		});


		final TextView nameText = (TextView) view.findViewById(R.id.manage_ignore_list_view_list_item_name);
		nameText.setText(getContext().getString(R.string.manage_ignore_list_view_list_name,
				item.getIdJP(),
				item.getName()));

		return view;
	}

}
