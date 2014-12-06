package fr.neraud.log;

/**
 * Created by Neraud on 04/12/2014.
 */
public class CrashReportingTree extends MyDebugTree {

	@Override
	public void v(String message, Object... args) {
	}

	@Override
	public void v(Throwable t, String message, Object... args) {
	}

	@Override
	public void d(String message, Object... args) {
	}

	@Override
	public void d(Throwable t, String message, Object... args) {
	}
}