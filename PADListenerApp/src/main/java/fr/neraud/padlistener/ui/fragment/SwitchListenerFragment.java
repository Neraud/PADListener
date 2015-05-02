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
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
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
	private ProxyMode mProxyMode;
	private boolean mSwitchListenerEnabled = true;
	private SwitchListenerResult mResult;

	@InjectView(R.id.switch_listener_status)
	TextView mListenerStatus;
	@InjectView(R.id.switch_listener_switch)
	Switch mListenerSwitch;
	@InjectView(R.id.switch_listener_secondary_action_button)
	Button mSecondaryActionButton;
	@InjectView(R.id.switch_listener_proxy_started)
	TextView mProxyStartedTextView;
	@InjectView(R.id.switch_listener_show_logs_button)
	Button mShowLogsButton;
	@InjectView(R.id.switch_listener_error_text)
	TextView mErrorText;

	public SwitchListenerFragment() {
		mCallbacks = new SwitchListenerTaskFragment.CallBacks() {

			@Override
			public void updateState(SwitchListenerTaskFragment.ListenerState state, SwitchListenerResult result) {
				MyLog.entry("state = " + state);
				mResult = result;
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

							String proxyUrl = "127.0.0.1:8008";
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
		mProxyMode = prefHelper.getProxyMode();

		if (mProxyMode == ProxyMode.MANUAL || mProxyMode == ProxyMode.AUTO_WIFI_PROXY) {
			mSecondaryActionButton.setText(R.string.switch_listener_launch_wifi_settings);
		} else if (mProxyMode == ProxyMode.AUTO_IPTABLES) {
			mSecondaryActionButton.setText(R.string.switch_listener_force_stop);
		} else {
			mSecondaryActionButton.setVisibility(View.INVISIBLE);
		}

		mProxyStartedTextView.setVisibility(View.GONE);
		mErrorText.setVisibility(View.GONE);
		mShowLogsButton.setVisibility(View.GONE);

		final boolean requireWifi = mProxyMode == ProxyMode.AUTO_WIFI_PROXY || prefHelper.isListenerNonLocalEnabled();
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
		ButterKnife.inject(this, view);

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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
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

		mResult = result;
		mListenerSwitch.setClickable(false);
		mSecondaryActionButton.setClickable(false);
		mErrorText.setTextColor(Color.RED);
		mErrorText.setVisibility(View.VISIBLE);
		mErrorText.setText(errorMessage);
		mShowLogsButton.setVisibility(View.VISIBLE);

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
			mSwitchListenerEnabled = false;
			mListenerSwitch.setChecked(checked);
			mSwitchListenerEnabled = true;
		}
	}

	@OnCheckedChanged(R.id.switch_listener_switch)
	@SuppressWarnings("unused")
	void onListenerCheckChanged(boolean isChecked) {
		MyLog.entry("isChecked = " + isChecked);
		if(mSwitchListenerEnabled) {
			if (isChecked) {
				mTaskFragment.startListener();
			} else {
				mTaskFragment.stopListener(false);
			}
		}
		MyLog.exit();
	}

	@OnClick(R.id.switch_listener_show_logs_button)
	@SuppressWarnings("unused")
	void onShowLogsButtonClicked() {
		MyLog.entry();

		final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle(R.string.switch_listener_show_logs_title);

		final WebView wv = new WebView(getActivity());
		final StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("<html><body>");
		if (mResult.getError() != null) {
			dataBuilder.append("<h2>").append(mResult.getError().getMessage()).append("</h2><br/>");
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			mResult.getError().printStackTrace(pw);
			final String stackTrace = sw.toString();
			pw.close();
			dataBuilder.append("<b>Stacktrace</b> : <br/>");
			dataBuilder.append("<pre>").append(stackTrace).append("</pre>");
		}

		if (mResult.getLogs() != null) {
			dataBuilder.append("<b>Logs</b> : <br/>");
			dataBuilder.append("<pre>");
			for (final String logLine : mResult.getLogs()) {
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

	@OnClick(R.id.switch_listener_secondary_action_button)
	@SuppressWarnings("unused")
	void onSecondaryActionClicked() {
		MyLog.entry();
		if (mProxyMode == ProxyMode.MANUAL || mProxyMode == ProxyMode.AUTO_WIFI_PROXY) {
			startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
		} else if (mProxyMode == ProxyMode.AUTO_IPTABLES) {
			mTaskFragment.stopListener(true);
		}
		MyLog.exit();
	}

}
