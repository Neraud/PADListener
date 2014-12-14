package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * ContentProvider to manipulate the PlayerFriendLeader
 *
 * @author Neraud
 */
public class CapturedPlayerFriendLeaderProvider extends AbstractPADListenerDbContentProvider {

	@Override
	public boolean onCreate() {
		MyLog.entry();
		MyLog.exit();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final CapturedPlayerFriendLeaderDescriptor.Paths path = CapturedPlayerFriendLeaderDescriptor.matchUri(uri);
		if (path != null) {
			return path.getContentType();
		} else {
			throw new UnsupportedOperationException("URI " + uri + " is not supported.");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final CapturedPlayerFriendLeaderDescriptor.Paths path = CapturedPlayerFriendLeaderDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(CapturedPlayerFriendLeaderDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerFriendLeaderDescriptor.Paths path = CapturedPlayerFriendLeaderDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		switch (path) {
			case ALL:
				builder.setTables(CapturedPlayerFriendLeaderDescriptor.TABLE_NAME);
				break;
			case ALL_WITH_INFO:
				final Map<String, String> columnMap = new HashMap<String, String>();
				fillColumnMapWithPrefix(columnMap, CapturedPlayerFriendLeaderDescriptor.TABLE_NAME, "", CapturedPlayerFriendLeaderDescriptor.Fields.values());

				final StringBuilder tableBuilder = new StringBuilder(CapturedPlayerFriendLeaderDescriptor.TABLE_NAME);

				final String infoTableAlias = "INFO";
				fillColumnMapWithPrefix(columnMap, infoTableAlias, CapturedPlayerFriendLeaderDescriptor.ALL_WITH_INFO_PREFIX, MonsterInfoDescriptor.Fields.values());
				tableBuilder.append(" LEFT OUTER JOIN ").append(MonsterInfoDescriptor.TABLE_NAME).append(" ").append(infoTableAlias);
				tableBuilder.append(" ON ");
				tableBuilder.append(CapturedPlayerFriendLeaderDescriptor.TABLE_NAME).append(".").append(CapturedPlayerFriendLeaderDescriptor.Fields.ID_JP.getColName());
				tableBuilder.append(" = ");
				tableBuilder.append(infoTableAlias).append(".").append(MonsterInfoDescriptor.Fields.ID_JP.getColName());
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
		final CapturedPlayerFriendLeaderDescriptor.Paths path = CapturedPlayerFriendLeaderDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case ALL:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		count = db.update(CapturedPlayerFriendLeaderDescriptor.TABLE_NAME, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		MyLog.exit();
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		MyLog.entry("uri = " + uri);

		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final CapturedPlayerFriendLeaderDescriptor.Paths path = CapturedPlayerFriendLeaderDescriptor.matchUri(uri);

		int count;

		switch (path) {
			case ALL:
				count = db.delete(CapturedPlayerFriendLeaderDescriptor.TABLE_NAME, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		MyLog.exit("uri = " + uri);
		return count;
	}

}
