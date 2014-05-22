
package fr.neraud.padlistener.helper;

import android.content.Context;
import android.preference.PreferenceManager;

public class DefaultSharedPreferencesHelper extends AbstractSharedPreferencesHelper {

	public DefaultSharedPreferencesHelper(Context context) {
		super(PreferenceManager.getDefaultSharedPreferences(context));
	}

}
