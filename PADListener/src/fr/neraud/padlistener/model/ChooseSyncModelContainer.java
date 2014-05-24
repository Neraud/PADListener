
package fr.neraud.padlistener.model;

import java.io.Serializable;

public class ChooseSyncModelContainer<M extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean choosen;
	private M syncedModel;

	public boolean isChoosen() {
		return choosen;
	}

	public void setChoosen(boolean choosen) {
		this.choosen = choosen;
	}

	public M getSyncedModel() {
		return syncedModel;
	}

	public void setSyncedModel(M syncedModel) {
		this.syncedModel = syncedModel;
	}

}
