package fr.neraud.padlistener.model;

/**
 * Model for syncing user information
 *
 * @author Neraud
 */
public class SyncedUserInfoModel extends SyncedBaseModel<Integer> {

	private static final long serialVersionUID = 1L;
	private int profileApiId;

	public int getProfileApiId() {
		return profileApiId;
	}

	public void setProfileApiId(int profileApiId) {
		this.profileApiId = profileApiId;
	}

}
