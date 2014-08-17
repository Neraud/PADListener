package fr.neraud.padlistener.model;

import java.util.List;

/**
 * Created by Neraud on 16/08/2014.
 */
public class IgnoreMonsterQuickActionModel {

	private String quickActionName;
	private List<Integer> monsterIds;

	public String getQuickActionName() {
		return quickActionName;
	}

	public void setQuickActionName(String quickActionName) {
		this.quickActionName = quickActionName;
	}

	public List<Integer> getMonsterIds() {
		return monsterIds;
	}

	public void setMonsterIds(List<Integer> monsterIds) {
		this.monsterIds = monsterIds;
	}

	public String toString()  {
		return quickActionName + " : " + monsterIds;
	}
}
