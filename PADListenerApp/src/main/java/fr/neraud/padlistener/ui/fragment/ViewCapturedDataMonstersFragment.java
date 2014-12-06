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

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
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

	@InjectView(R.id.view_captured_data_monsters_grid)
	GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.view_captured_data_fragment_monsters, container, false);
		ButterKnife.inject(this, view);

		mAdapter = new CapturedMonsterCursorAdapter(getActivity());
		gridView.setAdapter(mAdapter);
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
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), CapturedPlayerMonsterDescriptor.UriHelper.uriForAllWithInfo(),
				null, null, null, CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID_JP.getColName());
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
