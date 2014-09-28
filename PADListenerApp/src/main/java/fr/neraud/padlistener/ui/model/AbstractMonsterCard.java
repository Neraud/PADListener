package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.MonsterInfoModel;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Neraud on 27/09/2014.
 */
public abstract class AbstractMonsterCard extends Card {

	protected MonsterInfoModel mMonsterInfoModel;

	protected AbstractMonsterCard(Context context, MonsterInfoModel monsterInfoModel, int cardLayout) {
		super(context, cardLayout);
		mMonsterInfoModel = monsterInfoModel;
		init();
	}

	protected void init() {
		setShadow(false);
	}

	protected ImageView fillImage(View view) {
		final ImageView monsterImageView = (ImageView) view.findViewById(R.id.card_monster_image);
		final String imageUrl = PadHerderDescriptor.serverUrl + mMonsterInfoModel.getImage60Url();

		Picasso.with(getContext())
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		return monsterImageView;
	}

}
