package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Created by Neraud on 27/09/2014.
 */
public class MonsterInfoCard extends AbstractMonsterCard {

	private MonsterInfoModel mModel;

	public MonsterInfoCard(Context context, MonsterInfoModel model) {
		super(context, model, R.layout.card_monster_info);
		mModel = model;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;

		final ImageView monsterImageView = fillImage(view);
		monsterImageView.clearColorFilter();

		final ViewGroup monsterTextBlock = (ViewGroup) view.findViewById(R.id.card_monster_info_monster_text_block);
		monsterTextBlock.setVisibility(View.INVISIBLE);

		final TextView monsterIdText = (TextView) view.findViewById(R.id.card_monster_info_monster_text_id);
		monsterIdText.setText("" + mModel.getIdJP());

		final TextView monsterNameText = (TextView) view.findViewById(R.id.card_monster_info_monster_text_name);
		monsterNameText.setText(mModel.getName());

		monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterImageView.onClick :" + mModel.getIdJP());
				monsterTextBlock.setVisibility(View.VISIBLE);
				monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
			}
		});

		monsterTextBlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterTextBlock.onClick :" + mModel.getIdJP());
				monsterTextBlock.setVisibility(View.INVISIBLE);
				monsterImageView.clearColorFilter();
			}
		});
	}
}
