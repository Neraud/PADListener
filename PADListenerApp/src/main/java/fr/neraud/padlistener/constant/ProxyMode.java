package fr.neraud.padlistener.constant;

import fr.neraud.padlistener.R;

/**
 * Enum of proxy modes
 *
 * @author Neraud
 */
public enum ProxyMode {

	MANUAL(R.string.proxy_mode_manual, false),
	AUTO_WIFI_PROXY(R.string.proxy_mode_auto_wifi, true),
	AUTO_IPTABLES(R.string.proxy_mode_auto_iptables, true);

	private final int labelResId;
	private final boolean automatic;

	private ProxyMode(int labelResId, boolean automatic) {
		this.labelResId = labelResId;
		this.automatic = automatic;
	}

	public int getLabelResId() {
		return labelResId;
	}

	public boolean isAutomatic() {
		return automatic;
	}
}
