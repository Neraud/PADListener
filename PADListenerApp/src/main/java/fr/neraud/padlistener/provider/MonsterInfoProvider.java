package fr.neraud.padlistener.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
		Log.d(getClass().getName(), "onCreate");
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
		Log.d(getClass().getName(), "bulkInsert");
		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		switch (path) {
			case ALL_INFO:
				break;
			default:
				throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		final int count = values.length;
		for (int i = 0; i < count; i++) {
			db.insert(MonsterInfoDescriptor.TABLE_NAME, null, values[i]);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		Log.d(getClass().getName(), "bulkInsert finished");
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(getClass().getName(), "insert : " + uri);
		final SQLiteDatabase db = getDbHelper().getWritableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		if (path == null) {
			throw new UnsupportedOperationException("URI : " + uri + " not supported.");
		}

		db.insert(MonsterInfoDescriptor.TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(getClass().getName(), "query : " + uri);
		final SQLiteDatabase db = getDbHelper().getReadableDatabase();
		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);

		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(MonsterInfoDescriptor.TABLE_NAME);

		switch (path) {
			case INFO_BY_ID:
				// Add the ID
				builder.appendWhere(MonsterInfoDescriptor.Fields.ID_JP.getColName() + "=?");
				selectionArgs = MyDatabaseUtils.appendWhenreArgsToSelectionArgs(selectionArgs,
						new String[]{uri.getLastPathSegment()});
				break;
			case ALL_INFO:
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

		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(getClass().getName(), "delete : " + uri);
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

		return count;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		Log.d(getClass().getName(), "openFile : " + uri);

		final MonsterInfoDescriptor.Paths path = MonsterInfoDescriptor.matchUri(uri);
		switch (path) {
			case IMAGE_BY_ID:
				final String monsterId = uri.getLastPathSegment();

				final File imageDir = new File(getContext().getFilesDir().getPath() + "/monster_images");
				if (!imageDir.exists()) {
					imageDir.mkdir();
				}
				final File imagePath = new File(imageDir, monsterId + ".png");

				int imode = 0;
				if (mode.contains("w")) {
					imode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
					if (!imagePath.exists()) {
						try {
							imagePath.createNewFile();
						} catch (final IOException e) {
							Log.e(getClass().getName(), "openFile : error creating file " + imagePath.getAbsolutePath(), e);
						}
					}
				}
				if (mode.contains("r")) {
					imode |= ParcelFileDescriptor.MODE_READ_ONLY;
				}
				if (mode.contains("+")) {
					imode |= ParcelFileDescriptor.MODE_APPEND;
				}

				Log.d(getClass().getName(), "openFile : -> " + imagePath.getAbsolutePath());

				final ParcelFileDescriptor parcel = ParcelFileDescriptor.open(imagePath, imode);
				return parcel;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
}
