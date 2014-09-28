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
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.ui.adapter.MonsterInfoCursorAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * ViewMonsterInfo fragment for the Info tab
 *
 * @author Neraud
 */
public class ViewMonsterInfoMonstersFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private MonsterInfoCursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_monsters, container, false);
		mAdapter = new MonsterInfoCursorAdapter(getActivity());

		final CardGridView mGridView = (CardGridView) view.findViewById(R.id.view_monster_info_monster_grid);
		mGridView.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(getClass().getName(), "onCreateLoader");
		return new CursorLoader(getActivity(), MonsterInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
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
