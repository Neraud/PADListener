package fr.neraud.padlistener.provider.sqlite.tables;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendDescriptor;
import fr.neraud.padlistener.provider.helper.BaseProviderHelper;

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
		/* */CapturedPlayerFriendDescriptor.Fields.FAVOURITE.getColName() + " INTEGER NOT NULL DEFAULT " + BaseProviderHelper.BOOLEAN_FALSE + "," +
		/* */CapturedPlayerFriendDescriptor.Fields.STARTING_COLOR.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LAST_ACTIVITY.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER1_ID.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendDescriptor.Fields.LEADER2_ID.getColName() + " INTEGER" +
		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + CapturedPlayerFriendDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 11;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			MyLog.warn("Table " + CapturedPlayerFriendDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			MyLog.info("Table " + CapturedPlayerFriendDescriptor.TABLE_NAME + " is already up to date (hasn't changed since version " + getVersion() + ")");
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
