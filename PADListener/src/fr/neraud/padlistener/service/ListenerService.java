
package fr.neraud.padlistener.service;

import org.sandrop.webscarab.model.Preferences;
import org.sandroproxy.utils.PreferenceUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.exception.RootCommandExecutionException;
import fr.neraud.padlistener.gui.MainActivity;
import fr.neraud.padlistener.proxy.helper.IptablesHelper;
import fr.neraud.padlistener.proxy.helper.ProxyHelper;
import fr.neraud.padlistener.util.ScriptAssetUtils;

public class ListenerService extends Service {

	public static String EXTRA_TOSTART_NAME = "activate";
	private static String TAG = ListenerService.class.getName();
	private static final int NOTIFICATION_ID = 1;

	private boolean started = false;
	private IptablesHelper iptablesHelper = null;
	private ProxyHelper proxyHelper = null;

	private final IBinder mBinder = new ListenerServiceBinder();

	public ListenerService() {
		super();
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate : " + this);
		super.onCreate();

		if (started) {
			Log.d(TAG, "onCreate : already started");
		} else {
			Log.d(TAG, "onCreate : starting !");
			final ScriptAssetUtils scriptAssetUtils = new ScriptAssetUtils(getApplicationContext());
			scriptAssetUtils.copyScriptsFromAssets();

			iptablesHelper = new IptablesHelper(getApplicationContext());
			proxyHelper = new ProxyHelper(getApplicationContext());
			initValues();
		}
	}

	private void initValues() {
		Log.d(TAG, "initValues");
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
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.d(TAG, "onTaskRemoved");
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand : " + this + " : " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		if (started) {
			doStopListener();
		}
	}

	public class ListenerServiceBinder extends Binder {

		public boolean isListenerStarded() {
			return started;
		}

		public void startListener() {
			doStartListener();
		}

		public void stopListener() {
			doStopListener();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void doStartListener() {
		Log.d(TAG, "doStartListener");
		Preferences.init(getApplicationContext());

		try {
			iptablesHelper.activateIptables();
			proxyHelper.activateProxy();

			started = true;

			final Notification notification = buildNotification();
			startForeground(NOTIFICATION_ID, notification);
			showToast("PADListener started");
		} catch (final Exception e) {
			Log.e(TAG, "PADListener stop failed  : " + e.getMessage());
			showToast("PADListener stop failed !");
		}
	}

	private Notification buildNotification() {
		Log.d(TAG, "buildNotification");

		final Intent notificationIntent = new Intent(this, MainActivity.class);

		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
		        PendingIntent.FLAG_CANCEL_CURRENT);

		final Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle("Test");
		builder.setOngoing(true);
		builder.setContentText("todo content");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentIntent(pendingIntent);

		return builder.getNotification();
	}

	private void doStopListener() {
		Log.d(TAG, "doStopListener");

		try {
			proxyHelper.deactivateProxy();
			iptablesHelper.deactivateIptables();
			started = false;
			stopForeground(true);
			showToast("PADListener stopped");
		} catch (final RootCommandExecutionException e) {
			Log.e(TAG, "PADListener stop failed  : " + e.getMessage());
			showToast("PADListener stop failed !");
		}
	}

	private void showToast(final String toastMessage) {
		new Handler(getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
