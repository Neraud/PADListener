package fr.neraud.padlistener.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.List;

import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.helper.CaptureNotificationHelper;
import fr.neraud.padlistener.service.receiver.AbstractAutoCaptureReceiver;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;

/**
 * AutoCaptureService that triggers the ListenerService and (re)starts PAD
 * Created by Neraud on 16/11/2014.
 */
public class AutoCaptureService extends IntentService {

	private static final String CAPTURE_RECEIVER_EXTRA_NAME = "captureReceiver";
	private static final String PAD_VERSION_EXTRA_NAME = "padVersion";
	private ResultReceiver mAutoCaptureReceiver;

	public AutoCaptureService() {
		super("AutoCaptureService");
	}

	public static void addCaptureListenerInIntent(Intent intent, ResultReceiver receiver) {
		intent.putExtra(CAPTURE_RECEIVER_EXTRA_NAME, receiver);
	}

	public static void addPadVersionInIntent(Intent intent, PADVersion version) {
		intent.putExtra(PAD_VERSION_EXTRA_NAME, version);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(getClass().getName(), "onHandleIntent");

		this.mAutoCaptureReceiver = intent.getParcelableExtra(CAPTURE_RECEIVER_EXTRA_NAME);
		final PADVersion version = (PADVersion) intent.getSerializableExtra(PAD_VERSION_EXTRA_NAME);
		notifyProgress(AbstractAutoCaptureReceiver.State.INITIALIZED);

		startListener(version);
	}

	private void startListener(final PADVersion version) {
		Log.d(getClass().getName(), "startListener : " + version);

		final ListenerService.ListenerServiceListener listener = new ListenerService.ListenerServiceListener() {

			@Override
			public void notifyActionSuccess(SwitchListenerResult result) {
				Log.d(getClass().getName(), "notifyActionSuccess");
				notifyProgress(AbstractAutoCaptureReceiver.State.LISTENER_STARTED);
				killPadIfNecessary(version);
				startPad(version);
			}

			@Override
			public void notifyActionFailed(SwitchListenerResult result) {
				Log.d(getClass().getName(), "notifyActionFailed");
				notifyListenerError(result.getError());
			}
		};

		final ServiceConnection connection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				Log.d(getClass().getName(), "onServiceConnected");
				final ListenerService.ListenerServiceBinder listenerServiceBinder = (ListenerService.ListenerServiceBinder) service;

				Log.d(getClass().getName(), "onServiceConnected : started ? -> " + listenerServiceBinder.isListenerStarted());

				if (listenerServiceBinder.isListenerStarted()) {
					notifyProgress(AbstractAutoCaptureReceiver.State.LISTENER_STARTED);
					killPadIfNecessary(version);
					startPad(version);
				} else {
					final ListenerService.CaptureListener callbacks = new CaptureNotificationHelper(AutoCaptureService.this) {
						protected boolean needsToShutDownListener() {
							return true;
						}

						@Override
						public void notifyCaptureStarted() {
							super.notifyCaptureStarted();

							notifyProgress(AbstractAutoCaptureReceiver.State.CAPTURE_STARTING);
						}

						@Override
						public void notifyCaptureFinished(String accountName) {
							super.notifyCaptureFinished(accountName);
							notifyProgress(AbstractAutoCaptureReceiver.State.CAPTURE_FINISHED);
						}
					};

					listenerServiceBinder.startListener(listener, callbacks);
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

	private void killPadIfNecessary(PADVersion version) {
		Log.d(getClass().getName(), "killPadIfNecessary");
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
		if (processes != null) {
			for (ActivityManager.RunningAppProcessInfo process : processes) {
				if (version.getApplicationPackage().equals(process.processName)) {
					Log.d(getClass().getName(), "killPadIfNecessary : PAD found, killing");
					notifyProgress(AbstractAutoCaptureReceiver.State.STOPPING_PAD);
					activityManager.killBackgroundProcesses(process.processName);
					//android.os.Process.sendSignal(process.pid, android.os.Process.SIGNAL_KILL);
					break;
				}
			}
		}
	}

	private void startPad(PADVersion version) {
		Log.d(getClass().getName(), "startPad");
		final PackageManager packageManager = getPackageManager();
		final Intent padStartIntent = packageManager.getLaunchIntentForPackage(version.getApplicationPackage());
		notifyProgress(AbstractAutoCaptureReceiver.State.STARTING_PAD);
		startActivity(padStartIntent);
	}

	private void notifyProgress(AbstractAutoCaptureReceiver.State state) {
		if (mAutoCaptureReceiver != null) {
			final Bundle data = new Bundle();
			data.putSerializable(AbstractAutoCaptureReceiver.STATE_NAME, state);
			mAutoCaptureReceiver.send(AbstractAutoCaptureReceiver.CODE_OK, data);
		}
	}

	private void notifyListenerError(Exception error) {
		if (mAutoCaptureReceiver != null) {
			final Bundle data = new Bundle();
			data.putSerializable(AbstractAutoCaptureReceiver.EXCEPTION_NAME, error);
			mAutoCaptureReceiver.send(AbstractAutoCaptureReceiver.CODE_KO, data);
		}
	}
}
