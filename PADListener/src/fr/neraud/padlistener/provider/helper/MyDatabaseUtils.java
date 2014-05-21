
package fr.neraud.padlistener.provider.helper;

import android.database.DatabaseUtils;

/**
 * Utilities to manipulate a databases
 * 
 * @author Neraud
 */
public class MyDatabaseUtils extends DatabaseUtils {

	/**
	 * @param selectionArgs the original selectionArgs (if any)
	 * @param whereArgs the whereArgs (if any)
	 * @return both sets appended (whereArgs first)
	 */
	public static String[] appendWhenreArgsToSelectionArgs(String[] selectionArgs, String[] whereArgs) {
		if (whereArgs == null || whereArgs.length == 0) {
			return selectionArgs;
		}
		if (selectionArgs == null || selectionArgs.length == 0) {
			return whereArgs;
		}
		final String[] result = new String[selectionArgs.length + whereArgs.length];
		System.arraycopy(whereArgs, 0, result, 0, whereArgs.length);
		System.arraycopy(selectionArgs, 0, result, whereArgs.length, selectionArgs.length);
		return result;
	}
}
