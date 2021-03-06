package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import fr.neraud.log.MyLog;
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
		MyLog.entry();
		MyLog.exit();
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
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		switch (path) {
			case ALL:
				break;
			default:
				throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		final int count = values.length;
		for (final ContentValues value : values) {
			db.insert(CapturedPlayerMonsterDescriptor.TABLE_NAME, null, value);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(CapturedPlayerMonsterDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerMonsterDescriptor.Paths path = CapturedPlayerMonsterDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		switch (path) {
			case ALL:
				builder.setTables(CapturedPlayerMonsterDescriptor.TABLE_NAME);
				break;
			case ALL_WITH_INFO:
				final StringBuilder tableBuilder = new StringBuilder(CapturedPlayerMonsterDescriptor.TABLE_NAME);
				tableBuilder.append(" LEFT OUTER JOIN ").append(MonsterInfoDescriptor.TABLE_NAME);
				tableBuilder.append(" ON (");
				tableBuilder.append(CapturedPlayerMonsterDescriptor.TABLE_NAME).append(".")
						.append(CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID_JP.getColName());
				tableBuilder.append(" = ");
				tableBuilder.append(MonsterInfoDescriptor.TABLE_NAME).append(".").append(MonsterInfoDescriptor.Fields.ID_JP.getColName());
				tableBuilder.append(")");

				builder.setTables(tableBuilder.toString());
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

		MyLog.exit();
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		MyLog.entry("uri = " + uri);

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

		MyLog.exit();
		return count;
	}

}
