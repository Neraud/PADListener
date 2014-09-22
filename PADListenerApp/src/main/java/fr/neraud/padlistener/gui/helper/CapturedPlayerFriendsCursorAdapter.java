package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;

/**
 * Adaptor to display the captured player friends
 *
 * @author Neraud
 */
public class CapturedPlayerFriendsCursorAdapter extends SimpleCursorAdapter {

	private final MonsterImageHelper imageHelper;

	public CapturedPlayerFriendsCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
		imageHelper = new MonsterImageHelper(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final CapturedFriendFullInfoModel model = CapturedPlayerFriendProviderHelper.cursorWithInfoToModel(cursor);
		final CapturedFriendModel friendModel = model.getFriendModel();

		final String friendName = context.getString(R.string.view_captured_data_friend_name, friendModel.getId(), friendModel.getName());
		((TextView) view.findViewById(R.id.view_captured_data_friend_name)).setText(friendName);

		fillLeader(context, view, friendModel.getLeader1(), model.getLeader1Info(),
				R.id.view_captured_data_friend_item_leader1_image,
				R.id.view_captured_data_friend_item_leader1_name,
				R.id.view_captured_data_friend_item_leader1_stats);
		fillLeader(context, view, friendModel.getLeader2(), model.getLeader2Info(),
				R.id.view_captured_data_friend_item_leader2_image,
				R.id.view_captured_data_friend_item_leader2_name,
				R.id.view_captured_data_friend_item_leader2_stats);
	}

	private void fillLeader(Context context, View view, CapturedFriendLeaderModel leader, MonsterInfoModel leaderInfo, int imageResId, int nameResId, int statsResId) {
		imageHelper.fillMonsterImage((ImageView)view.findViewById(imageResId), leader.getIdJp());

		final String leader1Name = context.getString(R.string.view_captured_friend_item_leader_name, leader.getIdJp(), leaderInfo.getName());
		((TextView) view.findViewById(nameResId)).setText(leader1Name);

		final String leader1Level = context.getString(R.string.view_captured_friend_item_leader_stats,
				leader.getLevel(), leader.getPlusHp(), leader.getPlusAtk(), leader.getPlusRcv(), leader.getAwakenings(), leaderInfo.getAwokenSkillIds().size());
		((TextView) view.findViewById(statsResId)).setText(leader1Level);
	}

}
