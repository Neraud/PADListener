package fr.neraud.padlistener.constant;

import fr.neraud.padlistener.R;

/**
 * Enum of proxy modes
 *
 * @author Neraud
 */
public enum ProxyMode {

	MANUAL(R.string.proxy_mode_manual),
	AUTO_WIFI_PROXY(R.string.proxy_mode_auto_wifi),
	AUTO_IPTABLES(R.string.proxy_mode_auto_iptables);

	private final int labelResId;

	private ProxyMode(int labelResId) {
		this.labelResId = labelResId;
	}

	public int getLabelResId() {
		return labelResId;
	}
}
