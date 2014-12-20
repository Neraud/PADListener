package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.HashSet;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;
import fr.neraud.padlistener.provider.helper.BaseProviderHelper;
import fr.neraud.padlistener.provider.helper.CapturedPlayerFriendProviderHelper;
import fr.neraud.padlistener.ui.activity.FilterFriendsListUselessActivity;
import fr.neraud.padlistener.ui.adapter.ListFriendCursorAdapter;

/**
 * Created by Neraud on 20/12/2014.
 */
public class FilterFriendsListUselessFragment extends AbstractListFriendsFragment {

	private HashSet<Long> mUsefulFriendIds;
	private boolean mOnlyNotFavourite = true;

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);

		mUsefulFriendIds = ((FilterFriendsListUselessActivity) getActivity()).getUsefulFriendIds();

		MyLog.exit();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();

		final StringBuilder selectionBuilder = new StringBuilder();

		selectionBuilder.append(CapturedPlayerFriendDescriptor.TABLE_NAME).append(".").append(CapturedPlayerFriendLeaderDescriptor.Fields.PLAYER_ID.getColName());
		selectionBuilder.append(" NOT IN (");

		boolean first = true;
		for (final Long usefulFriendId : mUsefulFriendIds) {
			if (!first) {
				selectionBuilder.append(", ");
			}
			selectionBuilder.append(usefulFriendId);
			first = false;
		}
		selectionBuilder.append(")");

		if (mOnlyNotFavourite) {
			selectionBuilder.append(" AND ");
			selectionBuilder.append(CapturedPlayerFriendDescriptor.TABLE_NAME).append(".").append(CapturedPlayerFriendDescriptor.Fields.FAVOURITE.getColName());
			selectionBuilder.append(" = ").append(BaseProviderHelper.BOOLEAN_FALSE);
		}

		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerFriendDescriptor.UriHelper.uriForAllWithInfo(),
				null, selectionBuilder.toString(), null, CapturedPlayerFriendDescriptor.TABLE_NAME + "." + CapturedPlayerFriendDescriptor.Fields.NAME.getColName());
		MyLog.exit();
		return loader;
	}

	public boolean isOnlyNotFavourite() {
		return mOnlyNotFavourite;
	}

	public void filterResults(boolean onlyNotFavourite) {
		mOnlyNotFavourite = onlyNotFavourite;
		getLoaderManager().restartLoader(0, null, this);
	}

	public Set<CapturedFriendModel> extractUselessFriend() {
		MyLog.entry();
		final Set<CapturedFriendModel> uselessFriends = new HashSet<>();

		final ListFriendCursorAdapter adapter = getAdapter();

		for (int i = 0; i < adapter.getCount(); i++) {
			final Cursor cursor = (Cursor) adapter.getItem(i);
			final CapturedFriendModel model = CapturedPlayerFriendProviderHelper.cursorToModel(cursor);
			uselessFriends.add(model);
		}

		MyLog.exit();
		return uselessFriends;
	}
}
