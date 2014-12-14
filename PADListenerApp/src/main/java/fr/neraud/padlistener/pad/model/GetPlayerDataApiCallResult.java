package fr.neraud.padlistener.pad.model;

import java.util.List;

import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.MonsterModel;

/**
 * Model representing the result of a PAD GetPlayerData call to GunHo servers
 *
 * @author Neraud
 */
public class GetPlayerDataApiCallResult extends BaseApiCallResult {

	private CapturedPlayerInfoModel playerInfo;
	private List<MonsterModel> monsterCards;
	private List<PADCapturedFriendModel> friends;

	public CapturedPlayerInfoModel getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(CapturedPlayerInfoModel playerInfo) {
		this.playerInfo = playerInfo;
	}

	public List<MonsterModel> getMonsterCards() {
		return monsterCards;
	}

	public void setMonsterCards(List<MonsterModel> monsterCards) {
		this.monsterCards = monsterCards;
	}

	public List<PADCapturedFriendModel> getFriends() {
		return friends;
	}

	public void setFriends(List<PADCapturedFriendModel> friends) {
		this.friends = friends;
	}
}
