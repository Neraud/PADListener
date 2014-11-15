package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.ui.helper.NewMonsterImageHelper;

/**
 * Adapter to display the Ignored monsters for the List tab
 *
 * @author Neraud
 */
public class IgnoredMonsterAdapter extends ArrayAdapter<MonsterInfoModel> {

	private final Context mContext;
	private final ManageIgnoreListTaskFragment mTaskFragment;

	public IgnoredMonsterAdapter(Context context, ManageIgnoreListTaskFragment taskFragment) {
		super(context, 0);
		mContext = context;
		mTaskFragment = taskFragment;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_fragment_list_item, parent, false);
		}
		final MonsterInfoModel model = super.getItem(position);

		final ImageView monsterImageView = fillImage(view, R.id.manage_ignore_list_item_image_monster_image, model);
		monsterImageView.clearColorFilter();

		final ImageView removeImageView = (ImageView) view.findViewById(R.id.manage_ignore_list_item_image_remove_image);
		removeImageView.setVisibility(View.GONE);

		monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterImageView.onClick :" + model.getIdJP());
				if(removeImageView.getVisibility() == View.GONE) {
					removeImageView.setVisibility(View.VISIBLE);
					monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
				} else {
					removeImageView.setVisibility(View.GONE);
					monsterImageView.clearColorFilter();
				}
			}
		});

		removeImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "removeImageView.onClick :" + model.getIdJP());
				mTaskFragment.removeIgnoredIds(model.getIdJP());
			}
		});

		return view;
	}

	protected ImageView fillImage(View view, int imageViewId, MonsterInfoModel model) {
		return new NewMonsterImageHelper(mContext).fillImage(view, imageViewId, model);
	}
}
