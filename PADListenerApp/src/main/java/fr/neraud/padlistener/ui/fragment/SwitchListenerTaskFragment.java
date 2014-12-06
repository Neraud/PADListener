package fr.neraud.padlistener.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.CaptureNotificationHelper;
import fr.neraud.padlistener.service.ListenerService;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceBinder;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceListener;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;

/**
 * Task fragment for SwitchListener
 *
 * @author Neraud
 */
public class SwitchListenerTaskFragment extends Fragment {

	private final ServiceConnection mConnection;
	private boolean mIsBound = false;
	private ListenerServiceBinder mListenerServiceBinder;
	private CallBacks mCallbacks = null;
	private ListenerState mListenerState = null;
	private SwitchListenerResult mResult = null;

	public enum ListenerState {
		STARTING,
		STARTED,
		START_FAILED,
		STOPPING,
		STOPPED,
		STOP_FAILED;

		private ListenerState() {
		}
	}

	public static interface CallBacks {

		public void updateState(ListenerState state, SwitchListenerResult result);
	}

	public SwitchListenerTaskFragment() {
		mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				MyLog.entry();

				mListenerServiceBinder = (ListenerServiceBinder) service;

				MyLog.debug("started ? -> " + mListenerServiceBinder.isListenerStarted());

				if (mListenerServiceBinder.isListenerStarted()) {
					updateState(ListenerState.STARTED);
				} else {
					updateState(ListenerState.STOPPED);
				}

				mIsBound = true;

				MyLog.exit();
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				MyLog.entry();
				mListenerServiceBinder = null;
				mIsBound = false;
				MyLog.exit();
			}
		};
	}

	private void doBindService() {
		MyLog.entry();

		final Intent serviceIntent = new Intent(getActivity().getApplicationContext(), ListenerService.class);

		// Start manually to prevent the service being stopped when the app is closed and unbinds
		getActivity().getApplicationContext().startService(serviceIntent);
		getActivity().getApplicationContext().bindService(serviceIntent, mConnection, 0/*Context.BIND_AUTO_CREATE*/);

		MyLog.exit();
	}

	private void doUnbindService() {
		MyLog.entry();

		if (mIsBound) {
			// Detach our existing connection.
			getActivity().getApplicationContext().unbindService(mConnection);
		}

		MyLog.exit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		MyLog.exit();
	}

	@Override
	public void onStart() {
		MyLog.entry();
		super.onStart();
		doBindService();
		MyLog.exit();
	}

	@Override
	public void onStop() {
		MyLog.entry();
		super.onStop();
		doUnbindService();
		MyLog.exit();
	}

	@Override
	public void onDetach() {
		MyLog.entry();
		super.onDetach();
		mCallbacks = null;
		MyLog.exit();
	}

	public void registerCallbacks(CallBacks callbacks) {
		this.mCallbacks = callbacks;
		notifyCallBacks();
	}

	private void notifyCallBacks() {
		if (mCallbacks != null) {
			mCallbacks.updateState(mListenerState, mResult);
		}
	}

	public void startListener() {
		MyLog.entry();

		if (canStart()) {
			updateState(ListenerState.STARTING);

			final ListenerServiceListener startListener = new ListenerServiceListener() {

				@Override
				public void notifyActionSuccess(SwitchListenerResult newResult) {
					MyLog.entry();
					mResult = newResult;
					updateState(ListenerState.STARTED);
					MyLog.exit();
				}

				@Override
				public void notifyActionFailed(SwitchListenerResult newResult) {
					MyLog.entry();
					mResult = newResult;
					updateState(ListenerState.START_FAILED);
					MyLog.exit();
				}

			};
			mListenerServiceBinder.startListener(startListener, new CaptureNotificationHelper(getActivity()));
		} else {
			notifyCallBacks();
		}
		MyLog.exit();
	}

	public void stopListener(boolean forced) {
		MyLog.entry();

		if (canStop()) {
			updateState(ListenerState.STOPPING);

			final ListenerServiceListener stopListener = new ListenerServiceListener() {

				@Override
				public void notifyActionSuccess(SwitchListenerResult newResult) {
					MyLog.entry();
					mResult = newResult;
					updateState(ListenerState.STOPPED);
					MyLog.exit();
				}

				@Override
				public void notifyActionFailed(SwitchListenerResult newResult) {
					MyLog.entry();
					mResult = newResult;
					updateState(ListenerState.STOP_FAILED);
					MyLog.exit();
				}

			};

			mListenerServiceBinder.stopListener(stopListener);
		} else if (forced) {
			mListenerServiceBinder.stopListener(null);
		} else {
			notifyCallBacks();
		}
		MyLog.exit();
	}

	private boolean canStart() {
		if (mListenerState != null) {
			switch (mListenerState) {
				case STARTING:
					return false;
				case STARTED:
					return false;
				case START_FAILED:
					return true;
				case STOPPING:
					return false;
				case STOPPED:
					return true;
				case STOP_FAILED:
					return false;
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	private boolean canStop() {
		if (mListenerState != null) {
			switch (mListenerState) {
				case STARTING:
					return false;
				case STARTED:
					return true;
				case START_FAILED:
					return false;
				case STOPPING:
					return false;
				case STOPPED:
					return false;
				case STOP_FAILED:
					return true;
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	private void updateState(ListenerState status) {
		this.mListenerState = status;
		notifyCallBacks();
	}

}
