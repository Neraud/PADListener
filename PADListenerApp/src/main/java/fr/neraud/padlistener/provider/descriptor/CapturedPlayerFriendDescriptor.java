package fr.neraud.padlistener.provider.descriptor;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Descriptor for the PlayerFriendProvider
 *
 * @author Neraud
 */
public class CapturedPlayerFriendDescriptor {

	public static final String TABLE_NAME = "player_friend";
	private static final String AUTHORITY = "fr.neraud.padlistener.provider.player_friend";
	private static final String BASE_PATH = "player_friend";

	public static final String ALL_WITH_INFO_LEADER1_PREFIX = "L1_";
	public static final String ALL_WITH_INFO_LEADER2_PREFIX = "L2_";

	private static final UriMatcher URI_MATCHER = buildUriMatcher();

	/**
	 * Paths used by the StatusProvider
	 *
	 * @author Neraud
	 */
	public enum Paths {

		ALL(1, BASE_PATH, ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.player_friend"),
		ALL_WITH_INFO(2, BASE_PATH + "/withInfo", ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.player_friend_with_info");

		private final int id;
		private final String path;
		private final String contentType;

		private Paths(int id, String path, String contentType) {
			this.id = id;
			this.path = path;
			this.contentType = contentType;
		}

		public String getContentType() {
			return contentType;
		}
	}

	/**
	 * Fields used by the PlayerFriendProvider
	 *
	 * @author Neraud
	 */
	public enum Fields implements IField {

		FAKE_ID(BaseColumns._ID),
		ID("player_id"),
		NAME("name"),
		RANK("rank"),
		STARTING_COLOR("starting_color"),
		LAST_ACTIVITY("last_activity"),

		LEADER1_ID_JP("leader1_id_jp"),
		LEADER1_LEVEL("leader1_level"),
		LEADER1_SKILL_LEVEL("leader1_skillLevel"),
		LEADER1_PLUS_HP("leader1_plusHp"),
		LEADER1_PLUS_ATK("leader1_plusAtk"),
		LEADER1_PLUS_RCV("leader1_plusRcv"),
		LEADER1_AWAKENINGS("leader1_awakenings"),

		LEADER2_ID_JP("leader2_id_jp"),
		LEADER2_LEVEL("leader2_level"),
		LEADER2_SKILL_LEVEL("leader2_skillLevel"),
		LEADER2_PLUS_HP("leader2_plusHp"),
		LEADER2_PLUS_ATK("leader2_plusAtk"),
		LEADER2_PLUS_RCV("leader2_plusRcv"),
		LEADER2_AWAKENINGS("leader2_awakenings");

		private final String colName;

		private Fields(String colName) {
			this.colName = colName;
		}

		@Override
		public String getColName() {
			return colName;
		}
	}

	/**
	 * Helper for the URIs
	 *
	 * @author Neraud
	 */
	public static class UriHelper {

		private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath(BASE_PATH).build();

		/**
		 * @return the Uri to access all the PlayerFriend
		 */
		public static Uri uriForAll() {
			return CONTENT_URI;
		}

		/**
		 * @return the Uri to access all the PlayerFriend with info
		 */
		public static Uri uriForAllWithInfo() {
			final Uri.Builder uriBuilder = CONTENT_URI.buildUpon();
			uriBuilder.appendPath("withInfo");

			return uriBuilder.build();
		}
	}

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		matcher.addURI(AUTHORITY, Paths.ALL.path, Paths.ALL.id);
		matcher.addURI(AUTHORITY, Paths.ALL_WITH_INFO.path, Paths.ALL_WITH_INFO.id);

		return matcher;
	}

	/**
	 * @param uri the Uri
	 * @return the Paths matched by the uri
	 */
	public static Paths matchUri(Uri uri) {
		final int match = URI_MATCHER.match(uri);
		for (final Paths path : Paths.values()) {
			if (path.id == match) {
				return path;
			}
		}
		throw new UnsupportedOperationException("URI " + uri + " is not supported.");
	}
}
