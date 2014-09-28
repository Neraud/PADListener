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
import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;
import fr.neraud.padlistener.ui.fragment.ViewCapturedDataMonsterDetailDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Neraud on 27/09/2014.
 */
public class CapturedMonsterCard extends Card {

	private FragmentActivity mActivity;
	private CapturedMonsterFullInfoModel mModel;

	public CapturedMonsterCard(FragmentActivity activity, CapturedMonsterFullInfoModel model) {
		super(activity, R.layout.card_monster_captured);
		mActivity = activity;
		mModel = model;
		init();
	}

	private void init() {
		setOnClickListener(new OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				Log.d(getClass().getName(), "onClick");
				final ViewCapturedDataMonsterDetailDialogFragment fragment = new ViewCapturedDataMonsterDetailDialogFragment();
				fragment.setModel(mModel);
				fragment.show(mActivity.getSupportFragmentManager(), "view_captured_data_monster_detail");
			}
		});
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;

		final ImageView monsterImageView = (ImageView) view.findViewById(R.id.card_monster_captured_image);
		monsterImageView.clearColorFilter();
		final String imageUrl = PadHerderDescriptor.serverUrl + mModel.getMonsterInfo().getImage60Url();

		Picasso.with(getContext())
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		fillTextView(view, R.id.card_monster_captured_awakenings, mModel.getCapturedMonster().getAwakenings(), mModel.getMonsterInfo().getAwokenSkillIds().size());
		fillTextView(view, R.id.card_monster_captured_level, mModel.getCapturedMonster().getLevel(), mModel.getMonsterInfo().getMaxLevel());
		fillTextView(view, R.id.card_monster_captured_skill_level, mModel.getCapturedMonster().getSkillLevel(), 999); // TODO
		final int totalPluses = mModel.getCapturedMonster().getPlusHp() + mModel.getCapturedMonster().getPlusAtk() + mModel.getCapturedMonster().getPlusRcv();
		fillTextView(view, R.id.card_monster_captured_pluses, totalPluses, 3*99);
	}

	private void fillTextView(View view, int textViewResId, int value, int maxValue) {
		final TextView text = (TextView) view.findViewById(textViewResId);
		text.setText(value >= maxValue ? "*" : "" + value);
	}
}
