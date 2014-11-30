package fr.neraud.padlistener.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.MyNotification;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.WifiHelper;
import fr.neraud.padlistener.model.CapturedFriendModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.proxy.helper.ProxyHelper;
import fr.neraud.padlistener.service.task.StartListenerAsyncTask;
import fr.neraud.padlistener.service.task.StopListenerAsyncTask;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;
import fr.neraud.padlistener.ui.activity.HomeActivity;
import fr.neraud.padlistener.util.ExecutableAssetHelper;
import fr.neraud.padlistener.util.ScriptAssetHelper;

/**
 * ListenerService that captures PAD calls to GunHo servers
 *
 * @author Neraud
 */
public class ListenerService extends Service {

	private final IBinder mBinder = new ListenerServiceBinder();
	private boolean started = false;
	private ProxyHelper proxyHelper = null;

	public interface CaptureListener {

		public void notifyCaptureStarted();

		public void notifySavingMonsters(int num, int total, MonsterModel monster);

		public void notifySavingFriends(int num, int total, CapturedFriendModel friend);

		public void notifyCaptureFinished(String playerName);
	}

	public interface ListenerServiceListener {

		public void notifyActionSuccess(SwitchListenerResult result);

		public void notifyActionFailed(SwitchListenerResult result);
	}

	public class ListenerServiceBinder extends Binder {

		public boolean isListenerStarted() {
			return started;
		}

		public void startListener(ListenerServiceListener listener, CaptureListener captureListener) {
			ListenerService.this.doStartListener(listener, captureListener);
		}

		public void stopListener(ListenerServiceListener listener) {
			ListenerService.this.doStopListener(listener);
		}
	}

	public ListenerService() {
		super();
	}

	@Override
	public void onCreate() {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate();

		if (started) {
			Log.d(getClass().getName(), "onCreate : already started");
		} else {
			Log.d(getClass().getName(), "onCreate : starting !");
			try {
				final ScriptAssetHelper scriptAssetHelper = new ScriptAssetHelper(getApplicationContext());
				scriptAssetHelper.copyScriptsFromAssets();

				final ExecutableAssetHelper executableAssetHelper = new ExecutableAssetHelper(getApplicationContext());
				executableAssetHelper.copyExecutablesFromAssets();
			} catch (final IOException e) {
				Log.e(getClass().getName(), "Asset install failed  : " + e.getMessage(), e);
			}
			proxyHelper = new ProxyHelper(getApplicationContext());
		}
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

	private void doStartListener(final ListenerServiceListener listener, CaptureListener captureListener) {
		Log.d(getClass().getName(), "doStartListener");

		final StartListenerAsyncTask asyncTask = new StartListenerAsyncTask(getApplicationContext(), proxyHelper, captureListener) {

			@Override
			protected void onPostExecute(SwitchListenerResult switchListenerResult) {
				Log.d(getClass().getName(), "onPostExecute");

				if (switchListenerResult.isSuccess()) {
					started = true;

					final Notification notification = buildNotification(getProxyMode());
					startForeground(MyNotification.LISTENER_SERVICE.getId(), notification);
					if (listener != null) {
						listener.notifyActionSuccess(switchListenerResult);
					}
				} else {
					if (listener != null) {
						listener.notifyActionFailed(switchListenerResult);
					}
				}
			}
		};
		asyncTask.execute();
	}

	private Notification buildNotification(ProxyMode proxyMode) {
		Log.d(getClass().getName(), "buildNotification");

		final String modeLabel = getString(proxyMode.getLabelResId());
		final String notifTitle = getString(R.string.notification_listener_title, modeLabel);

		String proxyUrl = "localhost:8008";
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getApplicationContext());
		if (prefHelper.isListenerNonLocalEnabled()) {
			final WifiHelper wifiHelper = new WifiHelper(getApplicationContext());
			proxyUrl = wifiHelper.getWifiIpAddress() + ":8008";
		}

		final String notifContent = getString(R.string.notification_listener_content, proxyUrl);

		final Intent notificationIntent = new Intent(this, HomeActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentTitle(notifTitle);
		builder.setOngoing(true);
		builder.setContentText(notifContent);
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setContentIntent(pendingIntent);

		return builder.build();
	}

	private void doStopListener(final ListenerServiceListener listener) {
		Log.d(getClass().getName(), "doStopListener");

		final StopListenerAsyncTask asyncTask = new StopListenerAsyncTask(getApplicationContext(), proxyHelper) {
			@Override
			protected void onPostExecute(SwitchListenerResult switchListenerResult) {
				Log.d(getClass().getName(), "onPostExecute");

				if (switchListenerResult.isSuccess()) {
					started = false;
					stopForeground(true);
					if (listener != null) {
						listener.notifyActionSuccess(switchListenerResult);
					}
				} else {
					if (listener != null) {
						listener.notifyActionFailed(switchListenerResult);
					}
				}
			}
		};
		asyncTask.execute();
	}
}
