
package fr.neraud.padlistener.helper;

import android.content.Context;

public class TechnicalSharedPreferencesHelper extends AbstractSharedPreferencesHelper {

	public TechnicalSharedPreferencesHelper(Context context) {
		super(context.getSharedPreferences("technicalPrefs", 0));
	}

	public boolean isHasBeenInstalled() {
		return getBooleanPreference("hasBeenInstalled", false);
	}

	public void setHasBeenInstalled(boolean bool) {
		setBooleanPreference("hasBeenInstalled", bool);
	}
}
