package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterProviderHelper;

/**
 * Adapter to display the ViewcapturedData monsters
 *
 * @author Neraud
 */
public class CapturedMonsterCursorAdapter extends AbstractMonsterWithStatsCursorAdapter {

	public CapturedMonsterCursorAdapter(FragmentActivity context) {
		super(context, R.layout.view_captured_data_fragment_monsters_item);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final CapturedMonsterFullInfoModel model = CapturedPlayerMonsterProviderHelper.cursorWithInfoToModel(cursor);
		final MonsterModel monsterModel = model.getCapturedMonster();
		final MonsterInfoModel monsterInfoModel = model.getMonsterInfo();

		fillImage(view, R.id.view_captured_monsters_item_image, monsterInfoModel);

		fillTextView(view, R.id.view_captured_monsters_item_awakenings, monsterModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(view, R.id.view_captured_monsters_item_level, monsterModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(view, R.id.view_captured_monsters_item_skill_level, monsterModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterModel.getPlusHp() + monsterModel.getPlusAtk() + monsterModel.getPlusRcv();
		fillTextView(view, R.id.view_captured_monsters_item_pluses, totalPluses, 3 * 99);
		addDetailDialog(view, monsterInfoModel, monsterModel);
	}

}
