package gr.teilam.smartcity.parking.db.orm.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import gr.teilam.smartcity.parking.db.ParkingDataDBAdapter;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class ParkingProvider extends ContentProvider {
	private final static String LOG_TAG = ParkingProvider.class.getCanonicalName();

    // Local backend DB
    ParkingDataDBAdapter mDB;

    // shorten variable names for easier readability

    // ST:createShortContentURIforRelations:begin
    public final static Uri PARKING_CONTENT_URI = ParkingSchema.Parking.CONTENT_URI;
    // ST:createShortContentURIforRelations:finish

    public static String AUTHORITY = ParkingSchema.AUTHORITY;

    // ST:createShortURIMatchingTokens:begin
    public static final int PARKING_ALL_ROWS = ParkingSchema.Parking.PATH_TOKEN;
    public static final int PARKING_SINGLE_ROW = ParkingSchema.Parking.PATH_FOR_ID_TOKEN;
    // ST:createShortURIMatchingTokens:finish

    private static final UriMatcher uriMatcher = ParkingSchema.URI_MATCHER;

	@Override
	/**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the application
     * main thread at application launch time. It must not perform lengthy operations,
     * or application startup will be delayed.
     */
	synchronized public boolean onCreate() {
		Log.d(LOG_TAG, "onCreate()");
        mDB = new ParkingDataDBAdapter(getContext());
        mDB.open();
        return true;
	}

	@Override
	/**
     * Retrieve data from your provider. Use the arguments to select the table to query,
     * the rows and columns to return, and the sort order of the result. Return the data as a Cursor object.
     */
	synchronized public Cursor query(final Uri uri, final String[] projection,
            final String selection, final String[] selectionArgs,
            final String sortOrder) {
        Log.d(LOG_TAG, "query()");
        String modifiedSelection = selection;
        switch (uriMatcher.match(uri)) {

        // ST:createPublicQueryforRelations:begin
        case PARKING_SINGLE_ROW: {
            modifiedSelection = modifiedSelection + ParkingSchema.Parking.Cols.ID + " = " + uri.getLastPathSegment();
        }
        case PARKING_ALL_ROWS: {
            return query(uri, ParkingSchema.Parking.TABLE_NAME, projection, modifiedSelection, selectionArgs, sortOrder);
        }
        // ST:createPublicQueryforRelations:finish

        default:
            return null;
        }

    }

	/*
     * Private query that does the actual query based on the table
     */
    synchronized private Cursor query(final Uri uri, final String tableName,
            final String[] projection, final String selection,
            final String[] selectionArgs, final String sortOrder) {

        // TODO: Perform a query on the database with the given parameters
    	Cursor cursor = mDB.query(tableName, projection, selection, selectionArgs, sortOrder);
    	
    	
    	return cursor; // return null;
    }
    
	@Override
	/**
     * Implement this to handle requests for the MIME type of the data at the given URI. 
     * The returned MIME type should start with vnd.android.cursor.item for a single record,
     * or vnd.android.cursor.dir/ for multiple items. This method can be called from multiple 
     * threads, as described in Processes and Threads.
     */
	synchronized public String getType(Uri uri) {
		Log.d(LOG_TAG, "getType()");
        switch (uriMatcher.match(uri)) {

        // ST:createContentTypeReturnsforRelations:begin
        case PARKING_ALL_ROWS:
            return ParkingSchema.Parking.CONTENT_TYPE_DIR;
        case PARKING_SINGLE_ROW:
            return ParkingSchema.Parking.CONTENT_ITEM_TYPE;
            // ST:createContentTypeReturnsforRelations:finish

        default:
            throw new UnsupportedOperationException("URI " + uri
                    + " is not supported.");
        }
	}

	@Override
	/**
     * Implement this to handle requests to insert a new row. As a courtesy,
     * call notifyChange() after inserting. This method can be called from multiple threads, 
     * as described in Processes and Threads.
     * <p>
     * (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
	synchronized public Uri insert(Uri uri, ContentValues assignedValues) {
		Log.d(LOG_TAG, "query()");
        final int match = uriMatcher.match(uri);
        switch (match) {
	        // ST:createUpsertForRelations:begin
	        case PARKING_ALL_ROWS: {
	            final ContentValues values = ParkingSchema.Parking.initializeWithDefault(assignedValues);
	            values.remove(ParkingSchema.Parking.Cols.ID);
	
	            final long rowID = mDB.insert(ParkingSchema.Parking.TABLE_NAME, values);
	            if (rowID < 0) {
	                Log.d(LOG_TAG, "query()");
	                return null;
	            }
	            final Uri insertedID = ContentUris.withAppendedId(
	                    PARKING_CONTENT_URI, rowID);
	            getContext().getContentResolver().notifyChange(insertedID, null);
	            return ContentUris.withAppendedId(PARKING_CONTENT_URI, rowID);
	        }
	        // ST:createUpsertForRelations:finish
	
	        // breaks intentionally omitted
	        case PARKING_SINGLE_ROW:
	        default: {
	            throw new IllegalArgumentException("Unsupported URI: " + uri);
	        }
        }
	}

	@Override
    /**
     * Override this to handle requests to open a file blob.
     */
    public ParcelFileDescriptor openFile(Uri uri, String mode) {
        int imode = 0;
        try {
            if (mode.contains("w")) {
                imode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
            }
            if (mode.contains("r")) {
                imode |= ParcelFileDescriptor.MODE_READ_ONLY;
            }
            if (mode.contains("+")) {
                imode |= ParcelFileDescriptor.MODE_APPEND;
            }
        } finally {
        }

        int token = ParkingSchema.URI_MATCHER.match(uri);
        File imageDirectory = getContext().getCacheDir();
        switch (token) {
	        case ParkingSchema.Parking.PATH_FOR_ID_TOKEN: {
	            final List<String> segments = uri.getPathSegments();
	            final File storyFile = new File(imageDirectory, "Parking" + segments.get(1));
	            try {
	                if (!storyFile.exists()) {
	                    storyFile.createNewFile();
	                }
	                return ParcelFileDescriptor.open(storyFile, imode);
	            } catch (FileNotFoundException ex) {
	            } catch (IOException ex) {
	            }
	        }
	        default: {
	            throw new UnsupportedOperationException("URI: " + uri + " not supported.");
	        }
        }
    }
	
	@Override
	/**
     * Implement this to handle requests to delete one or more rows.
     */
    synchronized public int delete(Uri uri, String whereClause, String[] whereArgs) {
		switch (uriMatcher.match(uri)) {
        // ST:createDeleteforRelations:begin
        case PARKING_SINGLE_ROW:
            whereClause = whereClause + ParkingSchema.Parking.Cols.ID + " = " + uri.getLastPathSegment();
            // no break here on purpose
        case PARKING_ALL_ROWS: {
            return deleteAndNotify(uri, ParkingSchema.Parking.TABLE_NAME, whereClause, whereArgs);
        }
        // ST:createDeleteforRelations:finish
        default:
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
	}

	/**
	* Private method to both attempt the delete command, and then to notify of
    * the changes
    */
   private int deleteAndNotify(final Uri uri, final String tableName,
           final String whereClause, final String[] whereArgs) {
       int count = mDB.delete(tableName, whereClause, whereArgs);
       if (count > 0) {
           getContext().getContentResolver().notifyChange(uri, null);
       }
       return count;
   }
	
	@Override
	/**
     * Implement this to handle requests to update one or more rows.
     */
    synchronized public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        Log.d(LOG_TAG, "query()");

        switch (uriMatcher.match(uri)) {

        // ST:createUpdateForRelations:begin
        case PARKING_SINGLE_ROW:
            whereClause = whereClause + ParkingSchema.Parking.Cols.ID + " = "
                    + uri.getLastPathSegment();
        case PARKING_ALL_ROWS: {
            return updateAndNotify(uri, ParkingSchema.Parking.TABLE_NAME, values, whereClause, whereArgs);

        }
        // ST:createUpdateForRelations:finish
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	/*
     * private update function that updates based on parameters, then notifies
     * change
     */
    private int updateAndNotify(final Uri uri, final String tableName,
            final ContentValues values, final String whereClause,
            final String[] whereArgs) {
        int count = mDB.update(tableName, values, whereClause, whereArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
