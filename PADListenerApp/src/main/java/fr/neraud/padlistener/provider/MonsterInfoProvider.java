package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MyDatabaseUtils;

/**
 * ContentProvider to manipulate the MonsterInfo
 *
 * @author Neraud
 */
public class MonsterInfoProvider extends AbstractPADListenerDbContentProvider {

	@Override
	public boolean onCreate() {
		MyLog.entry();
		MyLog.exit();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);
		if (path != null) {
			return path.getContentType();
		} else {
			throw new UnsupportedOperationException("URI " + uri + " is not supported.");
		}
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		switch (path) {
			case ALL_INFO:
				break;
			default:
				throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		final int count = values.length;
		for (final ContentValues value : values) {
			db.insert(MonsterInfoDescriptor.TABLE_NAME, null, value);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(MonsterInfoDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(MonsterInfoDescriptor.TABLE_NAME);

		switch (path) {
			case INFO_BY_ID:
				// Add the ID
				builder.appendWhere(MonsterInfoDescriptor.Fields.ID_JP.getColName() + "=?");
				selectionArgs = MyDatabaseUtils.appendWhereArgsToSelectionArgs(selectionArgs,
						new String[]{uri.getLastPathSegment()});
				break;
			case ALL_INFO:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		final Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		MyLog.exit();
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case INFO_BY_ID:
				final StringBuilder selectionBuilder = new StringBuilder();
				if (selection != null) {
					selectionBuilder.append(selection).append(" AND ");
				}
				selectionBuilder.append(MonsterInfoDescriptor.Fields.ID_JP.getColName()).append("=?");
				selectionArgs = MyDatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{uri.getLastPathSegment()});

				count = db.update(MonsterInfoDescriptor.TABLE_NAME, values, selectionBuilder.toString(), selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				getContext().getContentResolver().notifyChange(MonsterInfoDescriptor.UriHelper.uriForAll(), null);
				break;
			case ALL_INFO:
				count = db.update(MonsterInfoDescriptor.TABLE_NAME, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		MyLog.exit();
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case INFO_BY_ID:
				final StringBuilder selectionBuilder = new StringBuilder();
				if (selection != null) {
					selectionBuilder.append(selection).append(" AND ");
				}
				selectionBuilder.append(MonsterInfoDescriptor.Fields.ID_JP.getColName()).append("=?");

				selectionArgs = MyDatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{uri.getLastPathSegment()});

				count = db.delete(MonsterInfoDescriptor.TABLE_NAME, selectionBuilder.toString(), selectionArgs);

				getContext().getContentResolver().notifyChange(uri, null);
				getContext().getContentResolver().notifyChange(MonsterInfoDescriptor.UriHelper.uriForAll(), null);
				break;
			case ALL_INFO:
				count = db.delete(MonsterInfoDescriptor.TABLE_NAME, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		MyLog.exit();
		return count;
	}

}
