
package fr.neraud.padlistener.pad.model;

import java.util.List;

import fr.neraud.padlistener.model.MonsterCardModel;
import fr.neraud.padlistener.model.PlayerInfoModel;

public class GetPlayerDataApiCallResult extends BaseApiCallResult {

	private PlayerInfoModel playerInfo;
	private List<MonsterCardModel> monsterCards;

	public PlayerInfoModel getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(PlayerInfoModel playerInfo) {
		this.playerInfo = playerInfo;
	}

	public List<MonsterCardModel> getMonsterCards() {
		return monsterCards;
	}

	public void setMonsterCards(List<MonsterCardModel> monsterCards) {
		this.monsterCards = monsterCards;
	}

}
