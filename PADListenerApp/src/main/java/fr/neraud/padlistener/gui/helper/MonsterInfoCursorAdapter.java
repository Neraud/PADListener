package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;

/**
 * Adapter to display the ViewMonsterInfo fragment for the Info tab
 *
 * @author Neraud
 */
public class MonsterInfoCursorAdapter extends SimpleCursorAdapter {

	private final MonsterImageHelper imageHelper;

	public MonsterInfoCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
		imageHelper = new MonsterImageHelper(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final MonsterInfoModel model = MonsterInfoProviderHelper.cursorToModel(cursor);

		final String lineName = context.getString(R.string.view_monster_info_name, model.getIdJP(),
				model.getName());
		((TextView) view.findViewById(R.id.view_monster_info_item_name)).setText(lineName);

		final TextView evoTextView = (TextView) view.findViewById(R.id.view_monster_info_item_evo);
		if (model.getBaseMonsterId() == model.getIdJP()) {
			evoTextView.setVisibility(View.GONE);
		} else {
			final String lineEvo = context.getString(R.string.view_monster_info_evo, model.getBaseMonsterId(), model.getEvolutionStage());
			evoTextView.setText(lineEvo);
			evoTextView.setVisibility(View.VISIBLE);
		}

		imageHelper.fillMonsterImage((ImageView) view.findViewById(R.id.view_monster_info_item_image), model.getIdJP());
	}
}
