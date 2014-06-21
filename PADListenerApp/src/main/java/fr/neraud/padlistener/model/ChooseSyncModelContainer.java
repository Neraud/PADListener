package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * Container for ChooseSync. Wraps a syncedModel and adds a flag to indicate if te user has choose to sync this model.
 *
 * @author Neraud
 */
public class ChooseSyncModelContainer<M extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean chosen;
	private M syncedModel;

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public M getSyncedModel() {
		return syncedModel;
	}

	public void setSyncedModel(M syncedModel) {
		this.syncedModel = syncedModel;
	}

	@Override
	public String toString() {
		return chosen + " -> " + syncedModel;
	}
}
