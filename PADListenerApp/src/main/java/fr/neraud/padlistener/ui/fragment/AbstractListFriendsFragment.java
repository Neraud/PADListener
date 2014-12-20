package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.adapter.ListFriendCursorAdapter;

/**
 * @author Neraud
 */
public abstract class AbstractListFriendsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private ListFriendCursorAdapter mAdapter;

	@InjectView(R.id.list_friends_grid)
	GridView mGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.list_friends_fragment, container, false);
		ButterKnife.inject(this, view);

		mAdapter = new ListFriendCursorAdapter(getActivity());
		mGridView.setAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);

		MyLog.exit();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();

		MyLog.debug("data : " + (data != null ? data.getCount() : "null"));
		mAdapter.swapCursor(data);
		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		mAdapter.swapCursor(null);
		MyLog.exit();
	}

	protected ListFriendCursorAdapter getAdapter() {
		return mAdapter;
	}
}
