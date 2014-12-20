package fr.neraud.padlistener.model;

import java.util.Date;

/**
 * Created by Neraud on 07/12/2014.
 */
public class CapturedFriendLeaderModel extends BaseMonsterStatsModel {

	private long friendId;
	private Date lastSeen;

	public long getFriendId() {
		return friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}
}
