package gr.teilam.smartcity.parking.db.orm;

import gr.teilam.smartcity.parking.db.orm.provider.ParkingSchema;
import gr.teilam.smartcity.parking.model.Parking;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public class ParkingResolver {
	private ContentResolver cr;

	private Uri parkingURI = ParkingSchema.Parking.CONTENT_URI;
	
	/**
	 * Constructor
	 * 
	 * @param activity
	 *            The Activity to get the ContentResolver from.
	 */
	public ParkingResolver(Context activity) {
		cr = activity.getContentResolver();
	}

	/**
	 * ApplyBatch, simple pass-through to the ContentResolver implementation.
	 * 
	 * @param operations
	 * @return array of ContentProviderResult
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	public ContentProviderResult[] applyBatch(
			final ArrayList<ContentProviderOperation> operations)
			throws RemoteException, OperationApplicationException {
		return cr.applyBatch(ParkingSchema.AUTHORITY, operations);
	}

	/*
	 * Bulk Insert for each ORM Data Type
	 */

	/**
	 * Insert a group of Parking all at once. Mainly useful for use on
	 * installation/first boot of an application. Allowing setup of the Database
	 * into a 'start state'
	 * 
	 * @param data
	 *            what is to be inserted into the ContentProvider
	 * @return number of rows inserted
	 * @throws RemoteException
	 */
	public int bulkInsertParking(final ArrayList<Parking> data) throws RemoteException {
		ContentValues[] values = new ContentValues[data.size()];
		int index = 0;
		for (Parking k : data) {
			values[index] = k.getCV();
			++index;
		}
		return cr.bulkInsert(parkingURI, values);
	}

	/*
	 * Delete for each ORM Data Type
	 */
	/**
	 * Delete all Parking(s) from the ContentProvider, that match the
	 * selectionArgs
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return number of StoryData rows deleted
	 * @throws RemoteException
	 */
	public int deleteParking(final String selection, final String[] selectionArgs) throws RemoteException {
		return cr.delete(parkingURI, selection, selectionArgs);
	}

	public int deleteParking(final Parking parkingObject) throws RemoteException {
		ContentValues tempCV = parkingObject.getCV();
		return cr.delete(parkingURI, ParkingSchema.Parking.Cols.ID + "=?", new String[] {String.valueOf(parkingObject.getKEY_ID())});
	}
	
	/**
	 * Get (MIME) Type for a URI
	 * 
	 * @param uri
	 * @return MIME TYPE as a String
	 * @throws RemoteException
	 */
	public String getType(Uri uri) throws RemoteException {
		return cr.getType(uri);
	}

	/*
	 * Insert for each ORM Data Type
	 */

	/**
	 * Insert a new Parking object into the ContentProvider
	 * 
	 * @param storyObject
	 *            object to be inserted
	 * @return URI of inserted StoryData in the ContentProvider
	 * @throws RemoteException
	 */
	public Uri insert(final Parking parkingObject) throws RemoteException {
		ContentValues tempCV = parkingObject.getCV();
		tempCV.remove(ParkingSchema.Parking.Cols.ID);
		return cr.insert(parkingURI, tempCV);
	}

	/**
	 * Access files from the Application's Assets, getting a AssetFileDescriptor
	 * 
	 * @param uri
	 * @param mode
	 * @return AssetFileDescriptor
	 * @throws RemoteException
	 * @throws FileNotFoundException
	 */
	// public AssetFileDescriptor openAssetFileDescriptor(final Uri uri,
	// final String mode) throws RemoteException, FileNotFoundException {
	// return cr.openAssetFileDescriptor(uri, mode);
	// }

	/**
	 * Access files from the content provider, getting a AssetFileDescriptor
	 * 
	 * @param uri
	 * @param mode
	 * @return
	 * @throws RemoteException
	 * @throws FileNotFoundException
	 */
	public ParcelFileDescriptor openFileDescriptor(final Uri uri,
			final String mode) throws RemoteException, FileNotFoundException {
		return cr.openFileDescriptor(uri, mode);
	}

	/*
	 * Query for each ORM Data Type
	 */

	/**
	 * Query for each StoryData, Similar to standard Content Provider query,
	 * just different return type
	 * 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return an ArrayList of StoryData objects
	 * @throws RemoteException
	 */
	public ArrayList<Parking> queryParkingData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the C.P.
		Cursor result = cr.query(parkingURI, projection, selection, selectionArgs, sortOrder);
		// make return object
		ArrayList<Parking> rValue = new ArrayList<Parking>();
		// convert cursor to reutrn object
		rValue.addAll(ParkingCreator.getParkingDataArrayListFromCursor(result));
		if(result != null)
			result.close();
		// return 'return object'
		return rValue;
	}

	/*
	 * Update for each ORM Data Type
	 */

	/**
	 * do an Update for a StoryData, same input as standard Content Provider
	 * update
	 * 
	 * @param values
	 * @param selection
	 * @param selectionArgs
	 * @return number of rows changed
	 * @throws RemoteException
	 */
	public int updateParkingData(final Parking values, final String selection, final String[] selectionArgs) throws RemoteException {
		return cr.update(parkingURI, values.getCV(), selection, selectionArgs);
	}

	public int updateParkingData(final Parking parkingObj) throws RemoteException {
		return cr.update(parkingURI, parkingObj.getCV(), ParkingSchema.Parking.Cols.ID + "=?", new String[] {String.valueOf(parkingObj.getKEY_ID())});
	}
	
	/*
	 * Sample extensions of above for customized additional methods for classes
	 * that extend this one
	 */

	/**
	 * Get all the Parking objects current stored in the Content Provider
	 * 
	 * @return an ArrayList containing all the Parking objects
	 * @throws RemoteException
	 */
	public ArrayList<Parking> getAllParkingData() throws RemoteException {
		return queryParkingData(null, null, null, null);
	}

	//select * from parking_table where checkin_count > 0  order by checkin_count desc limit 5
	public ArrayList<Parking> getMostCheckInParkingData() throws RemoteException {
		return queryParkingData(null, ParkingSchema.Parking.Cols.CHECKIN_COUNT + " > ?", new String[] {"0"}, 
				ParkingSchema.Parking.Cols.CHECKIN_COUNT + " desc limit 5");
	}
	
	/**
	 * Get a Parking from the data stored at the given rowID
	 * 
	 * @param rowID
	 * @return ParkingData at the given rowID
	 * @throws RemoteException
	 */
	public Parking getParkingDataViaRowID(final long rowID) throws RemoteException {
		String[] selectionArgs = { String.valueOf(rowID) };
		ArrayList<Parking> results = queryParkingData(null, ParkingSchema.Parking.Cols.ID + "= ?", selectionArgs, null);
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Delete All rows, from AllParking table, that have the given rowID. (Should
	 * only be 1 row, but Content Providers/SQLite3 deletes all rows with
	 * provided rowID)
	 * 
	 * @param rowID
	 * @return number of rows deleted
	 * @throws RemoteException
	 */
	public int deleteAllParkingWithRowID(long rowID) throws RemoteException {
		String[] args = { String.valueOf(rowID) };
		return deleteParking(ParkingSchema.Parking.Cols.ID + " = ? ", args);
	}

	/**
	 * Updates all StoryData stored with the provided StoryData's 'KEY_ID'
	 * (should only be 1 row of data in the content provider, but content
	 * provider implementation will update EVERY row that matches.)
	 * 
	 * @param data
	 * @return number of rows altered
	 * @throws RemoteException
	 */
	public int updateParkingWithID(Parking data) throws RemoteException {
		String selection = "_id = ?";
		String[] selectionArgs = { String.valueOf(data.KEY_ID) };
		return updateParkingData(data, selection, selectionArgs);
	}
}
