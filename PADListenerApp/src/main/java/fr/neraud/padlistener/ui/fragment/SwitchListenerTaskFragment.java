package fr.neraud.padlistener.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

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
				Log.d(getClass().getName(), "onServiceConnected");
				mListenerServiceBinder = (ListenerServiceBinder) service;

				Log.d(getClass().getName(), "onServiceConnected : started ? -> " + mListenerServiceBinder.isListenerStarted());

				if (mListenerServiceBinder.isListenerStarted()) {
					updateState(ListenerState.STARTED);
				} else {
					updateState(ListenerState.STOPPED);
				}

				mIsBound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				Log.d(getClass().getName(), "onServiceDisconnected");
				mListenerServiceBinder = null;
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
		mCallbacks = null;
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
		Log.d(getClass().getName(), "startListener");

		if (canStart()) {
			updateState(ListenerState.STARTING);

			final ListenerServiceListener startListener = new ListenerServiceListener() {

				@Override
				public void notifyActionSuccess(SwitchListenerResult newResult) {
					Log.d(getClass().getName(), "notifyActionSuccess");
					mResult = newResult;
					updateState(ListenerState.STARTED);
				}

				@Override
				public void notifyActionFailed(SwitchListenerResult newResult) {
					Log.d(getClass().getName(), "notifyActionFailed");
					mResult = newResult;
					updateState(ListenerState.START_FAILED);
				}

			};
			mListenerServiceBinder.startListener(startListener, new CaptureNotificationHelper(getActivity()));
		} else {
			notifyCallBacks();
		}
	}

	public void stopListener(boolean forced) {
		Log.d(getClass().getName(), "stopListener");


		if (canStop()) {
			updateState(ListenerState.STOPPING);

			final ListenerServiceListener stopListener = new ListenerServiceListener() {

				@Override
				public void notifyActionSuccess(SwitchListenerResult newResult) {
					Log.d(getClass().getName(), "notifyActionSuccess");
					mResult = newResult;
					updateState(ListenerState.STOPPED);
				}

				@Override
				public void notifyActionFailed(SwitchListenerResult newResult) {
					Log.d(getClass().getName(), "notifyActionFailed");
					mResult = newResult;
					updateState(ListenerState.STOP_FAILED);
				}

			};

			mListenerServiceBinder.stopListener(stopListener);
		} else if (forced) {
			mListenerServiceBinder.stopListener(null);
		} else {
			notifyCallBacks();
		}
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
