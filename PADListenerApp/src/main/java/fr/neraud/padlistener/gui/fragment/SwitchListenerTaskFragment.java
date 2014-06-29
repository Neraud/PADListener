package fr.neraud.padlistener.gui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

import fr.neraud.padlistener.service.ListenerService;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceBinder;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceListener;

/**
 * Task fragment for SwitchListener
 *
 * @author Neraud
 */
public class SwitchListenerTaskFragment extends Fragment {

	private final ServiceConnection mConnection;
	private boolean mIsBound = false;
	private ListenerServiceBinder listenerServiceBinder;
	private CallBacks callbacks = null;
	private ListenerState state = null;
	private Throwable error = null;

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

		public void updateState(ListenerState state, Throwable error);
	}

	public SwitchListenerTaskFragment() {
		mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				Log.d(getClass().getName(), "onServiceConnected");
				listenerServiceBinder = (ListenerServiceBinder) service;

				Log.d(getClass().getName(), "onServiceConnected : started ? -> " + listenerServiceBinder.isListenerStarted());

				if (listenerServiceBinder.isListenerStarted()) {
					updateState(ListenerState.STARTED);
				} else {
					updateState(ListenerState.STOPPED);
				}

				mIsBound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				Log.d(getClass().getName(), "onServiceDisconnected");
				listenerServiceBinder = null;
				mIsBound = false;
			}
		};
	}

	private void doBindService() {
		Log.d(getClass().getName(), "doBindService");

		final Intent serviceIntent = new Intent(getActivity().getApplicationContext(), ListenerService.class);

		// Start manually to prevent the service being stopped when the app is closed and unbinds
		getActivity().getApplicationContext().startService(serviceIntent);
		getActivity().getApplicationContext().bindService(serviceIntent, mConnection, 0/*Context.BIND_AUTO_CREATE*/);
	}

	private void doUnbindService() {
		Log.d(getClass().getName(), "doUnbindService");
		if (mIsBound) {
			// Detach our existing connection.
			getActivity().getApplicationContext().unbindService(mConnection);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public void onStart() {
		Log.d(getClass().getName(), "onStart");
		super.onStart();

		doBindService();
	}

	@Override
	public void onStop() {
		Log.d(getClass().getName(), "onStop");
		super.onStop();

		doUnbindService();
	}

	@Override
	public void onDetach() {
		Log.d(getClass().getName(), "onDetach");
		super.onDetach();
		callbacks = null;
	}

	public void registerCallbacks(CallBacks callbacks) {
		this.callbacks = callbacks;
		notifyCallBacks();
	}

	private void notifyCallBacks() {
		if (callbacks != null) {
			callbacks.updateState(state, error);
		}
	}

	public void startListener() {
		Log.d(getClass().getName(), "startListener");

		if (canStart()) {
			updateState(ListenerState.STARTING);

			final ListenerServiceListener startListener = new ListenerServiceListener() {

				@Override
				public void notifyActionSuccess() {
					Log.d(getClass().getName(), "notifyActionSuccess");
					updateState(ListenerState.STARTED);
				}

				@Override
				public void notifyActionFailed(Exception e) {
					Log.d(getClass().getName(), "notifyActionFailed");
					updateState(ListenerState.START_FAILED, e);
				}

			};
			listenerServiceBinder.startListener(startListener);
		} else {
			notifyCallBacks();
		}
	}

	public void stopListener() {
		Log.d(getClass().getName(), "stopListener");

		if (canStop()) {
			updateState(ListenerState.STOPPING);

			final ListenerServiceListener stopListener = new ListenerServiceListener() {

				@Override
				public void notifyActionSuccess() {
					Log.d(getClass().getName(), "notifyActionSuccess");
					updateState(ListenerState.STOPPED);
				}

				@Override
				public void notifyActionFailed(Exception e) {
					Log.d(getClass().getName(), "notifyActionFailed");
					updateState(ListenerState.STOP_FAILED, e);
				}

			};

			listenerServiceBinder.stopListener(stopListener);
		} else {
			notifyCallBacks();
		}
	}

	private boolean canStart() {
		if (state != null) {
			switch (state) {
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
		if (state != null) {
			switch (state) {
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
		updateState(status, null);
	}

	private void updateState(ListenerState status, Throwable t) {
		this.state = status;
		error = t;
		notifyCallBacks();
	}

}
