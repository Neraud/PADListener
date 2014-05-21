
package fr.neraud.padlistener.provider.sqlite.tables;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerMonsterDescriptor;

/**
 * The player_info table
 * 
 * @author Neraud
 */
public class CapturedPlayerMonsterTable implements ITable {

	@Override
	public String createTable() {
		return "CREATE TABLE " + CapturedPlayerMonsterDescriptor.TABLE_NAME + " (" +
		/* */CapturedPlayerMonsterDescriptor.Fields.FAKE_ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.EXP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.SKILL_LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.PLUS_HP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.PLUS_ATK.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.PLUS_RCV.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerMonsterDescriptor.Fields.AWAKENINGS.getColName() + " INTEGER NOT NULL" +
		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + CapturedPlayerMonsterDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 3;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			Log.w(this.getClass().getName(), "Table " + CapturedPlayerMonsterDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			Log.i(this.getClass().getName(), "Table " + CapturedPlayerMonsterDescriptor.TABLE_NAME
			        + " is already up to date (hasn't changed since version " + getVersion() + ")");
		}

		return queries;
	}
}
