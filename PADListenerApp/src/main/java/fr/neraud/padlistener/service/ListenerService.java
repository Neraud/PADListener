package fr.neraud.padlistener.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.sandrop.webscarab.model.Preferences;
import org.sandroproxy.utils.PreferenceUtils;

import java.io.IOException;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.gui.MainActivity;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.helper.WifiHelper;
import fr.neraud.padlistener.proxy.helper.IptablesHelper;
import fr.neraud.padlistener.proxy.helper.ProxyHelper;
import fr.neraud.padlistener.proxy.helper.WifiAutoProxyHelper;
import fr.neraud.padlistener.util.ScriptAssetHelper;

/**
 * ListenerService that captures PAD calls to GunHo servers
 *
 * @author Neraud
 */
public class ListenerService extends Service {

	private static final int NOTIFICATION_ID = 1;
	public static String EXTRA_TOSTART_NAME = "activate";
	private final IBinder mBinder = new ListenerServiceBinder();
	private boolean started = false;
	private ProxyHelper proxyHelper = null;

	public interface ListenerServiceListener {

		public void notifyActionSuccess();

		public void notifyActionFailed(Exception e);
	}

	public class ListenerServiceBinder extends Binder {

		public boolean isListenerStarted() {
			return started;
		}

		public void startListener(ListenerServiceListener listener) {
			doStartListener(listener);
		}

		public void stopListener(ListenerServiceListener listener) {
			doStopListener(listener);
		}
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
		}
	}

	private void initValues(DefaultSharedPreferencesHelper prefHelper) {
		Log.d(getClass().getName(), "initValues");
		final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

		// listen to all adapters only if non local mode is enabled
		final boolean proxyListenNonLocal = prefHelper.isListenerNonLocalEnabled();

		// Transparent proxy only for iptables mode
		final boolean isModeIptables = prefHelper.getProxyMode() == ProxyMode.AUTO_IPTABLES;

		// checking for directory to write data...
		editor.putString(PreferenceUtils.dataStorageKey, getExternalCacheDir().getAbsolutePath());

		// should we listen on all adapters ?
		editor.putBoolean(PreferenceUtils.proxyListenNonLocal, proxyListenNonLocal);

		// should we listen also for transparent flow ?
		editor.putBoolean(PreferenceUtils.proxyTransparentKey, isModeIptables);

		editor.putBoolean(PreferenceUtils.proxyFakeCerts, isModeIptables);

		// Capture data is necessary for SSL capture to work
		editor.putBoolean(PreferenceUtils.proxyCaptureData, true);

		editor.commit();
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
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getApplicationContext());
		final ProxyMode proxyMode = prefHelper.getProxyMode();

		initValues(prefHelper);
		Preferences.init(getApplicationContext());

		try {
			final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(getApplicationContext());
			techPrefHelper.setLastListenerStartProxyMode(proxyMode);

			proxyHelper.activateProxy();

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

			started = true;

			final Notification notification = buildNotification(proxyMode);
			startForeground(NOTIFICATION_ID, notification);
			if (listener != null) {
				listener.notifyActionSuccess();
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

		final String modeLabel = getString(proxyMode.getLabelResId());
		final String notifTitle = getString(R.string.notification_listener_title, modeLabel);

		String proxyUrl ="localhost:8008";
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getApplicationContext());
		if(prefHelper.isListenerNonLocalEnabled()) {
			final WifiHelper wifiHelper = new WifiHelper(getApplicationContext());
			proxyUrl = wifiHelper.getWifiIpAddress() + ":8008";
		}

		final String notifContent = getString(R.string.notification_listener_content, proxyUrl);

		final Intent notificationIntent = new Intent(this, MainActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		final Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(notifTitle);
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
					iptablesHelper.deactivateIptables();
					break;
				case AUTO_WIFI_PROXY:
					final WifiAutoProxyHelper wifiAutoProxyHelper = new WifiAutoProxyHelper(getApplicationContext());
					wifiAutoProxyHelper.deactivateAutoProxy();
					break;
				case MANUAL:
				default:
					// Nothing to do
					break;
			}

			started = false;
			stopForeground(true);
			if (listener != null) {
				listener.notifyActionSuccess();
			}
		} catch (final Exception e) {
			Log.e(getClass().getName(), "PADListener stop failed  : " + e.getMessage());
			if (listener != null) {
				listener.notifyActionFailed(e);
			}
		}
	}
}
