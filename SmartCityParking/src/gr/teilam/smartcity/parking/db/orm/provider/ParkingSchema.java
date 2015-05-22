package gr.teilam.smartcity.parking.db.orm.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class ParkingSchema {
	/**
	 * Project Related Constants
	 */
	public static final String ORGANIZATIONAL_NAME = "gr.teilam.smartcity";
	public static final String PROJECT_NAME = "parking";
	public static final String DATABASE_NAME = "parking.db";
	public static final int DATABASE_VERSION = 1;
	
	/**
	 * ConentProvider Related Constants
	 */
	public static final String AUTHORITY = ORGANIZATIONAL_NAME + "." + PROJECT_NAME + ".parkingrovider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final UriMatcher URI_MATCHER = buildUriMatcher();

    // register identifying URIs for Restaurant entity
    // the TOKEN value is associated with each URI registered
    private static UriMatcher buildUriMatcher() {

        // add default 'no match' result to matcher
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // ST:addMatcherURIs:inline
        // Story URIs
        matcher.addURI(AUTHORITY, Parking.PATH, Parking.PATH_TOKEN);
        matcher.addURI(AUTHORITY, Parking.PATH_FOR_ID, Parking.PATH_FOR_ID_TOKEN);
        // ST:addMatcherURIs:complete
        return matcher;

    }
    
	// Define a static class that represents description of stored content entity.
	public static class Parking {
		// define a URI paths to access entity
		public static final String TABLE_NAME = "parking_table";
		public static final String PATH = "parking";
		public static final int PATH_TOKEN = 110;

		public static final String PATH_FOR_ID = "parking/*";
		public static final int PATH_FOR_ID_TOKEN = 120;

		public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

		private final static String MIME_TYPE_END = "Parking";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME + ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME + ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { 
			Cols.ID, Cols.TITLE, Cols.REGION,
				Cols.STATE, Cols.DISTRICT, Cols.ADDRESS, Cols.TK, 
				Cols.PHONES, Cols.FAX, Cols.EMAIL, Cols.REMARKS,
				Cols.LATITUDE, Cols.LONGITUDE, Cols.IS_CHECKIN, Cols.CHECKIN_COUNT
		};		
		
		public static ContentValues initializeWithDefault(final ContentValues assignedValues) {
			// final Long now = Long.valueOf(System.currentTimeMillis());
			final ContentValues setValues = (assignedValues == null) ? new ContentValues() : assignedValues;
			
			if (!setValues.containsKey(Cols.TITLE)) {
				setValues.put(Cols.TITLE, "");
			}
			if (!setValues.containsKey(Cols.REGION)) {
				setValues.put(Cols.REGION, "");
			}
			if (!setValues.containsKey(Cols.STATE)) {
				setValues.put(Cols.STATE, "");
			}
			if (!setValues.containsKey(Cols.DISTRICT)) {
				setValues.put(Cols.DISTRICT, "");
			}
			if (!setValues.containsKey(Cols.ADDRESS)) {
				setValues.put(Cols.ADDRESS, "");
			}
			if (!setValues.containsKey(Cols.TK)) {
				setValues.put(Cols.TK, "");
			}
			if (!setValues.containsKey(Cols.PHONES)) {
				setValues.put(Cols.PHONES, "");
			}
			if (!setValues.containsKey(Cols.FAX)) {
				setValues.put(Cols.FAX, "");
			}
			if (!setValues.containsKey(Cols.EMAIL)) {
				setValues.put(Cols.EMAIL, "");
			}
			if (!setValues.containsKey(Cols.REMARKS)) {
				setValues.put(Cols.REMARKS, "");
			}
			if (!setValues.containsKey(Cols.LATITUDE)) {
				setValues.put(Cols.LATITUDE, 0);
			}
			if (!setValues.containsKey(Cols.LONGITUDE)) {
				setValues.put(Cols.LONGITUDE, 0);
			}
			if (!setValues.containsKey(Cols.IS_CHECKIN)) {
				setValues.put(Cols.IS_CHECKIN, 0);
			}
			if (!setValues.containsKey(Cols.CHECKIN_COUNT)) {
				setValues.put(Cols.CHECKIN_COUNT, 0);
			}
			return setValues;
		}

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			public static final String TITLE = "TITLE";
			public static final String REGION = "REGION";
			public static final String STATE = "STATE";
			public static final String DISTRICT = "DISTRICT";
			public static final String ADDRESS = "ADDRESS";
			public static final String TK = "TK";
			public static final String PHONES = "PHONES";
			public static final String FAX = "FAX";
			public static final String EMAIL = "EMAIL";
			public static final String REMARKS = "REMARKS";
			public static final String LATITUDE = "LATITUDE";
			public static final String LONGITUDE = "LONGITUDE";
			public static final String IS_CHECKIN = "IS_CHECKIN";
			public static final String CHECKIN_COUNT = "CHECKIN_COUNT";
		}
	}
}
