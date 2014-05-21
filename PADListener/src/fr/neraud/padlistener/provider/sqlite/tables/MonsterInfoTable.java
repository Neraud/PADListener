
package fr.neraud.padlistener.provider.sqlite.tables;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * The monster_info table
 * 
 * @author Neraud
 */
public class MonsterInfoTable implements ITable {

	@Override
	public String createTable() {
		return "CREATE TABLE " + MonsterInfoDescriptor.TABLE_NAME + " (" +
		/* */MonsterInfoDescriptor.Fields.ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */MonsterInfoDescriptor.Fields.NAME.getColName() + " TEXT NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.RARITY.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.ELEMENT_1.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.ELEMENT_2.getColName() + " INTEGER," +
		/* */MonsterInfoDescriptor.Fields.TYPE_1.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.TYPE_2.getColName() + " INTEGER," +
		/* */MonsterInfoDescriptor.Fields.ACTIVE_SKILL_NAME.getColName() + " INTEGER," +
		/* */MonsterInfoDescriptor.Fields.LEADER_SKILL_NAME.getColName() + " INTEGER," +
		/* */MonsterInfoDescriptor.Fields.AWOKEN_SKILL_IDS.getColName() + " INTEGER," +
		/* */MonsterInfoDescriptor.Fields.MAX_LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.XP_CURVE.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.FEED_XP.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.TEAM_COST.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.JP_ONLY.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.HP_MIN.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.HP_MAX.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.HP_SCALE.getColName() + " REAL NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.ATK_MIN.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.ATK_MAX.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.ATK_SCALE.getColName() + " REAL NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.RCV_MIN.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.RCV_MAX.getColName() + " INTEGER NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.RCV_SCALE.getColName() + " REAL NOT NULL," +
		/* */MonsterInfoDescriptor.Fields.IMAGE_40_URL.getColName() + " TEXT," +
		/* */MonsterInfoDescriptor.Fields.IMAGE_60_URL.getColName() + " TEXT" +
		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + MonsterInfoDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 5;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			Log.w(this.getClass().getName(), "Table " + MonsterInfoDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			Log.i(this.getClass().getName(), "Table " + MonsterInfoDescriptor.TABLE_NAME
			        + " is already up to date (hasn't changed since version " + getVersion() + ")");
		}

		return queries;
	}
}
