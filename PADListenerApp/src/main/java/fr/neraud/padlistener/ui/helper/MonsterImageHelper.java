package fr.neraud.padlistener.ui.helper;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Helper to retrieve a monster image
 *
 * Created by Neraud on 15/11/2015.
 */
public class MonsterImageHelper {


	private final Context mContext;

	public MonsterImageHelper(Context context) {
		this.mContext = context;
	}

	/**
	 * Fill the imageView with the monster image
	 *
	 * @param monsterImageView the ImageView
	 * @param model the MonsterInfoModel
	 */
	public void fillImage(ImageView monsterImageView, MonsterInfoModel model) {
		final String imageUrl = PadHerderDescriptor.serverUrl + model.getImage60Url();

		Picasso.with(mContext)
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);
	}
}
