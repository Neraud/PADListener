package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;

/**
 * Created by Neraud on 28/09/2014.
 */
public class CapturedFriendCursorAdapter extends AbstractMonsterWithStatsCursorAdapter {

	private FragmentActivity mActivity;

	public CapturedFriendCursorAdapter(FragmentActivity activity) {
		super(activity, R.layout.view_captured_data_fragment_friends_item);
		mActivity = activity;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final CapturedFriendFullInfoModel model = CapturedPlayerFriendProviderHelper.cursorWithInfoToModel(cursor);

		final TextView friendNameTextView = (TextView) view.findViewById(R.id.card_friend_name);
		friendNameTextView.setText(model.getFriendModel().getName());

		final TextView friendIdTextView = (TextView) view.findViewById(R.id.card_friend_id);
		friendIdTextView.setText(mActivity.getString(R.string.view_captured_data_friend_id, model.getFriendModel().getId()));

		final TextView friendRankTextView = (TextView) view.findViewById(R.id.card_friend_rank);
		friendRankTextView.setText(mActivity.getString(R.string.view_captured_data_friend_rank, model.getFriendModel().getRank()));

		fillLeader1(view, model.getLeader1Info(), model.getFriendModel().getLeader1());
		fillLeader2(view, model.getLeader2Info(), model.getFriendModel().getLeader2());
	}

	private void fillLeader1(View view, MonsterInfoModel monsterInfoModel, BaseMonsterStatsModel monsterStatsModel) {
		final ViewGroup leaderContainer = (ViewGroup) view.findViewById(R.id.view_captured_data_friend_leader1_container);

		fillImage(leaderContainer, R.id.view_captured_data_friend_leader1_image, monsterInfoModel);
		addDetailDialog(leaderContainer, monsterInfoModel, monsterStatsModel);

		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader1_awakenings, monsterStatsModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader1_level, monsterStatsModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader1_skill_level, monsterStatsModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterStatsModel.getPlusHp() + monsterStatsModel.getPlusAtk() + monsterStatsModel.getPlusRcv();
		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader1_pluses, totalPluses, 3 * 99);
	}

	private void fillLeader2(View view, MonsterInfoModel monsterInfoModel, BaseMonsterStatsModel monsterStatsModel) {
		final ViewGroup leaderContainer = (ViewGroup) view.findViewById(R.id.view_captured_data_friend_leader2_container);

		fillImage(leaderContainer, R.id.view_captured_data_friend_leader2_image, monsterInfoModel);
		addDetailDialog(leaderContainer, monsterInfoModel, monsterStatsModel);

		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader2_awakenings, monsterStatsModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader2_level, monsterStatsModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader2_skill_level, monsterStatsModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterStatsModel.getPlusHp() + monsterStatsModel.getPlusAtk() + monsterStatsModel.getPlusRcv();
		fillTextView(leaderContainer, R.id.view_captured_data_friend_leader2_pluses, totalPluses, 3 * 99);
	}

}
