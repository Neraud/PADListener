
package fr.neraud.padlistener.gui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.service.ListenerService;
import fr.neraud.padlistener.service.ListenerService.ListenerServiceBinder;

public class MainActivity extends AbstractPADListenerActivity {

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

		final Intent serviceIntent = new Intent(getApplicationContext(), ListenerService.class);

		// Start manually to prevent the service being stopped when the app is closed and unbinds
		startService(serviceIntent);
		bindService(serviceIntent, mConnection, 0/*Context.BIND_AUTO_CREATE*/);
	}

	private void doUnbindService() {
		Log.d(getClass().getName(), "doUnbindService");
		if (mIsBound) {
			// Detach our existing connection.
			unbindService(mConnection);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		doBindService();

		listenerSwitch = (Switch) findViewById(R.id.listenerSwitch);
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

		final TextView viewMonsterInfo = (TextView) findViewById(R.id.viewMonsterInfo);
		viewMonsterInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewMonsterInfo.onClick");
				startActivity(new Intent(getApplicationContext(), ViewMonsterInfoActivity.class));
			}
		});

		final TextView viewCapturedData = (TextView) findViewById(R.id.viewCapturedData);
		viewCapturedData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewCapturedData.onClick");
				startActivity(new Intent(getApplicationContext(), ViewCapturedDataActivity.class));
			}
		});

		final TextView viewPadHerder = (TextView) findViewById(R.id.viewPadHerderData);
		viewPadHerder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "viewPadHerder.onClick");
				// TODO
			}
		});
		final TextView computeSync = (TextView) findViewById(R.id.computeSync);
		computeSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "computeSync.onClick");
				// TODO
			}
		});
	}

	@Override
	protected void onDestroy() {
		Log.d(getClass().getName(), "onDestroy");
		super.onDestroy();
		doUnbindService();
	}

}
