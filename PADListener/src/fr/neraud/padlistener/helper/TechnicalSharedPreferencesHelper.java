
package fr.neraud.padlistener.helper;

import java.util.Date;

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

	public void setLastCaptureDate(Date date) {
		setLongPreference("lastCaptureDate", date.getTime());
	}

	public Date getLastCaptureDate() {
		return new Date(getLongPreference("lastCaptureDate", 0L));

	}
}
