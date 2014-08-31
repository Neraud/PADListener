package fr.neraud.padlistener.provider;

import android.content.ContentProvider;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

import fr.neraud.padlistener.provider.descriptor.IField;
import fr.neraud.padlistener.provider.sqlite.PADListenerSQLiteOpenHelper;

/**
 * Base content provider used by all the content providers querying the padlistener database
 *
 * @author Neraud
 */
public abstract class AbstractPADListenerDbContentProvider extends ContentProvider {

	/**
	 * Initializes the SQLiteOpenHelper if necessary, and returns the instance
	 *
	 * @return the SQLiteOpenHelper
	 */
	protected SQLiteOpenHelper getDbHelper() {
		return PADListenerSQLiteOpenHelper.getInstance(getContext());
	}

	protected void fillColumnMapWithPrefix(Map<String, String> columnMap, String table, String prefix, IField... fields) {
		for (final IField field : fields) {
			columnMap.put(table + "." + field.getColName(), table + "." + field.getColName() + " AS " + prefix + field.getColName());
		}
	}
}
