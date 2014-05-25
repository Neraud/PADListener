
package fr.neraud.padlistener.gui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Switch;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.service.ListenerService;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceBinder;

public class SwitchListenerFragment extends Fragment {

	private boolean mIsBound = false;
	private ListenerServiceBinder listenerServiceBinder;
	private Switch listenerSwitch;

	private final ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d(getClass().getName(), "onServiceConnected");
			listenerServiceBinder = (ListenerServiceBinder) service;

			Log.d(getClass().getName(), "onServiceConnected : started ? -> " + listenerServiceBinder.isListenerStarded());

			listenerSwitch.setChecked(listenerServiceBinder.isListenerStarded());
			listenerSwitch.setText(listenerServiceBinder.isListenerStarded() ? R.string.toggleLabel_on : R.string.toggleLabel_off);
			listenerSwitch.setEnabled(true);
			mIsBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			Log.d(getClass().getName(), "onServiceDisconnected");
			listenerServiceBinder = null;
			mIsBound = false;
		}
	};

	private void doBindService() {
		Log.d(getClass().getName(), "doBindService");

		final Intent serviceIntent = new Intent(getActivity(), ListenerService.class);

		// Start manually to prevent the service being stopped when the app is closed and unbinds
		getActivity().startService(serviceIntent);
		getActivity().bindService(serviceIntent, mConnection, 0/*Context.BIND_AUTO_CREATE*/);
	}

	private void doUnbindService() {
		Log.d(getClass().getName(), "doUnbindService");
		if (mIsBound) {
			// Detach our existing connection.
			getActivity().unbindService(mConnection);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");
		final View view = inflater.inflate(R.layout.switch_listener_fragment, container, false);

		doBindService();

		listenerSwitch = (Switch) view.findViewById(R.id.listenerSwitch);
		listenerSwitch.setText(R.string.toggleLabel);
		listenerSwitch.setEnabled(false);

		listenerSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final boolean value = ((Switch) v).isChecked();
				Log.d(getClass().getName(), "listenerSwitch.onClick : " + value);
				if (value) {
					listenerServiceBinder.startListener();
				} else {
					listenerServiceBinder.stopListener();
				}

			}
		});

		return view;
	}

	@Override
	public void onDestroyView() {
		Log.d(getClass().getName(), "onDestroyView");
		super.onDestroyView();
		doUnbindService();
	}

}
