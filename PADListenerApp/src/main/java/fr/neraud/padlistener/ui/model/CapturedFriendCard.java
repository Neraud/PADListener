package fr.neraud.padlistener.ui.model;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by Neraud on 27/09/2014.
 */
public class CapturedFriendCard extends Card {

	private FragmentActivity mActivity;
	private CapturedFriendFullInfoModel mModel;

	public CapturedFriendCard(FragmentActivity activity, CapturedFriendFullInfoModel model) {
		super(activity, R.layout.card_captured_friend);
		mActivity = activity;
		mModel = model;
		init();
	}

	private void init()  {
		final CardHeader header = new CardHeader(getContext());
		header.setTitle(mModel.getFriendModel().getName());
		addCardHeader(header);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		if (view == null) return;

		final TextView friendIdTextView = (TextView) view.findViewById(R.id.card_friend_id);
		friendIdTextView.setText(getContext().getString(R.string.view_captured_data_friend_id, mModel.getFriendModel().getId()));

		final TextView friendRankTextView = (TextView) view.findViewById(R.id.card_friend_rank);
		friendRankTextView.setText(getContext().getString(R.string.view_captured_data_friend_rank, mModel.getFriendModel().getRank()));

		final CardView leader1CardView = (CardView) view.findViewById(R.id.card_friend_leader_1);
		final MonsterCard leader1Card = new MonsterCard(mActivity, mModel.getLeader1Info(), mModel.getFriendModel().getLeader1());
		leader1CardView.setCard(leader1Card);

		final CardView leader2CardView = (CardView) view.findViewById(R.id.card_friend_leader_2);
		final MonsterCard leader2Card = new MonsterCard(mActivity, mModel.getLeader2Info(), mModel.getFriendModel().getLeader2());
		leader2CardView.setCard(leader2Card);
	}

}
