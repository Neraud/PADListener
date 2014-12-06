package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Adapter to display the ViewMonsterInfo fragment for the Info tab
 *
 * @author Neraud
 */
public class MonsterInfoCursorAdapter extends AbstractMonsterCursorAdapter {

	static class ViewHolder {

		@InjectView(R.id.view_monster_info_item_image)
		ImageView monsterImageView;
		@InjectView(R.id.view_monster_info_item_text_block)
		ViewGroup monsterTextBlock;
		@InjectView(R.id.view_monster_info_item_text_id)
		TextView monsterIdText;
		@InjectView(R.id.view_monster_info_item_text_name)
		TextView monsterNameText;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public MonsterInfoCursorAdapter(Context context) {
		super(context, R.layout.view_monster_info_item);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		MyLog.entry();

		final ViewHolder viewHolder = new ViewHolder(view);
		final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);

		fillImage(viewHolder.monsterImageView, model);
		viewHolder.monsterImageView.clearColorFilter();
		viewHolder.monsterTextBlock.setVisibility(View.INVISIBLE);
		viewHolder.monsterIdText.setText("" + model.getIdJP());
		viewHolder.monsterNameText.setText(model.getName());

		viewHolder.monsterImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry("idJP = " + model.getIdJP());
				viewHolder.	monsterTextBlock.setVisibility(View.VISIBLE);
				viewHolder.monsterImageView.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.DARKEN);
				MyLog.exit();
			}
		});

		viewHolder.monsterTextBlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry("idJP = " + model.getIdJP());
				viewHolder.monsterTextBlock.setVisibility(View.INVISIBLE);
				viewHolder.monsterImageView.clearColorFilter();
				MyLog.exit();
			}
		});

		MyLog.exit();
	}
}
