
package fr.neraud.padlistener.provider.descriptor;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Descriptor for the MonsterInfoProvider
 * 
 * @author Neraud
 */
public class MonsterInfoDescriptor {

	public static final String TABLE_NAME = "monster_info";
	private static final String AUTHORITY = "fr.neraud.padlistener.provider.monster_info";
	private static final String BASE_PATH = "monster_info";

	private static final UriMatcher URI_MATCHER = buildUriMatcher();

	/**
	 * Helper for the URIs
	 * 
	 * @author Neraud
	 */
	public static class UriHelper {

		private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY).buildUpon().appendPath(BASE_PATH).build();

		/**
		 * @return the Uri to access all the MonsterInfo
		 */
		public static Uri uriForAll() {
			return CONTENT_URI;
		}

		/**
		 * @param monsterId the monsterId
		 * @return the Uri to access the image of one monster
		 */
		public static Uri uriForImage(long monsterId) {
			final Uri.Builder uriBuilder = CONTENT_URI.buildUpon();
			uriBuilder.appendPath("image");
			uriBuilder.appendPath(String.valueOf(monsterId));

			return uriBuilder.build();
		}

		/**
		 * @param monsterId the monsterId
		 * @return the Uri to access one monster
		 */
		public static Uri uriById(long monsterId) {
			final Uri.Builder uriBuilder = CONTENT_URI.buildUpon();
			uriBuilder.appendPath(String.valueOf(monsterId));

			return uriBuilder.build();
		}
	}

	/**
	 * Paths used by the StatusProvider
	 * 
	 * @author Neraud
	 */
	public enum Paths {

		ALL_INFO(1, BASE_PATH, ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.fr.neraud.padlistener.monster_info"),
		INFO_BY_ID(2, BASE_PATH + "/#", ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.neraud.padlistener.monster_info"),
		IMAGE_BY_ID(3, BASE_PATH + "/image/#", ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.fr.neraud.padlistener.monster_image");

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
	 * Fields used by the MonsterInfoProvider
	 * 
	 * @author Neraud
	 */
	public enum Fields implements IField {

		ID_JP(BaseColumns._ID),
		ID_US("id_us"),
		NAME("name"),
		RARITY("rarity"),
		ELEMENT_1("element_1"),
		ELEMENT_2("element_2"),
		TYPE_1("type_1"),
		TYPE_2("type_2"),
		ACTIVE_SKILL_NAME("active_skill_name"),
		LEADER_SKILL_NAME("leader_skill_name"),
		AWOKEN_SKILL_IDS("awoken_skill_ids"),
		MAX_LEVEL("max_level"),
		XP_CURVE("xp_curve"),
		FEED_XP("feed_xp"),
		TEAM_COST("team_cost"),
		JP_ONLY("jp_only"),
		HP_MIN("hp_min"),
		HP_MAX("hp_max"),
		HP_SCALE("hp_scale"),
		ATK_MIN("atk_min"),
		ATK_MAX("atk_max"),
		ATK_SCALE("atk_scale"),
		RCV_MIN("rcv_min"),
		RCV_MAX("rcv_max"),
		RCV_SCALE("rcv_scale"),
		IMAGE_40_URL("image_40_url"),
		IMAGE_60_URL("image_60_url");

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

		matcher.addURI(AUTHORITY, Paths.ALL_INFO.path, Paths.ALL_INFO.id);
		matcher.addURI(AUTHORITY, Paths.INFO_BY_ID.path, Paths.INFO_BY_ID.id);
		matcher.addURI(AUTHORITY, Paths.IMAGE_BY_ID.path, Paths.IMAGE_BY_ID.id);

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
