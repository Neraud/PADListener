package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;

/**
 * ContentProvider to manipulate the PlayerInfo
 *
 * @author Neraud
 */
public class CapturedPlayerInfoProvider extends AbstractPADListenerDbContentProvider {

	@Override
	public boolean onCreate() {
		Log.d(getClass().getName(), "onCreate");
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final CapturedPlayerInfoDescriptor.Paths path = CapturedPlayerInfoDescriptor.matchUri(uri);
		if (path != null) {
			return path.getContentType();
		} else {
			throw new UnsupportedOperationException("URI " + uri + " is not supported.");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(getClass().getName(), "insert : " + uri);
		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerInfoDescriptor.Paths path = CapturedPlayerInfoDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(CapturedPlayerInfoDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(getClass().getName(), "query : " + uri);
		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerInfoDescriptor.Paths path = CapturedPlayerInfoDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(CapturedPlayerInfoDescriptor.TABLE_NAME);

		switch (path) {
			case ALL:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		final Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(getClass().getName(), "update : " + uri);
		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerInfoDescriptor.Paths path = CapturedPlayerInfoDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case ALL:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		count = db.update(CapturedPlayerInfoDescriptor.TABLE_NAME, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(getClass().getName(), "delete : " + uri);
		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerInfoDescriptor.Paths path = CapturedPlayerInfoDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case ALL:
				count = db.delete(CapturedPlayerInfoDescriptor.TABLE_NAME, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return count;
	}

}
