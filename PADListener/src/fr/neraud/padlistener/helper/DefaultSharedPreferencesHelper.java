
package fr.neraud.padlistener.helper;

import android.content.Context;
import android.preference.PreferenceManager;

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

}
