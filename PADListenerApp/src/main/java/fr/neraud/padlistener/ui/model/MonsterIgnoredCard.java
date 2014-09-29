package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ManageIgnoreListTaskFragment;

/**
 * Created by Neraud on 27/09/2014.
 */
public class MonsterIgnoredCard extends AbstractMonsterCard {

	private MonsterInfoModel mModel;
	private ManageIgnoreListTaskFragment mTaskFragment;

	public MonsterIgnoredCard(Context context, MonsterInfoModel model, ManageIgnoreListTaskFragment taskFragment) {
		super(context, model, R.layout.card_monster_ignored);
		mModel = model;
		mTaskFragment = taskFragment;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;

		final ImageView monsterImageView = fillImage(view);
		monsterImageView.clearColorFilter();

		final ImageView removeImageView = (ImageView) view.findViewById(R.id.card_monster_ignored_remove_image);
		removeImageView.setVisibility(View.GONE);

		monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterImageView.onClick :" + mModel.getIdJP());
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
				Log.d(getClass().getName(), "removeImageView.onClick :" + mModel.getIdJP());
				mTaskFragment.removeIgnoredIds(mModel.getIdJP());
			}
		});
	}
}
