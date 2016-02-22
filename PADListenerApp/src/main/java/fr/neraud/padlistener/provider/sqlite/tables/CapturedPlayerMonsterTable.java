package fr.neraud.padlistener.provider.sqlite.tables;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
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
		/* */CapturedPlayerMonsterDescriptor.Fields.CARD_ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */CapturedPlayerMonsterDescriptor.Fields.MONSTER_ID_JP.getColName() + " INTEGER," +
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
		return 9;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			MyLog.warn("Table " + CapturedPlayerMonsterDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			MyLog.info("Table " + CapturedPlayerMonsterDescriptor.TABLE_NAME + " is already up to date (hasn't changed since version " + getVersion() + ")");
		}

		return queries;
	}

	@Override
	public void preUpgrade(Context context, int oldVersion, int newVersion) {

	}

	@Override
	public void postUpgrade(Context context, int oldVersion, int newVersion) {
		if (oldVersion < getVersion()) {
			// Clear data from the technical preferences
			final TechnicalSharedPreferencesHelper helper = new TechnicalSharedPreferencesHelper(context);
			helper.setLastCaptureName(null);
			helper.setLastCaptureDate(new Date(0L));
		}
	}
}
