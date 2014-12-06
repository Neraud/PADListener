package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Adapter to display the Ignored monsters for the List tab
 *
 * @author Neraud
 */
public class IgnoredMonsterAdapter extends ArrayAdapter<MonsterInfoModel> {

	private final Context mContext;
	private final ManageIgnoreListTaskFragment mTaskFragment;

	static class ViewHolder {

		@InjectView(R.id.manage_ignore_list_item_image_monster_image)
		ImageView monsterImageView;
		@InjectView(R.id.manage_ignore_list_item_image_remove_image)
		ImageView removeImageView;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public IgnoredMonsterAdapter(Context context, ManageIgnoreListTaskFragment taskFragment) {
		super(context, 0);
		mContext = context;
		mTaskFragment = taskFragment;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		MyLog.entry();

		final MonsterInfoModel model = super.getItem(position);

		final ViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_fragment_list_item, parent, false);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		new MonsterImageHelper(mContext).fillImage(viewHolder.monsterImageView, model);

		viewHolder.monsterImageView.clearColorFilter();

		viewHolder.removeImageView.setVisibility(View.GONE);

		viewHolder.monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry("idJP = " + model.getIdJP());
				if (viewHolder.removeImageView.getVisibility() == View.GONE) {
					viewHolder.removeImageView.setVisibility(View.VISIBLE);
					viewHolder.monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
				} else {
					viewHolder.removeImageView.setVisibility(View.GONE);
					viewHolder.monsterImageView.clearColorFilter();
				}
				MyLog.exit();
			}
		});

		viewHolder.removeImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry("idJP = " + model.getIdJP());
				mTaskFragment.removeIgnoredIds(model.getIdJP());
				MyLog.exit();
			}
		});

		MyLog.exit();
		return view;
	}
}
