
package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * ContentProvider to manipulate the PlayerMonster
 * 
 * @author Neraud
 */
public class CapturedPlayerMonsterProvider extends AbstractPADListenerDbContentProvider {

	@Override
	public boolean onCreate() {
		Log.d(getClass().getName(), "onCreate");
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);
		if (path != null) {
			return path.getContentType();
		} else {
			throw new UnsupportedOperationException("URI " + uri + " is not supported.");
		}
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		Log.d(getClass().getName(), "bulkInsert");
		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		switch (path) {
		case ALL:
			break;
		default:
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		final int count = values.length;
		for (int i = 0; i < count; i++) {
			db.insert(CapturedPlayerMonsterDescriptor.TABLE_NAME, null, values[i]);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(getClass().getName(), "insert : " + uri);
		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(CapturedPlayerMonsterDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(getClass().getName(), "query : " + uri);
		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		switch (path) {
		case ALL:
			builder.setTables(CapturedPlayerMonsterDescriptor.TABLE_NAME);
			break;
		case ALL_WITH_INFO:
			final PADRegion region = PADRegion.valueOf(uri.getLastPathSegment());
			final StringBuilder tableBuilder = new StringBuilder(CapturedPlayerMonsterDescriptor.TABLE_NAME);
			tableBuilder.append(" LEFT OUTER JOIN ").append(MonsterInfoDescriptor.TABLE_NAME);
			tableBuilder.append(" ON (");
			tableBuilder.append(CapturedPlayerMonsterDescriptor.TABLE_NAME).append(".")
			        .append(CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID.getColName());
			tableBuilder.append(" = ");
			MonsterInfoDescriptor.Fields idField = null;
			switch (region) {
			case US:
				idField = MonsterInfoDescriptor.Fields.ID_US;
				break;
			case JP:
			default:
				idField = MonsterInfoDescriptor.Fields.ID_JP;
			}
			tableBuilder.append(MonsterInfoDescriptor.TABLE_NAME).append(".").append(idField.getColName());
			tableBuilder.append(")");

			builder.setTables(tableBuilder.toString());
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
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		int count;

		switch (path) {
		case ALL:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		count = db.update(CapturedPlayerMonsterDescriptor.TABLE_NAME, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(getClass().getName(), "delete : " + uri);
		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		int count;

		switch (path) {
		case ALL:
			count = db.delete(CapturedPlayerMonsterDescriptor.TABLE_NAME, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return count;
	}

}
