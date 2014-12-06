package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
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

	static class ViewHolder {

		@InjectView(R.id.view_captured_monsters_item_image)
		ImageView image;
		@InjectView(R.id.view_captured_monsters_item_awakenings)
		TextView awakeningsText;
		@InjectView(R.id.view_captured_monsters_item_level)
		TextView levelText;
		@InjectView(R.id.view_captured_monsters_item_skill_level)
		TextView skillLevelText;
		@InjectView(R.id.view_captured_monsters_item_pluses)
		TextView plusesText;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public CapturedMonsterCursorAdapter(FragmentActivity context) {
		super(context, R.layout.view_captured_data_fragment_monsters_item);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		MyLog.entry();

		final ViewHolder viewHolder = new ViewHolder(view);

		final CapturedMonsterFullInfoModel model = CapturedPlayerMonsterProviderHelper.cursorWithInfoToModel(cursor);
		final MonsterModel monsterModel = model.getCapturedMonster();
		final MonsterInfoModel monsterInfoModel = model.getMonsterInfo();

		fillImage(viewHolder.image, monsterInfoModel);

		fillTextView(viewHolder.awakeningsText, monsterModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(viewHolder.levelText, monsterModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(viewHolder.skillLevelText, monsterModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterModel.getPlusHp() + monsterModel.getPlusAtk() + monsterModel.getPlusRcv();
		fillTextView(viewHolder.plusesText, totalPluses, 3 * 99);
		addDetailDialog(view, monsterInfoModel, monsterModel);

		MyLog.exit();
	}

}
