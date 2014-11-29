package fr.neraud.padlistener.proxy.helper;

import java.lang.reflect.Field;

/**
 * Helper to access hidden WiFi settings.
 *
 * @author Neraud
 * @see "http://stackoverflow.com/questions/12486441/how-can-i-set-proxysettings-and-proxyproperties-on-android-wi-fi-connection-usin"
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public abstract class AbstractWifiConfigHelper implements WifiConfigHelper {

	protected static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getField(name);
		return f.get(obj);
	}

	protected static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		return f.get(obj);
	}

	protected static void setEnumField(Object obj, String value, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}

}
