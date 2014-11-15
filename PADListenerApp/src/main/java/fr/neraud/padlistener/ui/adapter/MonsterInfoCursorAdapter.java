package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Adapter to display the ViewMonsterInfo fragment for the Info tab
 *
 * @author Neraud
 */
public class MonsterInfoCursorAdapter extends SimpleCursorAdapter {

	private Context mContext;

	public MonsterInfoCursorAdapter(Context context) {
		super(context, R.layout.view_monster_info_item, null, new String[0], new int[0], 0);
		mContext = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);

		final ImageView monsterImageView = fillImage(view, model);
		monsterImageView.clearColorFilter();

		final ViewGroup monsterTextBlock = (ViewGroup) view.findViewById(R.id.card_monster_info_monster_text_block);
		monsterTextBlock.setVisibility(View.INVISIBLE);

		final TextView monsterIdText = (TextView) view.findViewById(R.id.card_monster_info_monster_text_id);
		monsterIdText.setText("" + model.getIdJP());

		final TextView monsterNameText = (TextView) view.findViewById(R.id.card_monster_info_monster_text_name);
		monsterNameText.setText(model.getName());

		monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterImageView.onClick :" + model.getIdJP());
				monsterTextBlock.setVisibility(View.VISIBLE);
				monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
			}
		});

		monsterTextBlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterTextBlock.onClick :" + model.getIdJP());
				monsterTextBlock.setVisibility(View.INVISIBLE);
				monsterImageView.clearColorFilter();
			}
		});
	}

	protected ImageView fillImage(View view, MonsterInfoModel model) {
		final ImageView monsterImageView = (ImageView) view.findViewById(R.id.card_monster_image);
		final String imageUrl = PadHerderDescriptor.serverUrl + model.getImage60Url();

		Picasso.with(mContext)
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		return monsterImageView;
	}
}
