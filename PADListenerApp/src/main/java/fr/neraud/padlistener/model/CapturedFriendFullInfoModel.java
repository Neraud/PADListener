package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * FriendModel for captured data with leader info
 *
 * @author Neraud
 */
public class CapturedFriendFullInfoModel implements Serializable {

	private CapturedFriendModel friendModel;
	private BaseMonsterStatsModel leader1;
	private BaseMonsterStatsModel leader2;
	private MonsterInfoModel leader1Info;
	private MonsterInfoModel leader2Info;

	public CapturedFriendModel getFriendModel() {
		return friendModel;
	}

	public void setFriendModel(CapturedFriendModel friendModel) {
		this.friendModel = friendModel;
	}

	public BaseMonsterStatsModel getLeader1() {
		return leader1;
	}

	public void setLeader1(BaseMonsterStatsModel leader1) {
		this.leader1 = leader1;
	}

	public BaseMonsterStatsModel getLeader2() {
		return leader2;
	}

	public void setLeader2(BaseMonsterStatsModel leader2) {
		this.leader2 = leader2;
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
