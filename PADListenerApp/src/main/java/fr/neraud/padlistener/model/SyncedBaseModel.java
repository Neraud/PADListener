package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * Base model for syncing. Stores captured and padherder information.
 *
 * @author Neraud
 */
public abstract class SyncedBaseModel<C, P> implements Serializable {

	private static final long serialVersionUID = 1L;
	private C capturedInfo;
	private P padherderInfo;

	public C getCapturedInfo() {
		return capturedInfo;
	}

	public void setCapturedInfo(C capturedInfo) {
		this.capturedInfo = capturedInfo;
	}

	public P getPadherderInfo() {
		return padherderInfo;
	}

	public void setPadherderInfo(P padherderInfo) {
		this.padherderInfo = padherderInfo;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(padherderInfo).append(" -> ").append(capturedInfo);
		return builder.toString();
	}
}
