package fr.neraud.padlistener.provider.sqlite.tables;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;

/**
 * The player_info table
 *
 * @author Neraud
 */
public class CapturedPlayerFriendTable implements ITable {

	@Override
	public String createTable() {
		return "CREATE TABLE " + CapturedPlayerFriendDescriptor.TABLE_NAME + " (" +
		/* */CapturedPlayerFriendDescriptor.Fields.FAKE_ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */CapturedPlayerFriendDescriptor.Fields.ID.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.NAME.getColName() + " TEXT NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.RANK.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.STARTING_COLOR.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LAST_ACTIVITY.getColName() + " INTEGER NOT NULL," +

		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_ID_JP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_SKILL_LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_HP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_ATK.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_PLUS_RCV.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_AWAKENINGS.getColName() + " INTEGER NOT NULL," +

		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_ID_JP.getColName() + " INTEGER," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_LEVEL.getColName() + " INTEGER," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_SKILL_LEVEL.getColName() + " INTEGER," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_HP.getColName() + " INTEGER," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_ATK.getColName() + " INTEGER," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_PLUS_RCV.getColName() + " INTEGER," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_AWAKENINGS.getColName() + " INTEGER" +

		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + CapturedPlayerFriendDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 10;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			Log.w(getClass().getName(), "Table " + CapturedPlayerFriendDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());

		} else {
			Log.i(getClass().getName(), "Table " + CapturedPlayerFriendDescriptor.TABLE_NAME
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
