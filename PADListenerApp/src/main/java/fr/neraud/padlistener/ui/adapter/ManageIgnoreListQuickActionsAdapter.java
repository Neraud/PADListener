package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.model.IgnoreMonsterQuickActionModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Created by Neraud on 15/11/2014.
 */
public class ManageIgnoreListQuickActionsAdapter extends ArrayAdapter<IgnoreMonsterQuickActionModel> {

	private final ManageIgnoreListTaskFragment mTaskFragment;
	private final MonsterImageHelper mImageHelper;

	static class ViewHolder {

		@InjectView(R.id.manage_ignore_list_quick_action_item_name)
		TextView nameText;
		@InjectView(R.id.manage_ignore_list_quick_action_item_remove_button)
		Button removeButton;
		@InjectView(R.id.manage_ignore_list_quick_action_item_add_button)
		Button addButton;

		@InjectViews({R.id.manage_ignore_list_quick_action_item_image_1,
				R.id.manage_ignore_list_quick_action_item_image_2,
				R.id.manage_ignore_list_quick_action_item_image_3,
				R.id.manage_ignore_list_quick_action_item_image_4,
				R.id.manage_ignore_list_quick_action_item_image_5})
		List<ImageView> imageViews;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public ManageIgnoreListQuickActionsAdapter(Context context, List<IgnoreMonsterQuickActionModel> ignoreMonsterQuickActionModels, ManageIgnoreListTaskFragment mTaskFragment) {
		super(context, 0, ignoreMonsterQuickActionModels);
		this.mTaskFragment = mTaskFragment;
		mImageHelper = new MonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		MyLog.entry();

		final IgnoreMonsterQuickActionModel item = super.getItem(position);
		MyLog.debug("item = " + item);

		final ViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_fragment_quick_actions_item, parent, false);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.nameText.setText(getContext().getString(R.string.manage_ignore_list_quick_action_name, item.getQuickActionName()));

		boolean hasAll = true;
		boolean hasNone = true;
		for (int i = 0; i <= 4; i++) {
			Integer monsterId = null;
			if (i < item.getMonsterIds().size()) {
				monsterId = item.getMonsterIds().get(i);
			}

			boolean alreadyIgnored = false;
			if (monsterId != null) {
				alreadyIgnored = mTaskFragment.getIgnoredIds().contains(monsterId);
				if (alreadyIgnored) {
					hasNone = false;
				} else {
					hasAll = false;
				}
			}

			bindOneImage(i, viewHolder.imageViews.get(i), monsterId, alreadyIgnored);
		}

		if (hasNone) {
			viewHolder.removeButton.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.removeButton.setVisibility(View.VISIBLE);
			viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();
					List<Integer> var = item.getMonsterIds();
					mTaskFragment.removeIgnoredIds(var.toArray(new Integer[var.size()]));
					MyLog.exit();
				}
			});
		}
		if (hasAll) {
			viewHolder.addButton.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.addButton.setVisibility(View.VISIBLE);
			viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();
					final List<Integer> monsterIds = item.getMonsterIds();
					mTaskFragment.addIgnoredIds(monsterIds.toArray(new Integer[monsterIds.size()]));
					MyLog.exit();
				}
			});
		}
		MyLog.exit();
		return view;
	}

	private void bindOneImage(int position, ImageView monsterImageView, Integer monsterId, boolean alreadyIgnored) {
		MyLog.entry("position = " + position);

		monsterImageView.clearColorFilter();

		if (monsterId != null && mTaskFragment.getMonsterInfoHelper() != null) {
			monsterImageView.setVisibility(View.VISIBLE);
			try {
				final MonsterInfoModel monsterInfo = mTaskFragment.getMonsterInfoHelper().getMonsterInfo(monsterId);
				mImageHelper.fillImage(monsterImageView, monsterInfo);

				if (!alreadyIgnored) {
					monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
				}
			} catch (UnknownMonsterException e) {
				Picasso.with(getContext())
						.load(R.drawable.no_monster_image)
						.into(monsterImageView);
			}
		} else {
			MyLog.debug("no monster at " + position + ", ignored");
			monsterImageView.setVisibility(View.INVISIBLE);
		}
		MyLog.exit();
	}
}
