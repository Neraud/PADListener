package fr.neraud.padlistener.provider.sqlite.tables;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.provider.descriptor.CapturedPlayerFriendLeaderDescriptor;

/**
 * The player_info table
 *
 * @author Neraud
 */
public class CapturedPlayerFriendLeaderTable implements ITable {

	@Override
	public String createTable() {
		return "CREATE TABLE " + CapturedPlayerFriendLeaderDescriptor.TABLE_NAME + " (" +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields._ID.getColName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.PLAYER_ID.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.LAST_SEEN.getColName() + " INTEGER NOT NULL," +
				
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.ID_JP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.SKILL_LEVEL.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_HP.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_ATK.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.PLUS_RCV.getColName() + " INTEGER NOT NULL," +
		/* */CapturedPlayerFriendLeaderDescriptor.Fields.AWAKENINGS.getColName() + " INTEGER NOT NULL" +
				
		/* */");";
	}

	@Override
	public String dropTable() {
		return "DROP TABLE IF EXISTS " + CapturedPlayerFriendLeaderDescriptor.TABLE_NAME;
	}

	@Override
	public int getVersion() {
		return 11;
	}

	@Override
	public List<String> upgrade(int oldVersion, int newVersion) {
		final List<String> queries = new ArrayList<String>();

		if (oldVersion < getVersion()) {
			MyLog.warn("Table " + CapturedPlayerFriendLeaderDescriptor.TABLE_NAME + " has changed, destroying table !");
			queries.add(dropTable());
			queries.add(createTable());
		} else {
			MyLog.info("Table " + CapturedPlayerFriendLeaderDescriptor.TABLE_NAME + " is already up to date (hasn't changed since version " + getVersion() + ")");
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
