package fr.neraud.padlistener.ui.model;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ViewCapturedDataMonsterDetailDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Neraud on 27/09/2014.
 */
public class MonsterWithStatsCard extends AbstractMonsterCard {

	private FragmentActivity mActivity;
	private BaseMonsterStatsModel mMonsterStatsModel;

	public MonsterWithStatsCard(FragmentActivity activity, MonsterInfoModel monsterInfoModel, BaseMonsterStatsModel monsterStatsModel) {
		super(activity, monsterInfoModel, R.layout.card_monster_with_stats);
		mMonsterStatsModel = monsterStatsModel;
		mActivity = activity;
	}

	protected void init() {
		super.init();
		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				Log.d(getClass().getName(), "onClick");
				final ViewCapturedDataMonsterDetailDialogFragment fragment = new ViewCapturedDataMonsterDetailDialogFragment();
				fragment.setMonsterInfoModel(mMonsterInfoModel);
				fragment.setMonsterStatsModel(mMonsterStatsModel);
				fragment.show(mActivity.getSupportFragmentManager(), "view_captured_data_monster_detail");
			}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;
		super.setupInnerViewElements(parent, view);

		fillImage(view);

		fillTextView(view, R.id.card_monster_awakenings, mMonsterStatsModel.getAwakenings(), mMonsterInfoModel.getAwokenSkillIds().size());
		fillTextView(view, R.id.card_monster_level, mMonsterStatsModel.getLevel(), mMonsterInfoModel.getMaxLevel());
		fillTextView(view, R.id.card_monster_skill_level, mMonsterStatsModel.getSkillLevel(), 999); // TODO
		final int totalPluses = mMonsterStatsModel.getPlusHp() + mMonsterStatsModel.getPlusAtk() + mMonsterStatsModel.getPlusRcv();
		fillTextView(view, R.id.card_monster_pluses, totalPluses, 3 * 99);
	}

	private void fillTextView(View view, int textViewId, int value, int maxValue) {
		final TextView text = (TextView) view.findViewById(textViewId);
		if(value > 0) {
			text.setVisibility(View.VISIBLE);
			text.setText(value >= maxValue ? "*" : "" + value);
		}else {
			text.setVisibility(View.INVISIBLE);
		}
	}
}
