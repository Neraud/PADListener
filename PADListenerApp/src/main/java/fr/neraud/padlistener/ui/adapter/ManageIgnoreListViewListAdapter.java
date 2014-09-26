package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Created by Neraud on 16/08/2014.
 */
public class ManageIgnoreListViewListAdapter extends ArrayAdapter<MonsterInfoModel> {

	private final ManageIgnoreListTaskFragment taskFragment;
	private final MonsterImageHelper imageHelper;

	public ManageIgnoreListViewListAdapter(Context context, ManageIgnoreListTaskFragment taskFragment) {
		super(context, R.layout.manage_ignore_list_view_list_item);
		this.taskFragment = taskFragment;
		imageHelper = new MonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");

		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_view_list_item, parent, false);
		}

		final MonsterInfoModel item = super.getItem(position);

		imageHelper.fillMonsterImage((ImageView) view.findViewById(R.id.manage_ignore_list_view_list_item_image), item.getIdJP());

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
