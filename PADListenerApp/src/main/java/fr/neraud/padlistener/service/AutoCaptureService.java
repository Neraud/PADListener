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

import java.util.List;

import fr.neraud.log.MyLog;
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
		MyLog.entry();

		this.mAutoCaptureReceiver = intent.getParcelableExtra(CAPTURE_RECEIVER_EXTRA_NAME);
		final PADVersion version = (PADVersion) intent.getSerializableExtra(PAD_VERSION_EXTRA_NAME);
		notifyProgress(AbstractAutoCaptureReceiver.State.INITIALIZED);

		startListener(version);

		MyLog.exit();
	}

	private void startListener(final PADVersion version) {
		MyLog.entry("version = " + version);

		final ListenerService.ListenerServiceListener listener = new ListenerService.ListenerServiceListener() {

			@Override
			public void notifyActionSuccess(SwitchListenerResult result) {
				MyLog.entry();
				notifyProgress(AbstractAutoCaptureReceiver.State.LISTENER_STARTED);
				killPadIfNecessary(version);
				startPad(version);
				MyLog.exit();
			}

			@Override
			public void notifyActionFailed(SwitchListenerResult result) {
				MyLog.entry();
				notifyListenerError(result.getError());
				MyLog.exit();
			}
		};

		final ServiceConnection connection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				MyLog.entry();
				final ListenerService.ListenerServiceBinder listenerServiceBinder = (ListenerService.ListenerServiceBinder) service;

				MyLog.debug("started ? -> " + listenerServiceBinder.isListenerStarted());

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
				MyLog.exit();
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				MyLog.entry();
				MyLog.exit();
			}
		};


		final Intent serviceIntent = new Intent(this, ListenerService.class);

		// Start manually to prevent the service being stopped when the app is closed and unbinds
		startService(serviceIntent);
		bindService(serviceIntent, connection, 0/*Context.BIND_AUTO_CREATE*/);

		MyLog.exit();
	}

	private void killPadIfNecessary(PADVersion version) {
		MyLog.entry();

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		final List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
		if (processes != null) {
			for (ActivityManager.RunningAppProcessInfo process : processes) {
				if (version.getApplicationPackage().equals(process.processName)) {
					MyLog.debug("PAD found, killing");
					notifyProgress(AbstractAutoCaptureReceiver.State.STOPPING_PAD);
					activityManager.killBackgroundProcesses(process.processName);
					//android.os.Process.sendSignal(process.pid, android.os.Process.SIGNAL_KILL);
					break;
				}
			}
		}
		MyLog.exit();
	}

	private void startPad(PADVersion version) {
		MyLog.entry();

		final PackageManager packageManager = getPackageManager();
		final Intent padStartIntent = packageManager.getLaunchIntentForPackage(version.getApplicationPackage());
		notifyProgress(AbstractAutoCaptureReceiver.State.STARTING_PAD);
		startActivity(padStartIntent);

		MyLog.exit();
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
