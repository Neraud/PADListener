
package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * Base model for syncing. Stores captured and padherder informations.
 * 
 * @author Neraud
 * @param <T>
 */
public abstract class SyncedBaseModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private T capturedInfo;
	private T padherderInfo;

	public T getCapturedInfo() {
		return capturedInfo;
	}

	public void setCapturedInfo(T capturedInfo) {
		this.capturedInfo = capturedInfo;
	}

	public T getPadherderInfo() {
		return padherderInfo;
	}

	public void setPadherderInfo(T padherderInfo) {
		this.padherderInfo = padherderInfo;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(padherderInfo).append(" -> ").append(capturedInfo);
		return builder.toString();
	}
}
