package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
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

	static class ViewHolder {

		@InjectView(R.id.view_captured_data_friend_name)
		TextView friendNameTextView;
		@InjectView(R.id.view_captured_data_friend_id)
		TextView friendIdTextView;
		@InjectView(R.id.view_captured_data_friend_rank)
		TextView friendRankTextView;

		@InjectView(R.id.view_captured_data_friend_leader1_container)
		ViewGroup leader1Container;
		@InjectView(R.id.view_captured_data_friend_leader1_image)
		ImageView leader1Image;
		@InjectView(R.id.view_captured_data_friend_leader1_awakenings)
		TextView leader1Awakenings;
		@InjectView(R.id.view_captured_data_friend_leader1_level)
		TextView leader1Level;
		@InjectView(R.id.view_captured_data_friend_leader1_skill_level)
		TextView leader1SkillLevel;
		@InjectView(R.id.view_captured_data_friend_leader1_pluses)
		TextView leader1Pluses;

		@InjectView(R.id.view_captured_data_friend_leader2_container)
		ViewGroup leader2Container;
		@InjectView(R.id.view_captured_data_friend_leader2_image)
		ImageView leader2Image;
		@InjectView(R.id.view_captured_data_friend_leader2_awakenings)
		TextView leader2Awakenings;
		@InjectView(R.id.view_captured_data_friend_leader2_level)
		TextView leader2Level;
		@InjectView(R.id.view_captured_data_friend_leader2_skill_level)
		TextView leader2SkillLevel;
		@InjectView(R.id.view_captured_data_friend_leader2_pluses)
		TextView leader2Pluses;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public CapturedFriendCursorAdapter(FragmentActivity activity) {
		super(activity, R.layout.view_captured_data_fragment_friends_item);
		mActivity = activity;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		MyLog.entry();

		final ViewHolder viewHolder = new ViewHolder(view);

		final CapturedFriendFullInfoModel model = CapturedPlayerFriendProviderHelper.cursorWithInfoToModel(cursor);

		viewHolder.friendNameTextView.setText(model.getFriendModel().getName());
		viewHolder.friendIdTextView.setText(mActivity.getString(R.string.view_captured_data_friend_id, model.getFriendModel().getId()));
		viewHolder.friendRankTextView.setText(mActivity.getString(R.string.view_captured_data_friend_rank, model.getFriendModel().getRank()));

		fillLeader1(viewHolder, model.getLeader1Info(), model.getFriendModel().getLeader1());
		fillLeader2(viewHolder, model.getLeader2Info(), model.getFriendModel().getLeader2());

		MyLog.exit();
	}

	private void fillLeader1(ViewHolder viewHolder, MonsterInfoModel monsterInfoModel, BaseMonsterStatsModel monsterStatsModel) {
		fillImage(viewHolder.leader1Image, monsterInfoModel);
		addDetailDialog(viewHolder.leader1Container, monsterInfoModel, monsterStatsModel);

		fillTextView(viewHolder.leader1Awakenings, monsterStatsModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(viewHolder.leader1Level, monsterStatsModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(viewHolder.leader1SkillLevel, monsterStatsModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterStatsModel.getPlusHp() + monsterStatsModel.getPlusAtk() + monsterStatsModel.getPlusRcv();
		fillTextView(viewHolder.leader1Pluses, totalPluses, 3 * 99);
	}

	private void fillLeader2(ViewHolder viewHolder, MonsterInfoModel monsterInfoModel, BaseMonsterStatsModel monsterStatsModel) {
		fillImage(viewHolder.leader2Image, monsterInfoModel);
		addDetailDialog(viewHolder.leader2Container, monsterInfoModel, monsterStatsModel);

		fillTextView(viewHolder.leader2Awakenings, monsterStatsModel.getAwakenings(), monsterInfoModel.getAwokenSkillIds().size());
		fillTextView(viewHolder.leader2Level, monsterStatsModel.getLevel(), monsterInfoModel.getMaxLevel());
		fillTextView(viewHolder.leader2SkillLevel, monsterStatsModel.getSkillLevel(), 999); // TODO
		final int totalPluses = monsterStatsModel.getPlusHp() + monsterStatsModel.getPlusAtk() + monsterStatsModel.getPlusRcv();
		fillTextView(viewHolder.leader2Pluses, totalPluses, 3 * 99);
	}

}
