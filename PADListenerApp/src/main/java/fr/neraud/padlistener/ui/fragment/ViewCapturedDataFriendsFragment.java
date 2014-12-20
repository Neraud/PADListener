package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;

/**
 * ViewCapturedData fragment for the Friends tabs
 *
 * @author Neraud
 */
public class ViewCapturedDataFriendsFragment extends AbstractListFriendsFragment {

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerFriendDescriptor.UriHelper.uriForAllWithInfo(),
				null, null, null, CapturedPlayerFriendDescriptor.TABLE_NAME + "." + CapturedPlayerFriendDescriptor.Fields.NAME.getColName());
		MyLog.exit();
		return loader;
	}

}
