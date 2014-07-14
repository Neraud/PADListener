package fr.neraud.padlistener.service.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import fr.neraud.padlistener.constant.ProxyMode;
import fr.neraud.padlistener.exception.CommandExecutionException;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.proxy.helper.IptablesHelper;
import fr.neraud.padlistener.proxy.helper.ProxyHelper;
import fr.neraud.padlistener.proxy.helper.WifiAutoProxyHelper;
import fr.neraud.padlistener.service.task.model.SwitchListenerResult;

/**
 * AsyncTask used to stop the Listener
 * Created by Neraud on 13/07/2014.
 */
public class StopListenerAsyncTask extends AsyncTask<Void, Void, SwitchListenerResult> {

	private final Context context;
	private final ProxyHelper proxyHelper;

	public StopListenerAsyncTask(Context context, ProxyHelper proxyHelper) {
		this.context = context;
		this.proxyHelper = proxyHelper;
	}

	@Override
	protected SwitchListenerResult doInBackground(Void... params) {
		Log.d(getClass().getName(), "doInBackground");
		final SwitchListenerResult result = new SwitchListenerResult();

		try {
			proxyHelper.deactivateProxy();

			final TechnicalSharedPreferencesHelper techPrefHelper = new TechnicalSharedPreferencesHelper(context);
			final ProxyMode proxyMode = techPrefHelper.getLastListenerStartProxyMode();
			switch (proxyMode) {
				case AUTO_IPTABLES:
					final IptablesHelper iptablesHelper = new IptablesHelper(context);
					iptablesHelper.deactivateIptables();
					break;
				case AUTO_WIFI_PROXY:
					final WifiAutoProxyHelper wifiAutoProxyHelper = new WifiAutoProxyHelper(context);
					wifiAutoProxyHelper.deactivateAutoProxy();
					break;
				case MANUAL:
				default:
					// Nothing to do
					break;
			}

			result.setSuccess(true);
		} catch (final CommandExecutionException e) {
			Log.e(getClass().getName(), "PADListener stop failed  : " + e.getMessage(), e);
			result.setSuccess(false);
			result.setError(e);
			result.setLogs(e.getLogs());
		} catch (final Exception e) {
			Log.e(getClass().getName(), "PADListener stop failed  : " + e.getMessage());
			result.setSuccess(false);
			result.setError(e);
		}

		return result;
	}
}
