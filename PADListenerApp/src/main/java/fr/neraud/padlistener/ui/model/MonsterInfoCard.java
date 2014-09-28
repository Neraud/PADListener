package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.MonsterInfoModel;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Neraud on 27/09/2014.
 */
public class MonsterInfoCard extends Card {

	private MonsterInfoModel mModel;

	public MonsterInfoCard(Context context, MonsterInfoModel model) {
		super(context, R.layout.card_monster_info);
		mModel = model;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;

		final ImageView monsterImageView = (ImageView) view.findViewById(R.id.card_monster_info_monster_image);
		monsterImageView.clearColorFilter();
		final String imageUrl = PadHerderDescriptor.serverUrl + mModel.getImage60Url();

		Picasso.with(getContext())
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

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
