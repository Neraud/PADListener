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
public class CapturedPlayerFriendLeaderDescriptor {

	public static final String TABLE_NAME = "player_friend_leader";
	private static final String AUTHORITY = "fr.neraud.padlistener.provider.player_friend_leader";
	private static final String BASE_PATH = "player_friend_leader";

	public static final String ALL_WITH_INFO_PREFIX = "INFO_";

	private static final UriMatcher URI_MATCHER = buildUriMatcher();

	/**
	 * Paths used by the StatusProvider
	 *
	 * @author Neraud
	 */
	public enum Paths {

		ALL(1, BASE_PATH, ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.player_friend_leader"),
		ALL_WITH_INFO(2, BASE_PATH + "/withInfo", ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.player_friend_leader_with_info"),
		ALL_WITH_INFO_BY_ID(3, BASE_PATH + "/#/withInfo", ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.player_friend_leader_with_info");

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

		_ID(BaseColumns._ID),
		PLAYER_ID("player_id"),
		LAST_SEEN("last_seen"),
		ID_JP("leader_id_jp"),
		LEVEL("leader_level"),
		SKILL_LEVEL("leader_skillLevel"),
		PLUS_HP("leader_plusHp"),
		PLUS_ATK("leader_plusAtk"),
		PLUS_RCV("leader_plusRcv"),
		AWAKENINGS("leader_awakenings");

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
		matcher.addURI(AUTHORITY, Paths.ALL_WITH_INFO_BY_ID.path, Paths.ALL_WITH_INFO_BY_ID.id);

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
