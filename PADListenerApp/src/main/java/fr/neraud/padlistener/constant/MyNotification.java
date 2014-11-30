package fr.neraud.padlistener.constant;

/**
 * Created by Neraud on 30/11/2014.
 */
public enum MyNotification {

	LISTENER_SERVICE(1),
	ONGOING_CAPTURE(2),
	ONGOING_SYNC(3);

	private final int id;

	private MyNotification(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
