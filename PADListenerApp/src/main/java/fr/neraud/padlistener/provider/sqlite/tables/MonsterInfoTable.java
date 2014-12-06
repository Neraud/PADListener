package fr.neraud.padlistener.provider.sqlite.tables;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
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
		/* */MonsterInfoDescriptor.Fields.ID_JP.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */MonsterInfoDescriptor.Fields.ID_US.getColName() + " INTEGER, " +
		/* */MonsterInfoDescriptor.Fields.BASE_ID_JP.getColName() + " INTEGER, " +
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
		/* */MonsterInfoDescriptor.Fields.EVOLUTION_STAGE.getColName() + " INTEGER, " +
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
		return 8;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			MyLog.warn("Table " + MonsterInfoDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			MyLog.info("Table " + MonsterInfoDescriptor.TABLE_NAME + " is already up to date (hasn't changed since version " + getVersion() + ")");
		}
		return queries;
	}

	@Override
	public void preUpgrade(Context context, int oldVersion, int newVersion) {

	}

	@Override
	public void postUpgrade(Context context, int oldVersion, int newVersion) {
		if (oldVersion < getVersion()) {
			// Start the install again to restore monsterInfo data
			final TechnicalSharedPreferencesHelper helper = new TechnicalSharedPreferencesHelper(context);
			helper.setHasBeenInstalled(false);
			helper.setMonsterInfoRefreshDate(new Date(0L));
		}
	}
}
