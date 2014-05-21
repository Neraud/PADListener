
package fr.neraud.padlistener.gui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.PlayerInfoCursorAdapter;
import fr.neraud.padlistener.provider.descriptor.PlayerInfoDescriptor;

public class ViewCapturedDataInfoFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		adapter = new PlayerInfoCursorAdapter(getActivity().getApplicationContext(), R.layout.view_captured_data_tab_info_item);
		setListAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(getClass().getName(), "onCreateLoader");
		return new CursorLoader(getActivity(), PlayerInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(getClass().getName(), "onLoadFinished");
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(getClass().getName(), "onLoaderReset");
		adapter.swapCursor(null);
	}

}
