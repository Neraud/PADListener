
package fr.neraud.padlistener.provider;

import android.content.ContentProvider;
import android.database.sqlite.SQLiteOpenHelper;
import fr.neraud.padlistener.provider.sqlite.PADListenerSQLiteOpenHelper;

/**
 * Base content provider used by all the content providers querying the padlistener database
 * 
 * @author Neraud
 */
public abstract class AbstractPADListenerDbContentProvider extends ContentProvider {

	private SQLiteOpenHelper sqlHelper = null;

	/**
	 * Initializes the SQLiteOpenHelper if necessary, and returns the instance
	 * 
	 * @return the SQLiteOpenHelper
	 */
	protected SQLiteOpenHelper getDbHelper() {
		if (sqlHelper == null) {
			sqlHelper = new PADListenerSQLiteOpenHelper(getContext());
		}
		return sqlHelper;
	}
}
