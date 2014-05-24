
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

	protected boolean getBooleanPreference(String name, boolean defaultValue) {
		return sharedPreferences.getBoolean(name, defaultValue);
	}

	protected String getStringPreference(String name, String defaultValue) {
		return sharedPreferences.getString(name, defaultValue);
	}
}
