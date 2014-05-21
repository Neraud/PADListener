
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterHelper;

public class CapturedPlayerMonsterCursorAdapter extends SimpleCursorAdapter {

	public CapturedPlayerMonsterCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final CapturedMonsterFullInfoModel model = CapturedPlayerMonsterHelper.cursorWithInfoToModel(cursor);

		final CapturedMonsterCardModel capturedMonster = model.getCapturedMonster();
		final MonsterInfoModel monsterInfo = model.getMonsterInfo();

		try {
			final InputStream is = context.getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(monsterInfo.getId()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			((ImageView) view.findViewById(R.id.view_captured_data_monster_item_image)).setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//(%1$d) %2$s (lv %3$d, %4$d xp)
		final String lineName = context.getString(R.string.view_captured_monster_item_name, capturedMonster.getId(),
		        monsterInfo.getName(), capturedMonster.getLevel(), capturedMonster.getExp());
		((TextView) view.findViewById(R.id.view_captured_data_monster_item_name)).setText(lineName);

		final String linePlus = context.getString(R.string.view_captured_monster_item_plus, capturedMonster.getPlusHp(),
		        capturedMonster.getPlusAtk(), capturedMonster.getPlusRcv(), capturedMonster.getAwakenings());
		((TextView) view.findViewById(R.id.view_captured_data_monster_item_plus)).setText(linePlus);
	}

}
