package fr.neraud.padlistener.ui.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.CapturedFriendFullInfoModel;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.provider.helper.BaseProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;
import fr.neraud.padlistener.ui.fragment.ViewCapturedDataFriendLeadersFragment;

/**
 * Created by Neraud on 28/09/2014.
 */
public class ListFriendCursorAdapter extends AbstractMonsterWithStatsCursorAdapter {

	private final FragmentActivity mActivity;

	static class ViewHolder {

		@InjectView(R.id.list_friends_item_name)
		TextView friendNameTextView;
		@InjectView(R.id.list_friends_item_rank)
		TextView friendRankTextView;
		@InjectView(R.id.list_friends_item_favourite)
		ImageView favouriteImageView;

		@InjectView(R.id.list_friends_item_leader1_container)
		ViewGroup leader1Container;
		@InjectView(R.id.list_friends_item_leader1_image)
		ImageView leader1Image;
		@InjectView(R.id.list_friends_item_leader1_awakenings)
		TextView leader1Awakenings;
		@InjectView(R.id.list_friends_item_leader1_level)
		TextView leader1Level;
		@InjectView(R.id.list_friends_item_leader1_skill_level)
		TextView leader1SkillLevel;
		@InjectView(R.id.list_friends_item_leader1_pluses)
		TextView leader1Pluses;

		@InjectView(R.id.list_friends_item_leader2_container)
		ViewGroup leader2Container;
		@InjectView(R.id.list_friends_item_leader2_image)
		ImageView leader2Image;
		@InjectView(R.id.list_friends_item_leader2_awakenings)
		TextView leader2Awakenings;
		@InjectView(R.id.list_friends_item_leader2_level)
		TextView leader2Level;
		@InjectView(R.id.list_friends_item_leader2_skill_level)
		TextView leader2SkillLevel;
		@InjectView(R.id.list_friends_item_leader2_pluses)
		TextView leader2Pluses;

		@InjectView(R.id.list_friends_item_leader_history)
		Button leaderHistoryButton;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	private static class FavouriteUpdateAsyncTask extends AsyncTask<CapturedFriendModel, Void, Void> {

		private final Context mContext;

		private FavouriteUpdateAsyncTask(Context context) {
			mContext = context;
		}

		@Override
		protected Void doInBackground(CapturedFriendModel... params) {
			MyLog.entry();

			final ContentResolver cr = mContext.getContentResolver();
			final Uri uriFriends = CapturedPlayerFriendDescriptor.UriHelper.uriForAll();

			for (CapturedFriendModel friend : params) {
				final ContentValues values = new ContentValues();
				BaseProviderHelper.putValue(values, CapturedPlayerFriendDescriptor.Fields.FAVOURITE, friend.isFavourite());
				final String projection = CapturedPlayerFriendDescriptor.Fields.ID.getColName() + "=" + friend.getId();
				cr.update(uriFriends, values, projection, null);
			}

			MyLog.exit();
			return null;
		}
	}

	public ListFriendCursorAdapter(FragmentActivity activity) {
		super(activity, R.layout.list_friends_item);
		mActivity = activity;
	}

	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		MyLog.entry();

		final ViewHolder viewHolder = new ViewHolder(view);

		final CapturedFriendFullInfoModel model = CapturedPlayerFriendProviderHelper.cursorWithInfoToModel(cursor);

		viewHolder.friendNameTextView.setText(model.getFriendModel().getName());
		viewHolder.friendRankTextView.setText(mActivity.getString(R.string.list_friends_item_rank, model.getFriendModel().getRank()));

		final int drawableResId = model.getFriendModel().isFavourite() ? R.drawable.ic_action_favourite : R.drawable.ic_action_not_favourite;
		viewHolder.favouriteImageView.setImageDrawable(context.getResources().getDrawable(drawableResId));

		viewHolder.favouriteImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onFavouriteClicked(v, context, model);
			}
		});

		viewHolder.leaderHistoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLeaderHistoryClicked(model);
			}
		});

		fillLeader1(viewHolder, model.getLeader1Info(), model.getLeader1());
		fillLeader2(viewHolder, model.getLeader2Info(), model.getLeader2());

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

	private void onFavouriteClicked(View v, Context context, CapturedFriendFullInfoModel model) {
		MyLog.entry();

		final boolean newValue = !model.getFriendModel().isFavourite();
		model.getFriendModel().setFavourite(newValue);

		final int drawableResId = newValue ? R.drawable.ic_action_favourite : R.drawable.ic_action_not_favourite;
		((ImageView) v).setImageDrawable(context.getResources().getDrawable(drawableResId));
		final AsyncTask<CapturedFriendModel, Void, Void> task = new FavouriteUpdateAsyncTask(context);
		task.execute(model.getFriendModel());

		MyLog.exit();
	}

	private void onLeaderHistoryClicked(CapturedFriendFullInfoModel model) {
		MyLog.entry();

		final ViewCapturedDataFriendLeadersFragment fragment = new ViewCapturedDataFriendLeadersFragment();

		final Bundle args = new Bundle();
		ViewCapturedDataFriendLeadersFragment.fillCapturedFriendModel(args, model.getFriendModel());
		fragment.setArguments(args);

		final FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
		final FragmentTransaction ft = fragmentManager.beginTransaction();
		final Fragment prev = fragmentManager.findFragmentByTag("leaderHistoryDialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		fragment.show(ft, "leaderHistoryDialog");

		MyLog.exit();
	}

}
