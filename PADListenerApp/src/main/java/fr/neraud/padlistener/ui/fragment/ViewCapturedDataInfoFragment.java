package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;
import fr.neraud.padlistener.ui.adapter.CapturedPlayerInfoCursorAdapter;

/**
 * ViewCapturedData fragment for the Information tab
 *
 * @author Neraud
 */
public class ViewCapturedDataInfoFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);

		adapter = new CapturedPlayerInfoCursorAdapter(getActivity(), R.layout.view_captured_data_fragment_info);
		setListAdapter(adapter);
		MyLog.exit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.entry();
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
		MyLog.exit();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
		MyLog.exit();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();
		adapter.swapCursor(data);
		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		adapter.swapCursor(null);
		MyLog.exit();
	}

}
