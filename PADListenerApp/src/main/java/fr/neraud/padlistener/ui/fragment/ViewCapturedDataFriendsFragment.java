package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.ui.adapter.CapturedFriendCursorAdapter;

/**
 * ViewCapturedData fragment for the Friends tabs
 *
 * @author Neraud
 */
public class ViewCapturedDataFriendsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private CapturedFriendCursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.view_captured_data_fragment_friends, container, false);

		mAdapter = new CapturedFriendCursorAdapter(getActivity());

		final GridView mGridView = (GridView) view.findViewById(R.id.view_captured_data_friends_grid);
		mGridView.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		MyLog.exit();
		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerFriendDescriptor.UriHelper.uriForAllWithInfo(),
				null, null, null, CapturedPlayerFriendDescriptor.TABLE_NAME + "." + CapturedPlayerFriendDescriptor.Fields.NAME.getColName());
		MyLog.exit();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();
		mAdapter.swapCursor(data);
		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		mAdapter.swapCursor(null);
		MyLog.exit();
	}

}
