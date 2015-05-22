package gr.teilam.smartcity.parking.db.orm;

import gr.teilam.smartcity.parking.db.orm.provider.ParkingSchema;
import gr.teilam.smartcity.parking.model.Parking;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

public class ParkingCreator {
	/*
	 * Create a ContentValues from a provided Parking.
	 * 
	 * @param data
	 *            Parking to be converted.
	 * @return ContentValues that is created from the Parking object
	 */
	public static ContentValues getCVfromParking(final Parking data) {
		ContentValues rValue = new ContentValues();
		rValue.put(ParkingSchema.Parking.Cols.TITLE, data.getTitle());
		rValue.put(ParkingSchema.Parking.Cols.REGION, data.getRegion());
		rValue.put(ParkingSchema.Parking.Cols.STATE, data.getState());
		rValue.put(ParkingSchema.Parking.Cols.DISTRICT, data.getDistrict());
		rValue.put(ParkingSchema.Parking.Cols.ADDRESS, data.getAddress());
		rValue.put(ParkingSchema.Parking.Cols.TK, data.getTk());
		rValue.put(ParkingSchema.Parking.Cols.PHONES, data.getPhones());
		rValue.put(ParkingSchema.Parking.Cols.FAX, data.getFax());
		rValue.put(ParkingSchema.Parking.Cols.EMAIL, data.getEmail());
		rValue.put(ParkingSchema.Parking.Cols.REMARKS, data.getRemarks());
		rValue.put(ParkingSchema.Parking.Cols.LATITUDE, data.getLatLng().latitude);
		rValue.put(ParkingSchema.Parking.Cols.LONGITUDE, data.getLatLng().longitude);
		rValue.put(ParkingSchema.Parking.Cols.IS_CHECKIN, data.isIscheckIn());
		rValue.put(ParkingSchema.Parking.Cols.CHECKIN_COUNT, data.getCheckInCount());
		return rValue;
	}

	/**
	 * Get all of the StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor to get Parking(s) of.
	 * @return ArrayList<StoryData\> The set of Parking
	 */
	public static ArrayList<Parking> getParkingDataArrayListFromCursor(Cursor cursor) {
		ArrayList<Parking> rValue = new ArrayList<Parking>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getParkingDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;
	}

	/**
	 * Get the first StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return StoryData object
	 */
	public static Parking getParkingDataFromCursor(Cursor cursor) {

		long rowID = cursor.getLong(cursor.getColumnIndex(ParkingSchema.Parking.Cols.ID));
		String title = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.TITLE));
		String region = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.REGION));
		String state = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.STATE));
		String district = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.DISTRICT));
		String address = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.ADDRESS));
		String tk = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.TK));
		String strPhones = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.PHONES));
		String[] phones = strPhones.split(" ");
		String fax = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.FAX));
		String email = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.EMAIL));
		String remarks = cursor.getString(cursor.getColumnIndex(ParkingSchema.Parking.Cols.REMARKS));
		double latitude = cursor.getDouble(cursor.getColumnIndex(ParkingSchema.Parking.Cols.LATITUDE));
		double longitude = cursor.getDouble(cursor.getColumnIndex(ParkingSchema.Parking.Cols.LONGITUDE));
		boolean isFavorite = (cursor.getInt(cursor.getColumnIndex(ParkingSchema.Parking.Cols.IS_CHECKIN)) > 0) ? true : false;
		int checkInCount = cursor.getInt(cursor.getColumnIndex(ParkingSchema.Parking.Cols.CHECKIN_COUNT));

		// construct the returned object
		Parking rValue = new Parking(rowID, title, region, state, district, address, tk, 
				phones, fax, email, remarks, latitude, longitude, isFavorite, checkInCount);
		return rValue;
	}
}
