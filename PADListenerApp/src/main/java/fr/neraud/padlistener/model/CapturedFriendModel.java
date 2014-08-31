package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.Date;

import fr.neraud.padlistener.pad.constant.StartingColor;

/**
 * FriendModel for captured data
 *
 * @author Neraud
 */
public class CapturedFriendModel implements Serializable {

	private long id;
	private String name;
	private int rank;
	private StartingColor startingColor;
	private Date lastActivityDate;
	private CapturedFriendLeaderModel leader1;
	private CapturedFriendLeaderModel leader2;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public StartingColor getStartingColor() {
		return startingColor;
	}

	public void setStartingColor(StartingColor startingColor) {
		this.startingColor = startingColor;
	}

	public Date getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(Date lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public CapturedFriendLeaderModel getLeader1() {
		return leader1;
	}

	public void setLeader1(CapturedFriendLeaderModel leader1) {
		this.leader1 = leader1;
	}

	public CapturedFriendLeaderModel getLeader2() {
		return leader2;
	}

	public void setLeader2(CapturedFriendLeaderModel leader2) {
		this.leader2 = leader2;
	}

	@Override
	public String toString() {
		return "(" + id + ") " + name;
	}
}
