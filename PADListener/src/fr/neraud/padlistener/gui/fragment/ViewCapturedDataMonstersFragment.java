
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
import fr.neraud.padlistener.gui.helper.CapturedPlayerMonsterCursorAdapter;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;

/**
 * ViewCapturedData fragment for the Monsters tabs
 * 
 * @author Neraud
 */
public class ViewCapturedDataMonstersFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		adapter = new CapturedPlayerMonsterCursorAdapter(getActivity().getApplicationContext(),
		        R.layout.view_captured_data_item_monster);
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
		return new CursorLoader(getActivity(), CapturedPlayerMonsterDescriptor.UriHelper.uriForAllWithInfo(), null, null, null,
		        null);
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
