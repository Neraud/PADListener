
package fr.neraud.padlistener.helper;

import android.content.Context;
import android.preference.PreferenceManager;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.constant.SyncMaterialInMonster;

public class DefaultSharedPreferencesHelper extends AbstractSharedPreferencesHelper {

	public DefaultSharedPreferencesHelper(Context context) {
		super(PreferenceManager.getDefaultSharedPreferences(context));
	}

	public String getListenerTargetHostname() {
		return getStringPreference("listener_target_hostname", "api-na-adr-pad.gungho.jp");
	}

	public ProxyMode getProxyMode() {
		return ProxyMode.valueOf(getStringPreference("listener_proxy_mode", "MANUAL"));
	}

	public String getPadHerderUserName() {
		return getStringPreference("padherder_login", null);
	}

	public String getPadHerderUserPassword() {
		return getStringPreference("padherder_password", null);
	}

	public SyncMaterialInMonster getSyncMaterialInMonster() {
		return SyncMaterialInMonster.valueOf(getStringPreference("sync_material_in_monster", "ONLY_IF_ALREADY_IN_PADHERDER"));
	}

	public boolean isSyncDeductMonsterInMaterial() {
		return getBooleanPreference("sync_deduct_monster_in_material", true);
	}

	public boolean isChooseSyncGroupMonstersUpdated() {
		return getBooleanPreference("choose_sync_group_monsters_updated", false);
	}

	public boolean isChooseSyncGroupMonstersCreated() {
		return getBooleanPreference("choose_sync_group_monsters_created", true);
	}

	public boolean isChooseSyncGroupMonstersDeleted() {
		return getBooleanPreference("choose_sync_group_monsters_deleted", false);
	}

}
