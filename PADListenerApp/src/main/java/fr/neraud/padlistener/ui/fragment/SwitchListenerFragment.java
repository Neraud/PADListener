package fr.neraud.padlistener.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

import fr.neraud.log.MyLog;
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
	private final SwitchListenerTaskFragment.CallBacks mCallbacks;
	private SwitchListenerTaskFragment mTaskFragment;
	private TextView mListenerStatus;
	private Switch mListenerSwitch;
	private Button mSecondaryActionButton;
	private TextView mProxyStartedTextView;
	private Button mShowLogsButton;
	private TextView mErrorText;
	private OnCheckedChangeListener mOnCheckedListener;

	public SwitchListenerFragment() {
		mCallbacks = new SwitchListenerTaskFragment.CallBacks() {

			@Override
			public void updateState(SwitchListenerTaskFragment.ListenerState state, SwitchListenerResult result) {
				MyLog.entry("state = " + state);
				mProxyStartedTextView.setVisibility(View.GONE);

				resetState();

				if (result != null && !result.isSuccess()) {
					if (result.getError() instanceof MissingRequirementException) {
						final int resId = ((MissingRequirementException) result.getError()).getRequirement().getErrorTextResId();
						updateMissingRequirement(resId);
					} else {
						updateError(result.getError().getMessage(), result);
					}
				}

				if (state != null) {
					switch (state) {
						case STARTING:
							mListenerSwitch.setEnabled(false);
							forceToggledWithoutListener(true);
							break;
						case STARTED:
							mListenerSwitch.setEnabled(true);
							forceToggledWithoutListener(true);
							mListenerStatus.setText(generateStatusStartedText());

							String proxyUrl = "localhost:8008";
							final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
							if (prefHelper.isListenerNonLocalEnabled()) {
								final WifiHelper wifiHelper = new WifiHelper(getActivity());
								proxyUrl = wifiHelper.getWifiIpAddress() + ":8008";
							}
							mProxyStartedTextView.setText(getString(R.string.switch_listener_proxy_started, proxyUrl));
							mProxyStartedTextView.setVisibility(View.VISIBLE);
							break;
						case START_FAILED:
							mListenerSwitch.setEnabled(true);
							forceToggledWithoutListener(false);
							mListenerStatus.setText(R.string.switch_listener_status_start_failed);
							break;
						case STOPPING:
							mListenerSwitch.setEnabled(false);
							forceToggledWithoutListener(false);
							break;
						case STOPPED:
							mListenerSwitch.setEnabled(true);
							forceToggledWithoutListener(false);
							mListenerStatus.setText(R.string.switch_listener_status_stopped);
							break;
						case STOP_FAILED:
							mListenerSwitch.setEnabled(true);
							forceToggledWithoutListener(true);
							mListenerStatus.setText(R.string.switch_listener_status_stop_failed);
							break;
						default:
					}
				} else {
					// No state -> force to stopped
					mListenerSwitch.setEnabled(true);
					forceToggledWithoutListener(false);
					mListenerStatus.setText(R.string.switch_listener_status_stopped);
				}
				MyLog.exit();
			}
		};
	}

	private void resetState() {
		MyLog.entry();

		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getActivity());
		final ProxyMode proxyMode = prefHelper.getProxyMode();

		if (proxyMode == ProxyMode.MANUAL || proxyMode == ProxyMode.AUTO_WIFI_PROXY) {
			mSecondaryActionButton.setText(R.string.switch_listener_launch_wifi_settings);
			mSecondaryActionButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyLog.entry();
					startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
					MyLog.exit();
				}
			});
		} else if (proxyMode == ProxyMode.AUTO_IPTABLES) {
			mSecondaryActionButton.setText(R.string.switch_listener_force_stop);
			mSecondaryActionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyLog.entry();
					mTaskFragment.stopListener(true);
					MyLog.exit();
				}
			});
		} else {
			mSecondaryActionButton.setVisibility(View.INVISIBLE);
		}

		mProxyStartedTextView.setVisibility(View.GONE);
		mErrorText.setVisibility(View.GONE);
		mShowLogsButton.setVisibility(View.GONE);

		final boolean requireWifi = proxyMode == ProxyMode.AUTO_WIFI_PROXY || prefHelper.isListenerNonLocalEnabled();
		final WifiHelper wifiHelper = new WifiHelper(getActivity());

		if (prefHelper.getAllListenerTargetHostnames().isEmpty()) {
			updateMissingRequirement(R.string.switch_listener_settings_no_target_hostname);
		} else if (requireWifi && !wifiHelper.isWifiConnected()) {
			updateMissingRequirement(R.string.switch_listener_settings_require_wifi);
		} else {
			mListenerSwitch.setClickable(true);
			mErrorText.setVisibility(View.GONE);
		}

		MyLog.exit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.switch_listener_fragment, container, false);

		mListenerStatus = (TextView) view.findViewById(R.id.switch_listener_status);
		mListenerSwitch = (Switch) view.findViewById(R.id.switch_listener_switch);

		mOnCheckedListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MyLog.entry("isChecked = " + isChecked);
				if (isChecked) {
					mTaskFragment.startListener();
				} else {
					mTaskFragment.stopListener(false);
				}
				MyLog.exit();
			}
		};
		mListenerSwitch.setOnCheckedChangeListener(mOnCheckedListener);

		mProxyStartedTextView = (TextView) view.findViewById(R.id.switch_listener_proxy_started);
		mSecondaryActionButton = (Button) view.findViewById(R.id.switch_listener_secondary_action_button);
		mErrorText = (TextView) view.findViewById(R.id.switch_listener_error_text);
		mShowLogsButton = (Button) view.findViewById(R.id.switch_listener_show_logs_button);

		final FragmentManager fm = getFragmentManager();
		mTaskFragment = (SwitchListenerTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new SwitchListenerTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
		}
		mTaskFragment.registerCallbacks(mCallbacks);

		MyLog.exit();
		return view;
	}

	private void updateMissingRequirement(int textResId) {
		MyLog.entry();

		mListenerSwitch.setClickable(false);
		mSecondaryActionButton.setClickable(false);
		mErrorText.setTextColor(Color.RED);
		mErrorText.setVisibility(View.VISIBLE);
		mErrorText.setText(textResId);

		MyLog.exit();
	}

	private void updateError(String errorMessage, final SwitchListenerResult result) {
		MyLog.entry();

		mListenerSwitch.setClickable(false);
		mSecondaryActionButton.setClickable(false);
		mErrorText.setTextColor(Color.RED);
		mErrorText.setVisibility(View.VISIBLE);
		mErrorText.setText(errorMessage);

		mShowLogsButton.setVisibility(View.VISIBLE);
		mShowLogsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();

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
				MyLog.exit();
			}
		});

		MyLog.exit();
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
		if (mListenerSwitch.isChecked() != checked) {
			mListenerSwitch.setOnCheckedChangeListener(null);
			mListenerSwitch.setChecked(checked);
			mListenerSwitch.setOnCheckedChangeListener(mOnCheckedListener);
		}
	}

}
