
package fr.neraud.padlistener.gui;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.helper.MonsterInfoCursorAdapter;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

public class ViewMonsterInfoActivity extends ListActivity implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		adapter = new MonsterInfoCursorAdapter(getApplicationContext(), R.layout.view_monster_info_item);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onDestroy() {
		Log.d(getClass().getName(), "onDestroy");
		super.onDestroy();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(getClass().getName(), "onCreateLoader");
		return new CursorLoader(getApplicationContext(), MonsterInfoDescriptor.UriHelper.uriForAll(), null, null, null, null);
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
