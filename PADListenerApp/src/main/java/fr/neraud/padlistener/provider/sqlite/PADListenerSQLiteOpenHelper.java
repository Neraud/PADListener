package fr.neraud.padlistener.provider.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.sqlite.tables.CapturedPlayerFriendLeaderTable;
import fr.neraud.padlistener.provider.sqlite.tables.CapturedPlayerFriendTable;
import fr.neraud.padlistener.provider.sqlite.tables.CapturedPlayerInfoTable;
import fr.neraud.padlistener.provider.sqlite.tables.CapturedPlayerMonsterTable;
import fr.neraud.padlistener.provider.sqlite.tables.ITable;
import fr.neraud.padlistener.provider.sqlite.tables.MonsterInfoTable;

/**
 * OpenHelper to access the accounts table in the database
 *
 * @author Neraud
 */
public class PADListenerSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 12;
	private static final String DATABASE_NAME = "padlistener.db";

	private static final List<ITable> TABLES = new ArrayList<ITable>();
	private static PADListenerSQLiteOpenHelper INSTANCE;

	static {
		addTable(new CapturedPlayerInfoTable());
		addTable(new CapturedPlayerMonsterTable());
		addTable(new MonsterInfoTable());
		addTable(new CapturedPlayerFriendTable());
		addTable(new CapturedPlayerFriendLeaderTable());
	}

	private final Context context;

	private PADListenerSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
		this.context = context;
	}

	/**
	 * Initializes the SQLiteOpenHelper if necessary, and returns the instance
	 *
	 * @param context the context
	 * @return the SQLiteOpenHelper
	 */
	public static synchronized PADListenerSQLiteOpenHelper getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new PADListenerSQLiteOpenHelper(context);
		}
		return INSTANCE;
	}

	private static void addTable(ITable table) {
		TABLES.add(table);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		MyLog.entry();

		MyLog.info("Creating new DB");
		for (final ITable table : TABLES) {
			db.execSQL(table.createTable());
		}

		MyLog.exit();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MyLog.entry();

		MyLog.info("Upgrading database from version " + oldVersion + " to " + newVersion);

		for (final ITable table : TABLES) {
			table.preUpgrade(context, oldVersion, newVersion);
			for (final String query : table.upgrade(oldVersion, newVersion)) {
				db.execSQL(query);
			}
			table.postUpgrade(context, oldVersion, newVersion);
		}

		MyLog.exit();
	}
}
