package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.exception.MissingRequirementException;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.helper.WifiHelper;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;

/**
 * Main fragment for SwitchListener
 *
 * @author Neraud
 */
public class SwitchListenerFragment extends Fragment {

	private static final String TAG_TASK_FRAGMENT = "switch_listener_task_fragment";
	private boolean missingRequirement;
	private final SwitchListenerTaskFragment.CallBacks callbacks;
	private SwitchListenerTaskFragment mTaskFragment;
	private TextView listenerStatus;
	private Switch listenerSwitch;
	private Button secondaryActionButton;
	private TextView proxyStartedTextView;
	private Button showLogsButton;
	private TextView errorText;
	private OnCheckedChangeListener onCheckedListener;

	public SwitchListenerFragment() {
		callbacks = new SwitchListenerTaskFragment.CallBacks() {

			@Override
			public void updateState(SwitchListenerTaskFragment.ListenerState state, SwitchListenerResult result) {
				Log.d(getClass().getName(), "updateState : " + state);
				proxyStartedTextView.setVisibility(View.GONE);

				handleError(result);

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

							String proxyUrl = "localhost:8008";
							final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
							if (prefHelper.isListenerNonLocalEnabled()) {
								final WifiHelper wifiHelper = new WifiHelper(getActivity());
								proxyUrl = wifiHelper.getWifiIpAddress() + ":8008";
							}
							proxyStartedTextView.setText(getString(R.string.switch_listener_proxy_started, proxyUrl));
							proxyStartedTextView.setVisibility(View.VISIBLE);
							break;
						case START_FAILED:
							listenerSwitch.setEnabled(true);
							forceToggledWithoutListener(false);
							listenerStatus.setText(R.string.switch_listener_status_start_failed);
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
							listenerStatus.setText(R.string.switch_listener_status_stop_failed);
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

			private void handleError(SwitchListenerResult result) {
				Log.d(getClass().getName(), "handleError");

				if (result == null || result.isSuccess()) {
					if (!missingRequirement) {
						errorText.setVisibility(View.GONE);
						showLogsButton.setVisibility(View.GONE);
					}
				} else if (result.getError() instanceof MissingRequirementException) {
					final int resId = ((MissingRequirementException) result.getError()).getRequirement().getErrorTextResId();
					updateMissingRequirement(resId);
				} else {
					updateError(result.getError().getMessage(), result);
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


		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
		final ProxyMode proxyMode = prefHelper.getProxyMode();

		proxyStartedTextView = (TextView) view.findViewById(R.id.switch_listener_proxy_started);
		proxyStartedTextView.setVisibility(View.GONE);

		secondaryActionButton = (Button) view.findViewById(R.id.switch_listener_secondary_action_button);
		if (proxyMode == ProxyMode.MANUAL || proxyMode == ProxyMode.AUTO_WIFI_PROXY) {
			secondaryActionButton.setText(R.string.switch_listener_launch_wifi_settings);
			secondaryActionButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "onClick");
					startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
				}
			});
		} else if (proxyMode == ProxyMode.AUTO_IPTABLES) {
			secondaryActionButton.setText(R.string.switch_listener_force_stop);
			secondaryActionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(getClass().getName(), "secondaryActionButton.onClick");
					mTaskFragment.stopListener(true);
				}
			});
		} else {
			secondaryActionButton.setVisibility(View.INVISIBLE);
		}

		errorText = (TextView) view.findViewById(R.id.switch_listener_error_text);
		errorText.setVisibility(View.GONE);
		showLogsButton = (Button) view.findViewById(R.id.switch_listener_show_logs_button);
		showLogsButton.setVisibility(View.GONE);

		final boolean requireWifi = proxyMode == ProxyMode.AUTO_WIFI_PROXY || prefHelper.isListenerNonLocalEnabled();
		final WifiHelper wifiHelper = new WifiHelper(getActivity());

		if (prefHelper.getAllListenerTargetHostnames().isEmpty()) {
			updateMissingRequirement(R.string.switch_listener_settings_no_target_hostname);
		} else if (requireWifi && !wifiHelper.isWifiConnected()) {
			updateMissingRequirement(R.string.switch_listener_settings_require_wifi);
		} else {
			listenerSwitch.setClickable(true);
			errorText.setVisibility(View.GONE);
		}

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
		Log.d(getClass().getName(), "updateMissingRequirement");

		missingRequirement = true;
		listenerSwitch.setClickable(false);
		secondaryActionButton.setClickable(false);
		errorText.setTextColor(Color.RED);
		errorText.setVisibility(View.VISIBLE);
		errorText.setText(textResId);
	}

	private void updateError(String errorMessage, final SwitchListenerResult result) {
		Log.d(getClass().getName(), "updateError");

		listenerSwitch.setClickable(false);
		secondaryActionButton.setClickable(false);
		errorText.setTextColor(Color.RED);
		errorText.setVisibility(View.VISIBLE);
		errorText.setText(errorMessage);

		showLogsButton.setVisibility(View.VISIBLE);
		showLogsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "showLogsButton.onClick");

				final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle(R.string.switch_listener_show_logs_title);

				final WebView wv = new WebView(getActivity());
				final StringBuilder dataBuilder = new StringBuilder();
				dataBuilder.append("<html><body>");
				if (result.getError() != null) {
					dataBuilder.append("<h2>").append(result.getError().getMessage()).append("</h2><br/>");
					final StringWriter sw = new StringWriter();
					final PrintWriter pw = new PrintWriter(sw);
					result.getError().printStackTrace(pw);
					final String stackTrace = sw.toString();
					pw.close();
					dataBuilder.append("<b>Stacktrace</b> : <br/>");
					dataBuilder.append("<pre>").append(stackTrace).append("</pre>");
				}

				if (result.getLogs() != null) {
					dataBuilder.append("<b>Logs</b> : <br/>");
					dataBuilder.append("<pre>");
					for (final String logLine : result.getLogs()) {
						dataBuilder.append(logLine).append("\n");
					}
					dataBuilder.append("</pre>");
				}

				dataBuilder.append("</body></html>");
				wv.loadDataWithBaseURL("", dataBuilder.toString(), "text/html", "UTF-8", "");

				alert.setView(wv);
				alert.setNegativeButton(R.string.switch_listener_show_logs_close, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				alert.show();
			}
		});
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