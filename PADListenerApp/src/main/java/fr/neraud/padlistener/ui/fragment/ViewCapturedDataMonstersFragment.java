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
import android.widget.GridView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.ui.adapter.CapturedMonsterCursorAdapter;

/**
 * ViewCapturedData fragment for the Monsters tabs
 *
 * @author Neraud
 */
public class ViewCapturedDataMonstersFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private CapturedMonsterCursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");

		final View view = inflater.inflate(R.layout.view_captured_data_fragment_monsters, container, false);

		mAdapter = new CapturedMonsterCursorAdapter(getActivity());

		final GridView gridView = (GridView) view.findViewById(R.id.view_captured_data_monsters_grid);
		gridView.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		return view;
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(getClass().getName(), "onCreateLoader");
		return new CursorLoader(getActivity(), CapturedPlayerMonsterDescriptor.UriHelper.uriForAllWithInfo(),
				null, null, null, CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID_JP.getColName());
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
