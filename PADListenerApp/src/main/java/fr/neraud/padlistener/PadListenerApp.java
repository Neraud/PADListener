package fr.neraud.padlistener;

import android.app.Application;

import fr.neraud.log.MyLog;

/**
 * PADListener Application
 * Created by Neraud on 04/12/2014.
 */
public class PadListenerApp extends Application {

	@Override public void onCreate() {
		super.onCreate();
		MyLog.init();
	}

}
