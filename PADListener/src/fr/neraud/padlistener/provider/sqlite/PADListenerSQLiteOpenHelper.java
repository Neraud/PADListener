
package fr.neraud.padlistener.provider.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.neraud.padlistener.provider.sqlite.tables.ITable;
import fr.neraud.padlistener.provider.sqlite.tables.MonsterInfoTable;
import fr.neraud.padlistener.provider.sqlite.tables.CapturedPlayerInfoTable;
import fr.neraud.padlistener.provider.sqlite.tables.CapturedPlayerMonsterTable;

/**
 * OpenHelper to access the accounts table in the database
 * 
 * @author Neraud
 */
public class PADListenerSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 5;
	private static final String DATABASE_NAME = "padlistener.db";

	private static final List<ITable> TABLES = new ArrayList<ITable>();
	static {
		addTable(new CapturedPlayerInfoTable());
		addTable(new CapturedPlayerMonsterTable());
		addTable(new MonsterInfoTable());
	}

	public PADListenerSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
	}

	private static void addTable(ITable table) {
		TABLES.add(table);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(this.getClass().getName(), "Creating new DB");

		for (final ITable table : TABLES) {
			db.execSQL(table.createTable());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(this.getClass().getName(), "Upgrading database from version " + oldVersion + " to " + newVersion);

		for (final ITable table : TABLES) {
			for (final String query : table.upgrade(oldVersion, newVersion)) {
				db.execSQL(query);
			}
		}
	}
}
