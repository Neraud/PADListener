package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

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

	public ManageIgnoreListQuickActionsAdapter(Context context, List<IgnoreMonsterQuickActionModel> ignoreMonsterQuickActionModels, ManageIgnoreListTaskFragment mTaskFragment) {
		super(context, 0, ignoreMonsterQuickActionModels);
		this.mTaskFragment = mTaskFragment;
		mImageHelper = new MonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_fragment_quick_actions_item, parent, false);
		}
		final IgnoreMonsterQuickActionModel item = super.getItem(position);
		Log.d(getClass().getName(), "getView : " + item);

		final TextView nameText = (TextView) view.findViewById(R.id.manage_ignore_list_quick_action_item_name);
		nameText.setText(getContext().getString(R.string.manage_ignore_list_quick_action_name, item.getQuickActionName()));

		final Integer[] imageIds = {R.id.manage_ignore_list_quick_action_item_image_1, R.id.manage_ignore_list_quick_action_item_image_2, R.id.manage_ignore_list_quick_action_item_image_3, R.id.manage_ignore_list_quick_action_item_image_4, R.id.manage_ignore_list_quick_action_item_image_5};

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

			bindOneImage(view, i, monsterId, alreadyIgnored, imageIds[i]);
		}

		final Button removeButton = (Button) view.findViewById(R.id.manage_ignore_list_quick_action_item_remove_button);
		if (hasNone) {
			removeButton.setVisibility(View.INVISIBLE);
		} else {
			removeButton.setVisibility(View.VISIBLE);
			removeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "removeButton.onClick");
					List<Integer> var = item.getMonsterIds();
					mTaskFragment.removeIgnoredIds(var.toArray(new Integer[var.size()]));
				}
			});
		}
		final Button addButton = (Button) view.findViewById(R.id.manage_ignore_list_quick_action_item_add_button);
		if (hasAll) {
			addButton.setVisibility(View.INVISIBLE);
		} else {
			addButton.setVisibility(View.VISIBLE);
			addButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "addButton.onClick");
					final List<Integer> monsterIds = item.getMonsterIds();
					mTaskFragment.addIgnoredIds(monsterIds.toArray(new Integer[monsterIds.size()]));
				}
			});
		}
		return view;
	}

	private void bindOneImage(View view, int position, Integer monsterId, boolean alreadyIgnored, int imageId) {
		Log.d(getClass().getName(), "bindOneImage" + position);

		final ImageView monsterImageView = (ImageView) view.findViewById(imageId);
		monsterImageView.clearColorFilter();

		if (monsterId != null && mTaskFragment.getMonsterInfoHelper() != null) {
			monsterImageView.setVisibility(View.VISIBLE);
			try {
				final MonsterInfoModel monsterInfo = mTaskFragment.getMonsterInfoHelper().getMonsterInfo(monsterId);
				mImageHelper.fillImage(monsterImageView, monsterInfo);

				if(!alreadyIgnored) {
					monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
				}
			} catch(UnknownMonsterException e) {
				Picasso.with(getContext())
						.load(R.drawable.no_monster_image)
						.into(monsterImageView);
			}
		} else {
			Log.d(getClass().getName(), "bindOneImage : no monster at " + position + ", ignored");
			monsterImageView.setVisibility(View.INVISIBLE);
		}
	}
}
