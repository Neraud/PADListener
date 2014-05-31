
package fr.neraud.padlistener.service;

import java.io.IOException;

import org.sandrop.webscarab.model.Preferences;
import org.sandroproxy.utils.PreferenceUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.exception.RootCommandExecutionException;
import fr.neraud.padlistener.gui.MainActivity;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.proxy.helper.IptablesHelper;
import fr.neraud.padlistener.proxy.helper.ProxyHelper;
import fr.neraud.padlistener.proxy.helper.WifiAutoProxyHelper;
import fr.neraud.padlistener.util.ScriptAssetHelper;

public class ListenerService extends Service {

	public static String EXTRA_TOSTART_NAME = "activate";
	private static final int NOTIFICATION_ID = 1;

	private boolean started = false;
	private ProxyHelper proxyHelper = null;

	private final IBinder mBinder = new ListenerServiceBinder();

	public class ListenerServiceBinder extends Binder {

		public boolean isListenerStarded() {
			return started;
		}

		public void startListener(ListenerServiceListener listener) {
			doStartListener(listener);
		}

		public void stopListener(ListenerServiceListener listener) {
			doStopListener(listener);
		}
	}

	public interface ListenerServiceListener {

		public void notifyActionSucess();

		public void notifyActionFailed(Exception e);
	}

	public ListenerService() {
		super();
	}

	@Override
	public void onCreate() {
		Log.d(getClass().getName(), "onCreate : " + this);
		super.onCreate();

		if (started) {
			Log.d(getClass().getName(), "onCreate : already started");
		} else {
			Log.d(getClass().getName(), "onCreate : starting !");
			try {
				final ScriptAssetHelper scriptAssetUtils = new ScriptAssetHelper(getApplicationContext());
				scriptAssetUtils.copyScriptsFromAssets();
			} catch (final IOException e) {
				Log.e(getClass().getName(), "PADListener stop failed  : " + e.getMessage(), e);
			}

			proxyHelper = new ProxyHelper(getApplicationContext());
			initValues();
		}
	}

	private void initValues() {
		Log.d(getClass().getName(), "initValues");
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		// checking for directory to write data...
		pref.edit().putString(PreferenceUtils.dataStorageKey, getExternalCacheDir().getAbsolutePath()).commit();

		// by default we listen on all adapters
		pref.edit().putBoolean(PreferenceUtils.proxyListenNonLocal, true).commit();

		// we listen also for transparent flow 
		pref.edit().putBoolean(PreferenceUtils.proxyTransparentKey, true).commit();

		// don't capture data to database
		pref.edit().putBoolean(PreferenceUtils.proxyCaptureData, false).commit();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(getClass().getName(), "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.d(getClass().getName(), "onTaskRemoved");
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(getClass().getName(), "onStartCommand : " + this + " : " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(getClass().getName(), "onDestroy");
		super.onDestroy();
		if (started) {
			doStopListener(null);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void doStartListener(ListenerServiceListener listener) {
		Log.d(getClass().getName(), "doStartListener");
		Preferences.init(getApplicationContext());

		try {
			final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getApplicationContext());
			final ProxyMode proxyMode = prefHelper.getProxyMode();
			switch (proxyMode) {
			case AUTO_IPTABLES:
				final IptablesHelper iptablesHelper = new IptablesHelper(getApplicationContext());
				iptablesHelper.activateIptables();
				break;
			case AUTO_WIFI_PROXY:
				final WifiAutoProxyHelper wifiAutoProxyHelper = new WifiAutoProxyHelper(getApplicationContext());
				wifiAutoProxyHelper.activateAutoProxy();
				break;
			case MANUAL:
			default:
				// Nothing to do
				break;
			}

			final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(getApplicationContext());
			techPrefHelper.setLastListenerStartProxyMode(proxyMode);

			proxyHelper.activateProxy();

			started = true;

			final Notification notification = buildNotification(proxyMode);
			startForeground(NOTIFICATION_ID, notification);
			if (listener != null) {
				listener.notifyActionSucess();
			}
		} catch (final Exception e) {
			Log.e(getClass().getName(), "PADListener stop failed  : " + e.getMessage(), e);
			if (listener != null) {
				listener.notifyActionFailed(e);
			}
		}
	}

	private Notification buildNotification(ProxyMode proxyMode) {
		Log.d(getClass().getName(), "buildNotification");

		String notifContent;
		switch (proxyMode) {
		case AUTO_IPTABLES:
			notifContent = getString(R.string.notification_listener_content_iptables);
			break;
		case AUTO_WIFI_PROXY:
			notifContent = getString(R.string.notification_listener_content_proxy_wifi);
			break;
		case MANUAL:
		default:
			notifContent = getString(R.string.notification_listener_content_manual);
			break;
		}

		final Intent notificationIntent = new Intent(this, MainActivity.class);

		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
		        PendingIntent.FLAG_CANCEL_CURRENT);

		final Notification.Builder builder = new Notification.Builder(this);

		builder.setContentTitle(getString(R.string.notification_listener_title));
		builder.setOngoing(true);
		builder.setContentText(notifContent);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentIntent(pendingIntent);

		return builder.getNotification();
	}

	private void doStopListener(ListenerServiceListener listener) {
		Log.d(getClass().getName(), "doStopListener");

		try {
			proxyHelper.deactivateProxy();

			final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(getApplicationContext());
			final ProxyMode proxyMode = techPrefHelper.getLastListenerStartProxyMode();
			switch (proxyMode) {
			case AUTO_IPTABLES:
				final IptablesHelper iptablesHelper = new IptablesHelper(getApplicationContext());
				iptablesHelper.activateIptables();
				break;
			case AUTO_WIFI_PROXY:
				final WifiAutoProxyHelper wifiAutoProxyHelper = new WifiAutoProxyHelper(getApplicationContext());
				wifiAutoProxyHelper.activateAutoProxy();
				break;
			case MANUAL:
			default:
				// Nothing to do
				break;
			}

			started = false;
			stopForeground(true);
			if (listener != null) {
				listener.notifyActionSucess();
			}
		} catch (final RootCommandExecutionException e) {
			Log.e(getClass().getName(), "PADListener stop failed  : " + e.getMessage());
			if (listener != null) {
				listener.notifyActionFailed(e);
			}
			;
		}
	}
}
