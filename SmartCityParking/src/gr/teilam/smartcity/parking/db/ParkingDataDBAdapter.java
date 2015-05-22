package gr.teilam.smartcity.parking.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gr.teilam.smartcity.parking.db.orm.provider.ParkingSchema;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class ParkingDataDBAdapter {
	private static final String LOG_TAG = ParkingDataDBAdapter.class.getCanonicalName();

    private static final String DATABASE_NAME = "ParkingDatabase.db";

    // ST:databaseTableVariableDeclaration:start
    static final String DATABASE_TABLE_PARKING = ParkingSchema.Parking.TABLE_NAME;
    // ST:databaseTableVariableDeclaration:finish

    static final int DATABASE_VERSION = 1;

    // The SHORT name of each column in your table
    // ST:createShortVariables:start
    private static final String Parking_KEY_ID = ParkingSchema.Parking.Cols.ID;
    private static final String Parking_Title = ParkingSchema.Parking.Cols.TITLE;
    private static final String Parking_Region = ParkingSchema.Parking.Cols.REGION;
    private static final String Parking_State = ParkingSchema.Parking.Cols.STATE;
    private static final String Parking_District = ParkingSchema.Parking.Cols.DISTRICT;
    private static final String Parking_Address = ParkingSchema.Parking.Cols.ADDRESS;
    private static final String Parking_Tk = ParkingSchema.Parking.Cols.TK;
    private static final String Parking_Phones = ParkingSchema.Parking.Cols.PHONES;
    private static final String Parking_Fax = ParkingSchema.Parking.Cols.FAX;
    private static final String Parking_Email = ParkingSchema.Parking.Cols.EMAIL;
    private static final String Parking_Remarks = ParkingSchema.Parking.Cols.REMARKS;
    private static final String Parking_Latitude = ParkingSchema.Parking.Cols.LATITUDE;
    private static final String Parking_Longitude = ParkingSchema.Parking.Cols.LONGITUDE;
    private static final String Parking_IsCheckin = ParkingSchema.Parking.Cols.IS_CHECKIN;
    private static final String Parking_CheckInCount = ParkingSchema.Parking.Cols.CHECKIN_COUNT;

    // ST:databaseTableCreationStrings:start
    // SQL Statement to create a new database table.
    @SuppressWarnings("unused")
	private static final String DATABASE_CREATE_PARKING = "create table "
            + DATABASE_TABLE_PARKING + " (" // start table
            + Parking_KEY_ID + " integer primary key autoincrement, " // setup
                                                                    // auto-inc.
            // ST:tableCreateVariables:start
            + Parking_Title + " TEXT ,"
            + Parking_Region + " TEXT ," 
            + Parking_State + " TEXT ,"
            + Parking_District + " TEXT ,"
            + Parking_Address + " TEXT ,"
            + Parking_Tk + " TEXT ,"
            + Parking_Phones + " TEXT ,"
            + Parking_Fax + " TEXT ,"
            + Parking_Email + " TEXT ,"
            + Parking_Remarks + " TEXT ,"
            + Parking_Latitude + " REAL ,"
            + Parking_Longitude + " REAL,  "
            + Parking_IsCheckin + " INTEGER, "
            + Parking_CheckInCount + " INTEGER "
            // ST:tableCreateVariables:finish
            + " );"; // end table
    // ST:databaseTableCreationStrings:finish

    // Variable to hold the database instance.
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private myDbHelper dbHelper;
    // if the DB is in memory or to file.
    private boolean MEMORY_ONLY_DB = false;

    /**
     * constructor that accepts the context to be associated with
     * 
     * @param _context
     */
    public ParkingDataDBAdapter(Context _context) {
        Log.d(LOG_TAG, "MyDBAdapter constructor");

        context = _context;
        dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * constructor that accepts the context to be associated with, and if this
     * DB should be created in memory only(non-persistent).
     * 
     * @param _context
     */
    public ParkingDataDBAdapter(Context _context, boolean memory_only_db) {
        Log.d(LOG_TAG, "MyDBAdapter constructor w/ mem only =" + memory_only_db);

        context = _context;
        MEMORY_ONLY_DB = memory_only_db;
        if (MEMORY_ONLY_DB == true) {
            dbHelper = new myDbHelper(context, null, null, DATABASE_VERSION);
        } else {
            dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * open the DB Get Memory or File version of DB, and write/read access or
     * just read access if that is all that is possible.
     * 
     * @return this MoocDataDBAdaptor
     * @throws SQLException
     */
    public ParkingDataDBAdapter open() throws SQLException {
        Log.d(LOG_TAG, "open()");
        try {
            db = dbHelper.openDataBase();//dbHelper.getWritableDatabase();
        } catch (SQLException ex) {
            db = dbHelper.openDataBase();//dbHelper.getReadableDatabase();
        }
        return this;
    }

    /**
     * Remove a row of the DB where the rowIndex matches.
     * 
     * @param rowIndex
     *            row to remove from DB
     * @return if the row was removed
     */
    public int delete(final String table, long _id) {
        Log.d(LOG_TAG, "delete(" + _id + ") ");
        return db.delete(table, android.provider.BaseColumns._ID + " = " + _id, null);
    }

    /**
     * Delete row(s) that match the whereClause and whereArgs(if used).
     * <p>
     * the whereArgs is an String[] of values to substitute for the '?'s in the
     * whereClause
     * 
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int delete(final String table, final String whereClause,
            final String[] whereArgs) {
        Log.d(LOG_TAG, "delete(" + whereClause + ") ");
        return db.delete(table, whereClause, whereArgs);
    }

    /**
     * Query the Database with the provided specifics.
     * 
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return Cursor of results
     */
    public Cursor query(final String table, final String[] projection,
            final String selection, final String[] selectionArgs,
            final String sortOrder) {

        // TODO: Perform a query on the database with the given parameters
        //Cursor cursor = query(table, projection, selection, selectionArgs, sortOrder);
        Cursor cursor = db.query(table, projection, selection, selectionArgs, "", "", sortOrder);
        
        return cursor; // return null;
    }

    /**
     * close the DB.
     */
    public void close() {
        Log.d(LOG_TAG, "close()");
        db.close();
    }

    /**
     * Start a transaction.
     */
    public void startTransaction() {
        Log.d(LOG_TAG, "startTransaction()");
        db.beginTransaction();
    }

    /**
     * End a transaction.
     */
    public void endTransaction() {
        Log.d(LOG_TAG, "endTransaction()");
        db.endTransaction();
    }

    /**
     * Get the underlying Database.
     * 
     * @return
     */
    SQLiteDatabase getDB() {
        return db;
    }

    /**
     * Insert a ContentValues into the DB.
     * 
     * @param location
     * @return row's '_id' of the newly inserted ContentValues
     */
    public long insert(final String table, final ContentValues cv) {
        Log.d(LOG_TAG, "insert(CV)");
        return db.insert(table, null, cv);
    }

    /**
     * Update Value(s) in the DB.
     * 
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return number of rows changed.
     */
    public int update(final String table, final ContentValues values,
            final String whereClause, final String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    @Override
    /**
     * finalize operations to this DB, and close it.
     */
    protected void finalize() throws Throwable {
        try {
            db.close();
        } catch (Exception e) {
            Log.d(LOG_TAG, "exception on finalize():" + e.getMessage());
        }
        super.finalize();
    }

    /**
     * This class can support running the database in a non-persistent mode,
     * this tells you if that is happening.
     * 
     * @return boolean true/false of if this DBAdaptor is persistent or in
     *         memory only.
     */
    public boolean isMemoryOnlyDB() {
        return MEMORY_ONLY_DB;
    }

    /**
     * DB Helper Class.
     * 
     * @author mwalker
     * 
     */
    private static class myDbHelper extends SQLiteOpenHelper {

    	private SQLiteDatabase myDataBase;
    	private final String DB_PATH;
    	private final Context context;
    	
        public myDbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
            DB_PATH = context.getFilesDir().getParentFile().toString() + "/databases/";
            this.context = context;
        }

        public void createDataBase()  throws IOException {
            Log.d(LOG_TAG, "DATABASE_CREATE: version: " + DATABASE_VERSION);
            
            boolean dbExist = checkDataBase();
            if(dbExist){
        		//do nothing - database already exist
            	// ST:createTable:start
            	// db.execSQL(DATABASE_CREATE_PARKING);
                // ST:createTable:finish
        	}else{
     
        		//By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
            	this.getReadableDatabase();
     
            	try {
        			copyDataBase();
        		} catch (IOException e) {
            		throw new Error("Error copying database");
            	}
        	}
        }
        
        public SQLiteDatabase openDataBase() throws SQLException{
        	//Open the database
            String myPath = DB_PATH + DATABASE_NAME;
        	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        	return myDataBase;
        }
        
        @Override
    	public synchronized void close() {
        	if(myDataBase != null)
        		myDataBase.close();
        	super.close();
    	}
        
        @Override
        public void onCreate(SQLiteDatabase db) {
            
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            // Log version upgrade.
//            Log.w(LOG_TAG + "DBHelper", "Upgrading from version " + oldVersion
//                    + " to " + newVersion + ", which will destroy all old data");
//
//            // **** Upgrade DB ****
//            // drop old DB
//
//            // ST:dropTableIfExists:start
//            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PARKING);
//
//            // Create a new one.
//            onCreate(db);

        }
        
        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase(){
        	SQLiteDatabase checkDB = null;
        	try{
        		String myPath = DB_PATH + DATABASE_NAME;
        		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        	}catch(SQLiteException e){
        		//database does't exist yet.
        	}
     
        	if(checkDB != null){
        		checkDB.close();
        	}
        	return checkDB != null ? true : false;
        }

        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */
        private void copyDataBase() throws IOException{
     
        	//Open your local db as the input stream
        	InputStream myInput = context.getAssets().open(DATABASE_NAME);
     
        	// Path to the just created empty db
        	String outFileName = DB_PATH + DATABASE_NAME;
     
        	//Open the empty db as the output stream
        	OutputStream myOutput = new FileOutputStream(outFileName);
     
        	//transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer))>0){
        		myOutput.write(buffer, 0, length);
        	}
     
        	//Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
        }
    }
}