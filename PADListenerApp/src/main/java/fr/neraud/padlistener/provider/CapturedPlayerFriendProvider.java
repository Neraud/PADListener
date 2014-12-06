package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * ContentProvider to manipulate the PlayerFriend
 *
 * @author Neraud
 */
public class CapturedPlayerFriendProvider extends AbstractPADListenerDbContentProvider {

	@Override
	public boolean onCreate() {
		MyLog.entry();
		MyLog.exit();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final CapturedPlayerFriendDescriptor.Paths path = CapturedPlayerFriendDescriptor.matchUri(uri);
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
		final CapturedPlayerFriendDescriptor.Paths path = CapturedPlayerFriendDescriptor.matchUri(uri);

		switch (path) {
			case ALL:
				break;
			default:
				throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		final int count = values.length;
		for (final ContentValues value : values) {
			db.insert(CapturedPlayerFriendDescriptor.TABLE_NAME, null, value);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerFriendDescriptor.Paths path = CapturedPlayerFriendDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(CapturedPlayerFriendDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerFriendDescriptor.Paths path = CapturedPlayerFriendDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		switch (path) {
			case ALL:
				builder.setTables(CapturedPlayerFriendDescriptor.TABLE_NAME);
				break;
			case ALL_WITH_INFO:
				final Map<String, String> columnMap = new HashMap<String, String>();
				fillColumnMapWithPrefix(columnMap, CapturedPlayerFriendDescriptor.TABLE_NAME, "", CapturedPlayerFriendDescriptor.Fields.values());

				final String leader1TableAlias = "L1";
				fillColumnMapWithPrefix(columnMap, leader1TableAlias, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER1_PREFIX, MonsterInfoDescriptor.Fields.values());
				final StringBuilder tableBuilder = new StringBuilder(CapturedPlayerFriendDescriptor.TABLE_NAME);
				tableBuilder.append(" LEFT OUTER JOIN ").append(MonsterInfoDescriptor.TABLE_NAME).append(" ").append(leader1TableAlias);
				tableBuilder.append(" ON ");
				tableBuilder.append(CapturedPlayerFriendDescriptor.TABLE_NAME).append(".").append(CapturedPlayerFriendDescriptor.Fields.LEADER1_ID_JP.getColName());
				tableBuilder.append(" = ");
				tableBuilder.append(leader1TableAlias).append(".").append(MonsterInfoDescriptor.Fields.ID_JP.getColName());
				tableBuilder.append("");

				final String leader2TableAlias = "L2";
				fillColumnMapWithPrefix(columnMap, leader2TableAlias, CapturedPlayerFriendDescriptor.ALL_WITH_INFO_LEADER2_PREFIX, MonsterInfoDescriptor.Fields.values());
				tableBuilder.append(" LEFT OUTER JOIN ").append(MonsterInfoDescriptor.TABLE_NAME).append(" ").append(leader2TableAlias);
				tableBuilder.append(" ON ");
				tableBuilder.append(CapturedPlayerFriendDescriptor.TABLE_NAME).append(".").append(CapturedPlayerFriendDescriptor.Fields.LEADER2_ID_JP.getColName());
				tableBuilder.append(" = ");
				tableBuilder.append(leader2TableAlias).append(".").append(MonsterInfoDescriptor.Fields.ID_JP.getColName());
				tableBuilder.append("");

				builder.setTables(tableBuilder.toString());
				builder.setProjectionMap(columnMap);
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
		final CapturedPlayerFriendDescriptor.Paths path = CapturedPlayerFriendDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case ALL:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		count = db.update(CapturedPlayerFriendDescriptor.TABLE_NAME, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerFriendDescriptor.Paths path = CapturedPlayerFriendDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case ALL:
				count = db.delete(CapturedPlayerFriendDescriptor.TABLE_NAME, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		MyLog.exit("uri = " + uri);
		return count;
	}

}
