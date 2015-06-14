package fr.neraud.padlistener.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;
import fr.neraud.padlistener.provider.helper.BaseProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendLeaderProviderHelper;
import fr.neraud.padlistener.provider.helper.MonsterInfoProviderHelper;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Created by Neraud on 28/09/2014.
 */
public class CapturedFriendLeadersCursorAdapter extends AbstractMonsterWithStatsCursorAdapter {

	static class ViewHolder {

		@InjectView(R.id.view_captured_friend_leader_image)
		ImageView image;
		@InjectView(R.id.view_captured_friend_leader_awakenings)
		TextView awakeningsTextView;
		@InjectView(R.id.view_captured_friend_leader_level)
		TextView levelTextView;
		@InjectView(R.id.view_captured_friend_leader_skill_level)
		TextView skillLevelTextView;
		@InjectView(R.id.view_captured_friend_leader_pluses)
		TextView plusesTextView;
		@InjectView(R.id.view_captured_friend_leader_last_seen)
		TextView lastSeenTextView;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public CapturedFriendLeadersCursorAdapter(FragmentActivity activity) {
		super(activity, R.layout.view_captured_data_fragment_friend_leader_item);
	}

	@Override
	public void bindView(View view, final Context context, final Cursor cursor) {
		MyLog.entry();

		final ViewHolder viewHolder = new ViewHolder(view);

		final CapturedFriendLeaderModel statsModel = CapturedPlayerFriendLeaderProviderHelper.cursorWithInfoToModel(cursor);
		final int leaderFriendPk = BaseProviderHelper.getInt(cursor, CapturedPlayerFriendLeaderDescriptor.Fields._ID);
		final MonsterInfoModel infoModel = MonsterInfoProviderHelper.cursorToModelWithPrefix(cursor, CapturedPlayerFriendLeaderDescriptor.ALL_WITH_INFO_PREFIX);

		new MonsterImageHelper(context).fillImage(viewHolder.image, infoModel);

		viewHolder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.view_captured_friend_leader_delete_dialog_title);
				builder.setMessage(getActivity().getString(R.string.view_captured_friend_leader_delete_dialog_content, infoModel.getName()));
				builder.setPositiveButton(R.string.view_captured_friend_leader_delete_dialog_confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyLog.debug("Removing " + statsModel);

						final Uri uri = CapturedPlayerFriendLeaderDescriptor.UriHelper.uriForAll();
						final String where = CapturedPlayerFriendLeaderDescriptor.Fields._ID + " = ?";
						final String[] args = new String[]{"" + leaderFriendPk};

						getActivity().getContentResolver().delete(uri, where, args);
						// TODO : if you remove the current leaders, ViewCapturedDataFragment should probably be refreshed to prevent from showing NULLs
					}
				});
				builder.setNegativeButton(R.string.view_captured_friend_leader_delete_dialog_cancel, null);
				builder.create().show();
			}
		});

		viewHolder.levelTextView.setText(context.getString(R.string.view_captured_friend_leader_level_value, statsModel.getLevel()));
		viewHolder.plusesTextView.setText(context.getString(R.string.view_captured_friend_leader_pluses_value,
				statsModel.getPlusHp(),
				statsModel.getPlusAtk(),
				statsModel.getPlusRcv()));
		viewHolder.awakeningsTextView.setText(context.getString(R.string.view_captured_friend_leader_awakenings_value, statsModel.getAwakenings()));
		viewHolder.skillLevelTextView.setText(context.getString(R.string.view_captured_friend_leader_skill_level_value, statsModel.getSkillLevel()));
		viewHolder.lastSeenTextView.setText(context.getString(R.string.view_captured_friend_leader_last_seen, statsModel.getLastSeen()));

		MyLog.exit();
	}

}
