
package fr.neraud.padlistener.pad.constant;

public enum ApiAction {

	LOGIN("login"),
	GET_PLAYER_DATA("get_player_data"),

	COMPOSITE_USER_CARDS("composite_user_cards"),
	SAVE_DECKS("save_decks"),

	GET_USER_MAILS("get_user_mails"),
	GET_USER_MAIL("get_user_mail"),
	DELETE_USER_MAIL("delete_user_mail"),

	PLAY_GACHA("play_gacha"),

	GET_RECOMMENDED_HELPERS("get_recommended_helpers"),
	SNEAK_DUNGEON("sneak_dungeon"),
	SNEAK_DUNGEON_ACK("sneak_dungeon_ack"),
	CLEAR_DUNGEON("clear_dungeon"),
	CONFIRM_LEVEL_UP("confirm_level_up"),

	REQUEST_FRIEND("request_friend"),
	ACCEPT_FRIEND_REQUEST("accept_friend_request"),

	UNKNOWN("unknown");

	private final String actionString;

	private ApiAction(String actionString) {
		this.actionString = actionString;
	}

	public static ApiAction fromString(String actionString) {
		for (final ApiAction action : values()) {
			if (action.actionString.equals(actionString)) {
				return action;
			}
		}
		return UNKNOWN;
	}

}
