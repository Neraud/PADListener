
package fr.neraud.padlistener.helper;

import android.content.Context;
import android.preference.PreferenceManager;
import fr.neraud.padlistener.constant.SyncMaterialInMonster;

public class DefaultSharedPreferencesHelper extends AbstractSharedPreferencesHelper {

	public DefaultSharedPreferencesHelper(Context context) {
		super(PreferenceManager.getDefaultSharedPreferences(context));
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

	public boolean getSyncDeductMonsterInMaterial() {
		return Boolean.valueOf(getStringPreference("sync_deduct_monster_in_material", "true"));
	}
}
