package fr.neraud.padlistener.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;

/**
 * AutoCaptureService that triggers the ListenerService and (re)starts PAD
 * Created by Neraud on 16/11/2014.
 */
public class AutoCaptureService extends IntentService {

	public AutoCaptureService() {
		super("AutoCaptureService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");

		final DefaultSharedPreferencesHelper helper = new DefaultSharedPreferencesHelper(getApplicationContext());
		final PADVersion server = helper.getAutoCaptureServer();
		startListener(server);
	}

	private void startListener(final PADVersion server) {
		Log.d(getClass().getName(), "startListener");

		final ListenerService.ListenerServiceListener listener = new ListenerService.ListenerServiceListener() {

			@Override
			public void notifyActionSuccess(SwitchListenerResult result) {
				Log.d(getClass().getName(), "notifyActionSuccess");
				killPadIfNecessary(server);
				startPad(server);
			}

			@Override
			public void notifyActionFailed(SwitchListenerResult result) {
				Log.d(getClass().getName(), "notifyActionFailed");
				Toast.makeText(getApplicationContext(), R.string.auto_capture_start_listener_failed, Toast.LENGTH_LONG).show();
			}
		};

		final ServiceConnection connection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				Log.d(getClass().getName(), "onServiceConnected");
				final ListenerService.ListenerServiceBinder listenerServiceBinder = (ListenerService.ListenerServiceBinder) service;

				Log.d(getClass().getName(), "onServiceConnected : started ? -> " + listenerServiceBinder.isListenerStarted());

				if (listenerServiceBinder.isListenerStarted()) {
					killPadIfNecessary(server);
					startPad(server);
				} else {
					listenerServiceBinder.startListener(listener, true);
				}
				unbindService(this);
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				Log.d(getClass().getName(), "onServiceDisconnected");
			}
		};


		final Intent serviceIntent = new Intent(this, ListenerService.class);

		// Start manually to prevent the service being stopped when the app is closed and unbinds
		startService(serviceIntent);
		bindService(serviceIntent, connection, 0/*Context.BIND_AUTO_CREATE*/);
	}

	private void killPadIfNecessary(PADVersion server) {
		Log.d(getClass().getName(), "killPadIfNecessary");
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
		if (processes != null) {
			for (ActivityManager.RunningAppProcessInfo process : processes) {
				if (server.getApplicationPackage().equals(process.processName)) {
					Log.d(getClass().getName(), "killPadIfNecessary : PAD found, killing");
					activityManager.killBackgroundProcesses("jp.gungho.padEN");
					//android.os.Process.sendSignal(process.pid, android.os.Process.SIGNAL_KILL);
					break;
				}
			}
		}
	}

	private void startPad(PADVersion server) {
		Log.d(getClass().getName(), "startListener");
		final PackageManager packageManager = getPackageManager();
		final Intent padStartIntent = packageManager.getLaunchIntentForPackage(server.getApplicationPackage());
		startActivity(padStartIntent);
	}
}
