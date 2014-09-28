package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.ui.adapter.CapturedFriendCursorAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * ViewCapturedData fragment for the Friends tabs
 *
 * @author Neraud
 */
public class ViewCapturedDataFriendsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private CapturedFriendCursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");

		final View view = inflater.inflate(R.layout.view_captured_data_fragment_friends, container, false);

		mAdapter = new CapturedFriendCursorAdapter(getActivity());

		final CardGridView mGridView = (CardGridView) view.findViewById(R.id.view_captured_data_friends_grid);
		mGridView.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(getClass().getName(), "onCreateLoader");
		return new CursorLoader(getActivity(), CapturedPlayerFriendDescriptor.UriHelper.uriForAllWithInfo(), null, null, null, CapturedPlayerFriendDescriptor.TABLE_NAME + "." + CapturedPlayerFriendDescriptor.Fields.NAME.getColName());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(getClass().getName(), "onLoadFinished");
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(getClass().getName(), "onLoaderReset");
		mAdapter.swapCursor(null);
	}

}
