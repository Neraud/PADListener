
package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.MonsterCardModel;
import fr.neraud.padlistener.provider.helper.PlayerMonsterHelper;

public class PlayerMonsterCursorAdapter extends SimpleCursorAdapter {

	public PlayerMonsterCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final MonsterCardModel model = PlayerMonsterHelper.cursorToModel(cursor);

		final String lineName = context.getString(R.string.view_captured_monster_item_name, model.getId(), model.getLevel());
		((TextView) view.findViewById(R.id.view_captured_data_monster_item_name)).setText(lineName);

		final String linePlus = context.getString(R.string.view_captured_monster_item_plus, model.getPlusHp(), model.getPlusAtk(),
		        model.getPlusRcv(), model.getAwakenings());
		((TextView) view.findViewById(R.id.view_captured_data_monster_item_plus)).setText(linePlus);
	}

}
