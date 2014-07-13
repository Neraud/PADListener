package fr.neraud.padlistener.gui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import fr.neraud.padlistener.exception.MissingRequirementException;
import fr.neraud.padlistener.gui.fragment.SwitchListenerTaskFragment.ListenerState;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.helper.WifiHelper;

/**
 * Main fragment for SwitchListener
 *
 * @author Neraud
 */
public class SwitchListenerFragment extends Fragment {

	private static final String TAG_TASK_FRAGMENT = "switch_listener_task_fragment";
	private final SwitchListenerTaskFragment.CallBacks callbacks;
	private SwitchListenerTaskFragment mTaskFragment;
	private TextView listenerStatus;
	private Switch listenerSwitch;
	private TextView missingRequirementTextView;
	private TextView proxyStartedTextView;
	private OnCheckedChangeListener onCheckedListener;

	public SwitchListenerFragment() {
		callbacks = new SwitchListenerTaskFragment.CallBacks() {

			@Override
			public void updateState(ListenerState state, Throwable error) {
				Log.d(getClass().getName(), "updateState : " + state);
				proxyStartedTextView.setVisibility(View.GONE);

				updateMissingRequirementFromError(error);

				if (state != null) {
					switch (state) {
						case STARTING:
							listenerSwitch.setEnabled(false);
							forceToggledWithoutListener(true);
							break;
						case STARTED:
							listenerSwitch.setEnabled(true);
							forceToggledWithoutListener(true);
							listenerStatus.setText(generateStatusStartedText());

							String proxyUrl ="localhost:8008";
							final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
							if(prefHelper.isListenerNonLocalEnabled()) {
								final WifiHelper wifiHelper = new WifiHelper(getActivity());
								proxyUrl = wifiHelper.getWifiIpAddress() + ":8008";
							}
							proxyStartedTextView.setText(getString(R.string.switch_listener_proxy_started, proxyUrl));
							proxyStartedTextView.setVisibility(View.VISIBLE);
							break;
						case START_FAILED:
							listenerSwitch.setEnabled(true);
							forceToggledWithoutListener(false);
							listenerStatus.setText(getString(R.string.switch_listener_status_start_failed, error.getMessage()));
							break;
						case STOPPING:
							listenerSwitch.setEnabled(false);
							forceToggledWithoutListener(false);
							break;
						case STOPPED:
							listenerSwitch.setEnabled(true);
							forceToggledWithoutListener(false);
							listenerStatus.setText(R.string.switch_listener_status_stopped);
							break;
						case STOP_FAILED:
							listenerSwitch.setEnabled(true);
							forceToggledWithoutListener(true);
							listenerStatus.setText(getString(R.string.switch_listener_status_stop_failed, error.getMessage()));
							break;
						default:
					}
				} else {
					// No state -> force to stopped
					listenerSwitch.setEnabled(true);
					forceToggledWithoutListener(false);
					listenerStatus.setText(R.string.switch_listener_status_stopped);
				}
			}

			private void updateMissingRequirementFromError(Throwable error) {
				if(error instanceof MissingRequirementException) {
					final int resId = ((MissingRequirementException) error).getRequirement().getErrorTextResId();
					updateMissingRequirement(resId);
				} else {
					listenerSwitch.setClickable(true);
					missingRequirementTextView.setVisibility(View.GONE);
				}
			}

		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");
		final View view = inflater.inflate(R.layout.switch_listener_fragment, container, false);

		listenerStatus = (TextView) view.findViewById(R.id.switch_listener_status);
		listenerSwitch = (Switch) view.findViewById(R.id.switch_listener_switch);

		onCheckedListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.d(getClass().getName(), "listenerSwitch.onClick : " + isChecked);
				if (isChecked) {
					mTaskFragment.startListener();
				} else {
					mTaskFragment.stopListener(false);
				}
			}
		};
		listenerSwitch.setOnCheckedChangeListener(onCheckedListener);


        final Button forceStopButton = (Button) view.findViewById(R.id.switch_listener_force_stop_button);
        forceStopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getName(), "forceStopButton.onClick");
                mTaskFragment.stopListener(true);
            }
        });

		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());

		missingRequirementTextView = (TextView) view.findViewById(R.id.switch_listener_missing_requirement);
        final boolean requireWifi = prefHelper.getProxyMode() == ProxyMode.AUTO_WIFI_PROXY || prefHelper.isListenerNonLocalEnabled();
		final WifiHelper wifiHelper = new WifiHelper(getActivity());

        if(prefHelper.getProxyMode() == ProxyMode.MANUAL || prefHelper.getProxyMode() == ProxyMode.AUTO_WIFI_PROXY) {
	        updateMissingRequirement(R.string.switch_listener_settings_not_working_anymore);
        } else if (requireWifi && !wifiHelper.isWifiConnected()) {
	        updateMissingRequirement(R.string.switch_listener_settings_require_wifi);
        } else {
			listenerSwitch.setClickable(true);
            missingRequirementTextView.setVisibility(View.GONE);
		}

		proxyStartedTextView = (TextView) view.findViewById(R.id.switch_listener_proxy_started);
		proxyStartedTextView.setVisibility(View.GONE);

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

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (SwitchListenerTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new SwitchListenerTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		}
		mTaskFragment.registerCallbacks(callbacks);

		return view;
	}

	private void updateMissingRequirement(int textResId) {
		listenerSwitch.setClickable(false);
		missingRequirementTextView.setTextColor(Color.RED);
		missingRequirementTextView.setVisibility(View.VISIBLE);
		missingRequirementTextView.setText(textResId);
	}

	private String generateStatusStartedText() {
		final ProxyMode mode = new TechnicalSharedPreferencesHelper(getActivity()).getLastListenerStartProxyMode();
		String status;
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
		return status;
	}

	private void forceToggledWithoutListener(boolean checked) {
		if (listenerSwitch.isChecked() != checked) {
			listenerSwitch.setOnCheckedChangeListener(null);
			listenerSwitch.setChecked(checked);
			listenerSwitch.setOnCheckedChangeListener(onCheckedListener);
		}
	}

}
