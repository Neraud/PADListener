package fr.neraud.padlistener.service.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.sandrop.webscarab.model.Preferences;
import org.sandroproxy.utils.PreferenceUtils;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.exception.CommandExecutionException;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.proxy.helper.IptablesHelper;
import fr.neraud.padlistener.proxy.helper.ProxyHelper;
import fr.neraud.padlistener.proxy.helper.WifiAutoProxyHelper;
import fr.neraud.padlistener.service.ListenerService;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;

/**
 * AsyncTask used to start the Listener
 * Created by Neraud on 13/07/2014.
 */
public class StartListenerAsyncTask extends AsyncTask<Void, Void, SwitchListenerResult> {

	private final Context context;
	private final ProxyHelper proxyHelper;
	private final ListenerService.CaptureListener captureListener;
	private final DefaultSharedPreferencesHelper prefHelper;
	private final ProxyMode proxyMode;

	public StartListenerAsyncTask(Context context, ProxyHelper proxyHelper, ListenerService.CaptureListener captureListener) {
		this.context = context;
		this.proxyHelper = proxyHelper;
		this.captureListener = captureListener;

		prefHelper = new DefaultSharedPreferencesHelper(context);
		proxyMode = prefHelper.getProxyMode();
	}

	protected ProxyMode getProxyMode() {
		return proxyMode;
	}

	@Override
	protected SwitchListenerResult doInBackground(Void... params) {
		MyLog.entry();

		final SwitchListenerResult result = new SwitchListenerResult();

		final ProxyMode proxyMode = getProxyMode();
		initValues();
		Preferences.init(context);

		try {
			final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(context);
			techPrefHelper.setLastListenerStartProxyMode(proxyMode);

			proxyHelper.activateProxy(captureListener);

			switch (proxyMode) {
				case AUTO_IPTABLES:
					final IptablesHelper iptablesHelper = new IptablesHelper(context);
					iptablesHelper.activateIptables();
					break;
				case AUTO_WIFI_PROXY:
					final WifiAutoProxyHelper wifiAutoProxyHelper = new WifiAutoProxyHelper(context);
					wifiAutoProxyHelper.activateAutoProxy();
					break;
				case MANUAL:
				default:
					// Nothing to do
					break;
			}
			result.setSuccess(true);
		} catch (final CommandExecutionException e) {
			MyLog.error("PADListener start failed  : " + e.getMessage(), e);
			result.setSuccess(false);
			result.setError(e);
			result.setLogs(e.getLogs());
		} catch (final Exception e) {
			MyLog.error("PADListener start failed  : " + e.getMessage(), e);
			result.setSuccess(false);
			result.setError(e);
		}

		MyLog.exit();
		return result;
	}

	private void initValues() {
		MyLog.entry();

		final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

		// listen to all adapters only if non local mode is enabled
		final boolean proxyListenNonLocal = prefHelper.isListenerNonLocalEnabled();

		// Transparent proxy only for iptables mode
		final boolean isModeIptables = prefHelper.getProxyMode() == ProxyMode.AUTO_IPTABLES;

		// checking for directory to write data...
		editor.putString(PreferenceUtils.dataStorageKey, context.getExternalCacheDir().getAbsolutePath());

		// should we listen on all adapters ?
		editor.putBoolean(PreferenceUtils.proxyListenNonLocal, proxyListenNonLocal);

		// should we listen also for transparent flow ?
		editor.putBoolean(PreferenceUtils.proxyTransparentKey, isModeIptables);

		editor.putBoolean(PreferenceUtils.proxyFakeCerts, isModeIptables);

		// Capture data is necessary for SSL capture to work
		editor.putBoolean(PreferenceUtils.proxyCaptureData, true);

		editor.commit();

		MyLog.exit();
	}
}
