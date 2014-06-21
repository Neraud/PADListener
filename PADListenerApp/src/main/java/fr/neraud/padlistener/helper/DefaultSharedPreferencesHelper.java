
package fr.neraud.padlistener.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;
import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.constant.SyncMaterialInMonster;

/**
 * Helper to access the default SharedPreferences
 * 
 * @author Neraud
 */
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

	@SuppressLint("UseSparseArrays")
	public Map<Integer, String> getPadHerderAccounts() {
		final Map<Integer, String> result = new HashMap<Integer, String>();
		for (int i = 1; i <= 3; i++) {
			if (StringUtils.isNotBlank(getPadHerderUserName(i)) && StringUtils.isNotBlank(getPadHerderUserPassword(i))) {
				result.put(i, getPadHerderUserName(i));
			}
		}

		return result;
	}

	public String getPadHerderUserName(int accountId) {
		return getStringPreference("padherder_login_" + accountId, null);
	}

	public String getPadHerderUserPassword(int accountId) {
		return getStringPreference("padherder_password_" + accountId, null);
	}

	public SyncMaterialInMonster getSyncMaterialInMonster() {
		return SyncMaterialInMonster.valueOf(getStringPreference("sync_material_in_monster", "ONLY_IF_ALREADY_IN_PADHERDER"));
	}

	public boolean isSyncDeductMonsterInMaterial() {
		return getBooleanPreference("sync_deduct_monster_in_material", true);
	}

	public boolean isChooseSyncPreselectUserInfoToUpdate() {
		return getBooleanPreference("choose_sync_preselect_updated_user_info", true);
	}

	public boolean isChooseSyncPreselectMaterialsUpdated() {
		return getBooleanPreference("choose_sync_preselect_updated_materials", true);
	}

	public boolean isChooseSyncPreselectMonstersUpdated() {
		return getBooleanPreference("choose_sync_preselect_updated_monsters", true);
	}

	public boolean isChooseSyncPreselectMonstersCreated() {
		return getBooleanPreference("choose_sync_preselect_created_monsters", false);
	}

	public boolean isChooseSyncPreselectMonstersDeleted() {
		return getBooleanPreference("choose_sync_preselect_deleted_monsters", false);
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

	public PADRegion getPlayerRegion() {
		// TODO use a proper settings if JP is an option ?
		return PADRegion.US;
	}

}
