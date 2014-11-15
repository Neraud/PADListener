package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Base Adapter to display monsters
 *
 * @author Neraud
 */
public abstract class AbstractMonsterCursorAdapter extends SimpleCursorAdapter {

	private Context mContext;

	protected AbstractMonsterCursorAdapter(Context context, int layoutId) {
		super(context, layoutId, null, new String[0], new int[0], 0);
		mContext = context;
	}

	protected ImageView fillImage(View view, int imageViewId, MonsterInfoModel model) {
		final ImageView monsterImageView = (ImageView) view.findViewById(imageViewId);
		final String imageUrl = PadHerderDescriptor.serverUrl + model.getImage60Url();

		Picasso.with(mContext)
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		return monsterImageView;
	}
}
