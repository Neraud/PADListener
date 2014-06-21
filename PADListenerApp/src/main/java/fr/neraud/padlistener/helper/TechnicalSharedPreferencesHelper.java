
package fr.neraud.padlistener.helper;

import java.util.Date;

import android.content.Context;
import fr.neraud.padlistener.constant.ProxyMode;

/**
 * Helper to access the technical SharedPreferences
 * 
 * @author Neraud
 */
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

	public void setLastListenerStartProxyMode(ProxyMode proxyMode) {
		setStringPreference("lastListenerStartProxyMode", proxyMode.name());
	}

	public ProxyMode getLastListenerStartProxyMode() {
		return ProxyMode.valueOf(getStringPreference("lastListenerStartProxyMode", "MANUAL"));
	}

	public void setMonsterInfoRefreshDate(Date date) {
		setLongPreference("monsterInfoRefreshDate", date.getTime());
	}

	public Date getMonsterInfoRefreshDate() {
		return new Date(getLongPreference("monsterInfoRefreshDate", 0L));
	}

	public void setMonsterImagesRefreshDate(Date date) {
		setLongPreference("monsterImagesRefreshDate", date.getTime());
	}

	public Date getMonsterImagesRefreshDate() {
		return new Date(getLongPreference("monsterImagesRefreshDate", 0L));
	}
}
