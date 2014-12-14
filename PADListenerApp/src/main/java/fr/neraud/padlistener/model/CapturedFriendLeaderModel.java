package fr.neraud.padlistener.model;

import java.util.Date;

/**
 * Created by Neraud on 07/12/2014.
 */
public class CapturedFriendLeaderModel extends BaseMonsterStatsModel {

	private Date lastSeen;

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}
}
