package fr.neraud.log;

import org.apache.commons.lang3.StringUtils;

import fr.neraud.padlistener.BuildConfig;
import timber.log.Timber;

/**
 * Created by Neraud on 04/12/2014.
 */
public class MyLog {

	private static char ENTRY_SYMBOL = '>';
	private static char EXIT_SYMBOL = '<';

	public static void init() {
		if (BuildConfig.DEBUG) {
			Timber.plant(new MyDebugTree());
		} else {
			Timber.plant(new CrashReportingTree());
		}
	}

	private static String generateEntryExitMessage(char symbol, String message) {
		return symbol + (StringUtils.isNotBlank(message) ? " " + message : "");
	}

	public static void entry() {
		Timber.d(generateEntryExitMessage(ENTRY_SYMBOL, null));
	}

	public static void entry(String message) {
		Timber.d(generateEntryExitMessage(ENTRY_SYMBOL, message));
	}

	public static void exit() {
		Timber.d(generateEntryExitMessage(EXIT_SYMBOL, null));
	}

	public static void exit(String message) {
		Timber.d(generateEntryExitMessage(EXIT_SYMBOL, message));
	}

	public static void debug(String message) {
		Timber.v(message);
	}

	public static void debug(String message, Throwable t) {
		Timber.v(t, message);
	}

	public static void info(String message) {
		Timber.i(message);
	}

	public static void info(String message, Throwable t) {
		Timber.i(t, message);
	}

	public static void warn(String message) {
		Timber.i(message);
	}

	public static void warn(String message, Throwable t) {
		Timber.i(t, message);
	}

	public static void error(String message) {
		Timber.i(message);
	}

	public static void error(String message, Throwable t) {
		Timber.i(t, message);
	}

}
