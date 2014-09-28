package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.IgnoreMonsterQuickActionModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by Neraud on 28/09/2014.
 */
public class IgnoreListQuickActionCard extends Card {

	private ManageIgnoreListTaskFragment mTaskFragment;
	private final IgnoreMonsterQuickActionModel mModel;

	public IgnoreListQuickActionCard(Context context, ManageIgnoreListTaskFragment taskFragment, IgnoreMonsterQuickActionModel model) {
		super(context, R.layout.card_manage_ignore_list_quick_action);
		mTaskFragment = taskFragment;
		mModel = model;
		init();
	}

	private void init() {
		CardHeader header = new CardHeader(getContext());
		header.setTitle(mModel.getQuickActionName());
		addCardHeader(header);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		final Integer[] imageIds = {R.id.manage_ignore_list_quick_action_item_image_1, R.id.manage_ignore_list_quick_action_item_image_2, R.id.manage_ignore_list_quick_action_item_image_3, R.id.manage_ignore_list_quick_action_item_image_4, R.id.manage_ignore_list_quick_action_item_image_5};

		boolean hasAll = true;
		boolean hasNone = true;
		for (int i = 0; i <= 4; i++) {
			Integer monsterId = null;
			if (i < mModel.getMonsterIds().size()) {
				monsterId = mModel.getMonsterIds().get(i);
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
					List<Integer> var = mModel.getMonsterIds();
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
					final List<Integer> monsterIds = mModel.getMonsterIds();
					mTaskFragment.addIgnoredIds(monsterIds.toArray(new Integer[monsterIds.size()]));
				}
			});
		}
	}

	private void bindOneImage(View view, int position, Integer monsterId, boolean alreadyIgnored, int imageId) {
		Log.d(getClass().getName(), "bindOneImage" + position);
		final ImageView monsterImageView = (ImageView) view.findViewById(imageId);
		monsterImageView.clearColorFilter();
		if (monsterId != null && mTaskFragment.getMonsterInfoHelper() != null) {
			monsterImageView.setVisibility(View.VISIBLE);
			try {
				final MonsterInfoModel monsterInfo = mTaskFragment.getMonsterInfoHelper().getMonsterInfo(monsterId);
				final String imageUrl = PadHerderDescriptor.serverUrl + monsterInfo.getImage60Url();

				Picasso.with(getContext())
						.load(imageUrl)
						.error(R.drawable.no_monster_image)
						.into(monsterImageView);

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
