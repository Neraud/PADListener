
package fr.neraud.padlistener.provider.sqlite.tables;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import fr.neraud.padlistener.provider.descriptor.PlayerInfoDescriptor;

/**
 * The player_info table
 * 
 * @author Neraud
 */
public class PlayerInfoTable implements ITable {

	@Override
	public String createTable() {
		return "CREATE TABLE " + PlayerInfoDescriptor.TABLE_NAME + " (" +
		/* */PlayerInfoDescriptor.Fields.FAKE_ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */PlayerInfoDescriptor.Fields.LAST_UPDATE.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.FRIEND_MAX.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.CARD_MAX.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.NAME.getColName() + " TEXT NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.RANK.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.EXP.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.CURRENT_LEVEL_EXP.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.NEXT_LEVEL_EXP.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.COST_MAX.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.STAMINA.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.STAMINA_MAX.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.STONES.getColName() + " INTEGER NOT NULL," +
		/* */PlayerInfoDescriptor.Fields.COINS.getColName() + " INTEGER NOT NULL" +
		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + PlayerInfoDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			Log.w(this.getClass().getName(), "Table " + PlayerInfoDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			Log.i(this.getClass().getName(), "Table " + PlayerInfoDescriptor.TABLE_NAME
			        + " is already up to date (hasn't changed since version " + getVersion() + ")");
		}

		return queries;
	}
}
