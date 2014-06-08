
package fr.neraud.padlistener.gui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.service.ListenerService;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceBinder;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceListener;

/**
 * Main fragment for SwitchListener
 * 
 * @author Neraud
 */
public class SwitchListenerFragment extends Fragment {

	private boolean mIsBound = false;
	private ListenerServiceBinder listenerServiceBinder;
	private TextView listenerStatus;
	private Switch listenerSwitch;

	private final ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d(getClass().getName(), "onServiceConnected");
			listenerServiceBinder = (ListenerServiceBinder) service;

			Log.d(getClass().getName(), "onServiceConnected : started ? -> " + listenerServiceBinder.isListenerStarded());

			if (listenerServiceBinder.isListenerStarded()) {
				updateStatusStarted();
			} else {
				updateStatusStopped();
			}

			listenerSwitch.setChecked(listenerServiceBinder.isListenerStarded());
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

		listenerStatus = (TextView) view.findViewById(R.id.switch_listener_status);

		listenerSwitch = (Switch) view.findViewById(R.id.switch_listener_switch);
		listenerSwitch.setEnabled(false);

		final ListenerServiceListener startListener = new ListenerServiceListener() {

			@Override
			public void notifyActionSucess() {
				Log.d(getClass().getName(), "notifyActionSucess");
				updateStatusStarted();
				listenerSwitch.setEnabled(true);
			}

			@Override
			public void notifyActionFailed(Exception e) {
				Log.d(getClass().getName(), "notifyActionFailed");
				updateStatusStartFailed(e);
				listenerSwitch.setEnabled(true);
			}

		};
		final ListenerServiceListener stopListener = new ListenerServiceListener() {

			@Override
			public void notifyActionSucess() {
				Log.d(getClass().getName(), "notifyActionSucess");
				updateStatusStopped();
				listenerSwitch.setEnabled(true);
			}

			@Override
			public void notifyActionFailed(Exception e) {
				Log.d(getClass().getName(), "notifyActionFailed");
				updateStatusStopFailed(e);
				listenerSwitch.setEnabled(true);
			}

		};

		listenerSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.d(getClass().getName(), "listenerSwitch.onClick : " + isChecked);
				listenerSwitch.setEnabled(false);
				if (isChecked) {
					listenerServiceBinder.startListener(startListener);
				} else {
					listenerServiceBinder.stopListener(stopListener);
				}
			}
		});

		final Button launchWifiSettingsButton = (Button) view.findViewById(R.id.switch_listener_launch_wifi_settings_button);
		launchWifiSettingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
			}
		});

		final ImageButton launchPadButton = (ImageButton) view.findViewById(R.id.switch_listener_launch_pad_button);
		final View launchPadBlock = view.findViewById(R.id.switch_listener_launch_pad_block);

		final PackageManager packageManager = getActivity().getPackageManager();
		try {
			launchPadBlock.setVisibility(View.VISIBLE);
			final Drawable padIcon = packageManager.getApplicationIcon("jp.gungho.padEN");
			launchPadButton.setImageDrawable(padIcon);
			final Intent padStartIntent = packageManager.getLaunchIntentForPackage("jp.gungho.padEN");
			launchPadButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "onClick");
					startActivity(padStartIntent);
				}
			});
		} catch (final NameNotFoundException e) {
			Log.d(getClass().getName(), "onCreateView : PAD not found", e);
			launchPadBlock.setVisibility(View.GONE);
		}

		return view;
	}

	private void updateStatusStarted() {
		final ProxyMode mode = new TechnicalSharedPreferencesHelper(getActivity()).getLastListenerStartProxyMode();
		String status = null;
		switch (mode) {
		case AUTO_WIFI_PROXY:
			status = getString(R.string.switch_listener_status_started_proxy_wifi);
			break;
		case AUTO_IPTABLES:
			status = getString(R.string.switch_listener_status_started_iptables);
			break;
		case MANUAL:
		default:
			status = getString(R.string.switch_listener_status_started_manual);
			break;
		}

		listenerStatus.setText(status);
	}

	private void updateStatusStopped() {
		listenerStatus.setText(R.string.switch_listener_status_stopped);
	}

	private void updateStatusStartFailed(Exception e) {
		listenerStatus.setText(getString(R.string.switch_listener_status_start_failed, e.getMessage()));
	}

	private void updateStatusStopFailed(Exception e) {
		listenerStatus.setText(getString(R.string.switch_listener_status_stop_failed, e.getMessage()));
	}

	@Override
	public void onDestroyView() {
		Log.d(getClass().getName(), "onDestroyView");
		super.onDestroyView();
		doUnbindService();
	}

}
