package fr.neraud.padlistener.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.ui.adapter.MonsterInfoCursorAdapter;

/**
 * Main fragment for ViewMonsterInfo
 *
 * @author Neraud
 */
public class ViewMonsterInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private MonsterInfoCursorAdapter mAdapter;
	private TextView mCurrentTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.view_monster_info_fragment_monsters, container, false);

		mCurrentTextView = (TextView) view.findViewById(R.id.monster_info_fetch_info_current);
		refreshLastUpdate();

		mAdapter = new MonsterInfoCursorAdapter(getActivity());

		final GridView mGridView = (GridView) view.findViewById(R.id.view_monster_info_monster_grid);
		mGridView.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		MyLog.exit();
		return view;
	}


	private void refreshLastUpdate() {
		final Date lastRefreshDate = new TechnicalSharedPreferencesHelper(getActivity()).getMonsterInfoRefreshDate();
		if(lastRefreshDate.getTime() > 0) {
			final String lastRefreshDateFormatted = SimpleDateFormat.getDateInstance().format(lastRefreshDate);
			mCurrentTextView.setText(getString(R.string.monster_info_last_refresh_date, lastRefreshDateFormatted));
		} else {
			mCurrentTextView.setText(getString(R.string.monster_info_last_refresh_never));
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.entry();
		final Loader<Cursor> loader = new CursorLoader(getActivity(), MonsterInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
		MyLog.exit();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		MyLog.entry();
		mAdapter.swapCursor(data);
		refreshLastUpdate();
		MyLog.exit();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.entry();
		mAdapter.swapCursor(null);
		MyLog.exit();
	}

}
