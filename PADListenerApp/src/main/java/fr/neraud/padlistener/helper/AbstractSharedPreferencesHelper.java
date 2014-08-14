package fr.neraud.padlistener.helper;

import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Base helper to access SharedPreferences
 *
 * @author Neraud
 */
public abstract class AbstractSharedPreferencesHelper {

	private final SharedPreferences sharedPreferences;

	protected AbstractSharedPreferencesHelper(SharedPreferences sharedPreference) {
		this.sharedPreferences = sharedPreference;
	}

	protected boolean getBooleanPreference(String name, boolean defaultValue) {
		return sharedPreferences.getBoolean(name, defaultValue);
	}

	protected void setBooleanPreference(String name, boolean bool) {
		sharedPreferences.edit().putBoolean(name, bool).commit();
	}

	protected String getStringPreference(String name, String defaultValue) {
		return sharedPreferences.getString(name, defaultValue);
	}

	protected void setStringPreference(String name, String value) {
		sharedPreferences.edit().putString(name, value).commit();
	}

	protected Long getLongPreference(String name, Long defaultValue) {
		return sharedPreferences.getLong(name, defaultValue);
	}

	protected void setLongPreference(String name, Long value) {
		sharedPreferences.edit().putLong(name, value).commit();
	}

	protected int getIntFromStringPreference(String name, int defaultValue) {
		final String value = sharedPreferences.getString(name, "");
		if (StringUtils.isNotBlank(value))
			return Integer.parseInt(sharedPreferences.getString(name, ""));
		else
			return defaultValue;
	}

	protected Set<String> getStringSetPreference(String name, Set<String> defaultValue) {
		return sharedPreferences.getStringSet(name, defaultValue);
	}
}
