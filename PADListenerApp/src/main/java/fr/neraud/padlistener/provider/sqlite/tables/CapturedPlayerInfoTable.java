package fr.neraud.padlistener.provider.sqlite.tables;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.provider.descriptor.CapturedPlayerInfoDescriptor;

/**
 * The player_info table
 *
 * @author Neraud
 */
public class CapturedPlayerInfoTable implements ITable {

	@Override
	public String createTable() {
		return "CREATE TABLE " + CapturedPlayerInfoDescriptor.TABLE_NAME + " (" +
		/* */CapturedPlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */CapturedPlayerInfoDescriptor.Fields.LAST_UPDATE.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.FRIEND_MAX.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.CARD_MAX.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.NAME.getColName() + " TEXT NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.RANK.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.EXP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.CURRENT_LEVEL_EXP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.NEXT_LEVEL_EXP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.COST_MAX.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.STAMINA.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.STAMINA_MAX.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.STONES.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.COINS.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerInfoDescriptor.Fields.REGION.getColName() + " TEXT NOT NULL" +
		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + CapturedPlayerInfoDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 9;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			Log.w(getClass().getName(), "Table " + CapturedPlayerInfoDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			Log.i(getClass().getName(), "Table " + CapturedPlayerInfoDescriptor.TABLE_NAME
					+ " is already up to date (hasn't changed since version " + getVersion() + ")");
		}

		return queries;
	}

	@Override
	public void preUpgrade(Context context, int oldVersion, int newVersion) {

	}

	@Override
	public void postUpgrade(Context context, int oldVersion, int newVersion) {

	}
}
