package fr.neraud.padlistener.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.constant.SyncMaterialInMonster;
import fr.neraud.padlistener.constant.SyncMode;
import fr.neraud.padlistener.model.PADHerderAccountModel;
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

		final Set<String> selectedTargetServers = getStringSetPreference("listener_target_servers", new HashSet<String>());
		final String customTargetHostName = getStringPreference("listener_custom_target_hostname", null);

		for (final String selectedTargetServer : selectedTargetServers) {
			final PADVersion server = PADVersion.fromName(selectedTargetServer);
			if (server != null) {
				targetHostNames.add(server.getServerHostName());
			} else {
				MyLog.warn("ignoring unknown server : " + selectedTargetServer);
			}
		}

		if (StringUtils.isNotBlank(customTargetHostName)) {
			targetHostNames.add(customTargetHostName);
		}

		return targetHostNames;
	}

	public ProxyMode getProxyMode() {
		return ProxyMode.valueOf(getStringPreference("listener_proxy_mode", "MANUAL"));
	}

	public PADVersion getAutoCaptureServer() {
		return PADVersion.valueOf(getStringPreference("listener_auto_capture_server", "US"));
	}

	public boolean isListenerAutoShutdown() {
		return getBooleanPreference("listener_auto_shutdown", true);
	}

	public boolean isListenerNonLocalEnabled() {
		return getBooleanPreference("listener_non_local_enabled", false);
	}

	public int getPadHerderAccountNumber() {
		return getIntFromStringPreference("padherder_account_number", 1);
	}

	@SuppressLint("UseSparseArrays")
	public List<PADHerderAccountModel> getPadHerderAccounts() {
		final List<PADHerderAccountModel> results = new ArrayList<PADHerderAccountModel>();
		for (int i = 1; i <= getPadHerderAccountNumber(); i++) {
			if (StringUtils.isNotBlank(getPadHerderUserName(i)) && StringUtils.isNotBlank(getPadHerderUserPassword(i))) {
				final PADHerderAccountModel account = new PADHerderAccountModel();
				account.setAccountId(i);
				account.setLogin(getPadHerderUserName(i));
				account.setPassword(getPadHerderUserPassword(i));
				account.setName(getPadHerderName(i));
				results.add(account);
			}
		}

		return results;
	}

	public String getPadHerderName(int accountId) {
		return StringUtils.trim(getStringPreference("padherder_name_" + accountId, null));
	}

	public String getPadHerderUserName(int accountId) {
		return StringUtils.trim(getStringPreference("padherder_login_" + accountId, null));
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

	public boolean isChooseSyncPreselectMonsters(SyncMode mode) {
		switch (mode) {
			case CREATED:
				return getBooleanPreference("choose_sync_preselect_created_monsters", false);
			case DELETED:
				return getBooleanPreference("choose_sync_preselect_deleted_monsters", false);
			case UPDATED:
				return getBooleanPreference("choose_sync_preselect_updated_monsters", true);
			default:
				return false;
		}
	}

	public boolean isChooseSyncGroupMonsters(SyncMode mode) {
		switch (mode) {
			case CREATED:
				return getBooleanPreference("choose_sync_group_monsters_created", true);
			case DELETED:
				return getBooleanPreference("choose_sync_group_monsters_deleted", false);
			case UPDATED:
				return getBooleanPreference("choose_sync_group_monsters_updated", false);
			default:
				return false;
		}
	}

	public boolean isChooseSyncUseIgnoreListForMonsters(SyncMode mode) {
		switch (mode) {
			case CREATED:
				return getBooleanPreference("choose_sync_use_ignore_monsters_created", false);
			case DELETED:
				return getBooleanPreference("choose_sync_use_ignore_monsters_deleted", false);
			case UPDATED:
				return getBooleanPreference("choose_sync_use_ignore_monsters_updated", true);
			default:
				return false;
		}
	}

	public MonsterPriority getDefaultMonsterCreatePriority() {
		return MonsterPriority.valueOf(getStringPreference("default_monster_create_priority", MonsterPriority.MED.name()));
	}

	public Set<Integer> getMonsterIgnoreList() {
		final Set<Integer> monsterIds = new HashSet<Integer>();

		final String ignoreListString = getStringPreference("monster_ignore_list", null);
		if (StringUtils.isNotBlank(ignoreListString)) {
			for (final String idString : ignoreListString.split(",")) {
				try {
					monsterIds.add(Integer.parseInt(idString));
				} catch (NumberFormatException e) {
					// Should not happen as the list is only set from setMonsterIgnoreList(Set<Integer>)
					MyLog.warn("ignoring invalid number", e);
				}
			}
		}

		return monsterIds;
	}

	public void setMonsterIgnoreList(Set<Integer> monsterIds) {
		setStringPreference("monster_ignore_list", StringUtils.join(monsterIds, ","));
	}
}
