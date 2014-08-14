package fr.neraud.padlistener.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.constant.SyncMaterialInMonster;
import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * Helper to access the default SharedPreferences
 *
 * @author Neraud
 */
public class DefaultSharedPreferencesHelper extends AbstractSharedPreferencesHelper {

	public DefaultSharedPreferencesHelper(Context context) {
		super(PreferenceManager.getDefaultSharedPreferences(context));
	}

	public Set<String> getAllListenerTargetHostnames() {
		final Set<String> targetHostNames = new HashSet<String>();

		final Set<String> selectedTargetHostNames = getStringSetPreference("listener_target_hostnames", new HashSet<String>());
		final String customTargetHostName = getStringPreference("listener_custom_target_hostname", null);

		targetHostNames.addAll(selectedTargetHostNames);
		if (StringUtils.isNotBlank(customTargetHostName)) {
			targetHostNames.add(customTargetHostName);
		}

		return targetHostNames;
	}

	public ProxyMode getProxyMode() {
		return ProxyMode.valueOf(getStringPreference("listener_proxy_mode", "MANUAL"));
	}

	public boolean isListenerAutoShutdown() {
		return getBooleanPreference("listener_auto_shutdown", true);
	}

	public boolean isListenerNonLocalEnabled() {
		return getBooleanPreference("listener_non_local_enabled", false);
	}

	public int getPadHerderAccountNumber() {
		return getIntFromStringPreference("padherder_account_number", 3);
	}

	@SuppressLint("UseSparseArrays")
	public Map<Integer, String> getPadHerderAccounts() {
		final Map<Integer, String> result = new HashMap<Integer, String>();
		for (int i = 1; i <= getPadHerderAccountNumber(); i++) {
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

	public MonsterPriority getDefaultMonsterCreatePriority() {
		return MonsterPriority.valueOf(getStringPreference("default_monster_create_priority", MonsterPriority.MED.name()));
	}
}
