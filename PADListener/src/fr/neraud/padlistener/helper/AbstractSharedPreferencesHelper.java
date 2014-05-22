
package fr.neraud.padlistener.helper;

import android.content.SharedPreferences;

public abstract class AbstractSharedPreferencesHelper {

	private final SharedPreferences sharedPreferences;

	protected AbstractSharedPreferencesHelper(SharedPreferences sharedPreference) {
		this.sharedPreferences = sharedPreference;
	}

	protected void setBooleanPreference(String name, boolean bool) {
		sharedPreferences.edit().putBoolean(name, bool).commit();
	}

	protected boolean getBooleanPreference(String name, boolean defaultBool) {
		return sharedPreferences.getBoolean(name, defaultBool);
	}
}
