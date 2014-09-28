package fr.neraud.padlistener.ui.model;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.fragment.ViewCapturedDataMonsterDetailDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Neraud on 27/09/2014.
 */
public class MonsterCard extends Card {

	private FragmentActivity mActivity;
	private MonsterInfoModel mMonsterInfoModel;
	private BaseMonsterStatsModel mMonsterStatsModel;

	public MonsterCard(FragmentActivity activity, MonsterInfoModel monsterInfoModel, BaseMonsterStatsModel monsterStatsModel) {
		super(activity, R.layout.card_monster);
		mActivity = activity;
		mMonsterInfoModel = monsterInfoModel;
		mMonsterStatsModel = monsterStatsModel;
		init();
	}

	private void init() {
		setShadow(false);
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

		final ImageView monsterImageView = (ImageView) view.findViewById(R.id.card_monster_image);
		monsterImageView.clearColorFilter();
		final String imageUrl = PadHerderDescriptor.serverUrl + mMonsterInfoModel.getImage60Url();

		Picasso.with(getContext())
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		fillTextView(view, R.id.card_monster_awakenings, mMonsterStatsModel.getAwakenings(), mMonsterInfoModel.getAwokenSkillIds().size());
		fillTextView(view, R.id.card_monster_level, mMonsterStatsModel.getLevel(), mMonsterInfoModel.getMaxLevel());
		fillTextView(view, R.id.card_monster_skill_level, mMonsterStatsModel.getSkillLevel(), 999); // TODO
		final int totalPluses = mMonsterStatsModel.getPlusHp() + mMonsterStatsModel.getPlusAtk() + mMonsterStatsModel.getPlusRcv();
		fillTextView(view, R.id.card_monster_pluses, totalPluses, 3 * 99);
	}

	private void fillTextView(View view, int textViewResId, int value, int maxValue) {
		final TextView text = (TextView) view.findViewById(textViewResId);
		text.setText(value >= maxValue ? "*" : "" + value);
	}
}
