package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerMonsterProviderHelper;
import fr.neraud.padlistener.ui.fragment.ViewCapturedDataMonsterDetailDialogFragment;

/**
 * Adapter to display the ViewcapturedData monsters
 *
 * @author Neraud
 */
public class CapturedMonsterCursorAdapter extends AbstractMonsterCursorAdapter {

	private FragmentActivity mActivity;

	public CapturedMonsterCursorAdapter(FragmentActivity context) {
		super(context, R.layout.view_captured_data_fragment_monsters_item);
		mActivity = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final CapturedMonsterFullInfoModel model = CapturedPlayerMonsterProviderHelper.cursorWithInfoToModel(cursor);
		final MonsterModel monsterModel = model.getCapturedMonster();
		final MonsterInfoModel monsterInfoModel = model.getMonsterInfo();

		fillImage(view, monsterInfoModel);

		fillTextView(view, R.id.view_captured_monsters_item_awakenings, monsterModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(view, R.id.view_captured_monsters_item_level, monsterModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(view, R.id.view_captured_monsters_item_skill_level, monsterModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterModel.getPlusHp() + monsterModel.getPlusAtk() + monsterModel.getPlusRcv();
		fillTextView(view, R.id.view_captured_monsters_item_pluses, totalPluses, 3 * 99);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "onClick");
				final ViewCapturedDataMonsterDetailDialogFragment fragment = new ViewCapturedDataMonsterDetailDialogFragment();
				fragment.setMonsterInfoModel(monsterInfoModel);
				fragment.setMonsterStatsModel(monsterModel);
				fragment.show(mActivity.getSupportFragmentManager(), "view_captured_data_monster_detail");
			}
		});
	}

	private void fillTextView(View view, int textViewId, int value, int maxValue) {
		final TextView text = (TextView) view.findViewById(textViewId);
		if (value > 0) {
			text.setVisibility(View.VISIBLE);
			text.setText(value >= maxValue ? "*" : "" + value);
		} else {
			text.setVisibility(View.INVISIBLE);
		}
	}

}
