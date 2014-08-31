package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * FriendModel for captured data with leader info
 *
 * @author Neraud
 */
public class CapturedFriendFullInfoModel implements Serializable {

	private CapturedFriendModel friendModel;
	private MonsterInfoModel leader1Info;
	private MonsterInfoModel leader2Info;

	public CapturedFriendModel getFriendModel() {
		return friendModel;
	}

	public void setFriendModel(CapturedFriendModel friendModel) {
		this.friendModel = friendModel;
	}

	public MonsterInfoModel getLeader1Info() {
		return leader1Info;
	}

	public void setLeader1Info(MonsterInfoModel leader1Info) {
		this.leader1Info = leader1Info;
	}

	public MonsterInfoModel getLeader2Info() {
		return leader2Info;
	}

	public void setLeader2Info(MonsterInfoModel leader2Info) {
		this.leader2Info = leader2Info;
	}
}
