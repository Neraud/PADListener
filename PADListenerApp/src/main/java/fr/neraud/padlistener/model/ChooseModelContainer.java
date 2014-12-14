package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * Wraps a model and adds a flag to indicate if te user has choose to sync this model.
 *
 * @author Neraud
 */
public class ChooseModelContainer<M extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean chosen;
	private M model;

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return chosen + " -> " + model;
	}
}
