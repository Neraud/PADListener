package fr.neraud.padlistener.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ViewCapturedDataMonsterDetailDialogFragment;

/**
 * Base Adapter to display monsters with stats
 *
 * @author Neraud
 */
public abstract class AbstractMonsterWithStatsCursorAdapter extends AbstractMonsterCursorAdapter {

	private FragmentActivity mActivity;

	protected AbstractMonsterWithStatsCursorAdapter(FragmentActivity activity, int layoutId) {
		super(activity, layoutId);
		mActivity = activity;
	}

	protected void fillTextView(TextView textView, int value, int maxValue) {
		if (value > 0) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(value >= maxValue ? "*" : "" + value);
		} else {
			textView.setVisibility(View.INVISIBLE);
		}
	}

	protected void addDetailDialog(View view, final MonsterInfoModel monsterInfoModel, final BaseMonsterStatsModel monsterModel) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();
				final ViewCapturedDataMonsterDetailDialogFragment fragment = new ViewCapturedDataMonsterDetailDialogFragment();
				fragment.setMonsterInfoModel(monsterInfoModel);
				fragment.setMonsterStatsModel(monsterModel);
				fragment.show(mActivity.getSupportFragmentManager(), "view_captured_data_monster_detail");
				MyLog.exit();
			}
		});
	}

	protected FragmentActivity getActivity() {
		return mActivity;
	}
}
