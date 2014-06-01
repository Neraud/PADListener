
package fr.neraud.padlistener.model;

import java.io.Serializable;

/**
 * UserInfoMaterial model resulting from a PADherder user-api call for the materials section
 * 
 * @author Neraud
 */
public class UserInfoMaterialModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private long padherderId;
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPadherderId() {
		return padherderId;
	}

	public void setPadherderId(long padherderId) {
		this.padherderId = padherderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
