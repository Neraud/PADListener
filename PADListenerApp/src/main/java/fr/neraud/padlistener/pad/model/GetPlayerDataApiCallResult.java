package fr.neraud.padlistener.pad.model;

import java.util.List;

import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.CapturedMonsterCardModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;

/**
 * Model representing the result of a PAD GetPlayerData call to GunHo servers
 *
 * @author Neraud
 */
public class GetPlayerDataApiCallResult extends BaseApiCallResult {

	private CapturedPlayerInfoModel playerInfo;
	private List<CapturedMonsterCardModel> monsterCards;
	private List<CapturedFriendModel> friends;

	public CapturedPlayerInfoModel getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(CapturedPlayerInfoModel playerInfo) {
		this.playerInfo = playerInfo;
	}

	public List<CapturedMonsterCardModel> getMonsterCards() {
		return monsterCards;
	}

	public void setMonsterCards(List<CapturedMonsterCardModel> monsterCards) {
		this.monsterCards = monsterCards;
	}

	public List<CapturedFriendModel> getFriends() {
		return friends;
	}

	public void setFriends(List<CapturedFriendModel> friends) {
		this.friends = friends;
	}
}
