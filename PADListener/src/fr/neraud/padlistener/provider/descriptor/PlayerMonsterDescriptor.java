
package fr.neraud.padlistener.provider.descriptor;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Descriptor for the PlayerMonsterProvider
 * 
 * @author Neraud
 */
public class PlayerMonsterDescriptor {

	public static final String TABLE_NAME = "player_monster";
	private static final String AUTHORITY = "fr.neraud.padlistener.provider.player_monster";
	private static final String BASE_PATH = "player_monster";

	private static final UriMatcher URI_MATCHER = buildUriMatcher();

	/**
	 * Helper for the URIs
	 * 
	 * @author Neraud
	 */
	public static class UriHelper {

		private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath(BASE_PATH).build();

		/**
		 * @return the Uri to access all the PlayerMonster
		 */
		public static Uri uriForAll() {
			return CONTENT_URI;
		}
	}

	/**
	 * Paths used by the StatusProvider
	 * 
	 * @author Neraud
	 */
	public enum Paths {

		ALL(1, BASE_PATH, ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.player_monster");

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
	 * Fields used by the PlayerMonsterProvider
	 * 
	 * @author Neraud
	 */
	public enum Fields implements IField {

		FAKE_ID(BaseColumns._ID),
		MONSTER_ID("monster_id"),
		EXP("exp"),
		LEVEL("level"),
		SKILL_LEVEL("skillLevel"),
		PLUS_HP("plusHp"),
		PLUS_ATK("plusAtk"),
		PLUS_RCV("plusRcv"),
		AWAKENINGS("awakenings");

		private final String colName;

		private Fields(String colName) {
			this.colName = colName;
		}

		@Override
		public String getColName() {
			return colName;
		}
	}

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		matcher.addURI(AUTHORITY, Paths.ALL.path, Paths.ALL.id);

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
