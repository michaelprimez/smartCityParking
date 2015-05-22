package gr.teilam.smartcity.parking.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import gr.teilam.smartcity.parking.db.orm.ParkingCreator;

public class Parking implements Parcelable {
	public final long KEY_ID;
	public String title;
	public String region;
	public String state;
	public String district;
	public String address;
	public String tk;
	public String[] phones;
	public String fax;
	public String email;
	public String remarks;
	public LatLng latLng;
	public boolean ischeckIn;
	public int checkInCount;
	
	/**
	 * Constructor WITHOUT _id, this creates a new object for insertion into the
	 * ContentProvider
	 * 
	 * @param title
	 * @param region
	 * @param state
	 * @param district
	 * @param address
	 * @param tk
	 * @param phones
	 * @param fax
	 * @param email
	 * @param remarks
	 * @param latitude
	 * @param longitude
	 * @param isFavorite
	 * @param checkInCount
	 */
	public Parking(String title, String region, 
			   String state, String district, String address, 
			   String tk, String[] phones, String fax, String email, 
			   String remarks, double latitude, double longitude, boolean ischeckIn, int checkInCount) {
		super();
		this.KEY_ID = -1;
		this.title = title;
		this.region = region;
		this.state = state;
		this.district = district;
		this.address = address;
		this.tk = tk;
		
		this.phones = new String[phones.length];
		for(int i = 0; i < phones.length; i++)
			this.phones[i] = phones[i];
		
		this.fax = fax;
		this.email = email;
		this.remarks = remarks;
			try{
				latLng = new LatLng(latitude, longitude);
			}catch(NumberFormatException nfe){}
		this.ischeckIn = ischeckIn;
		this.checkInCount = checkInCount;
	}
	
	/**
	 * Constructor WITH _id, this creates a new object for use when pulling
	 * already existing object's information from the ContentProvider
	 * 
	 * @param KEY_ID
	 * @param title
	 * @param region
	 * @param state
	 * @param district
	 * @param address
	 * @param tk
	 * @param phones
	 * @param fax
	 * @param email
	 * @param remarks
	 * @param latitude
	 * @param longitude
	 * @param isFavorite
	 * @param checkInCount
	 */
	public Parking(long KEY_ID, String title, String region, 
			   String state, String district, String address, 
			   String tk, String[] phones, String fax, String email, 
			   String remarks, double latitude, double longitude, boolean ischeckIn, int checkInCount) {
		super();
		this.KEY_ID = KEY_ID;
		this.title = title;
		this.region = region;
		this.state = state;
		this.district = district;
		this.address = address;
		this.tk = tk;
		
		this.phones = new String[phones.length];
		for(int i = 0; i < phones.length; i++)
			this.phones[i] = phones[i];
		
		this.fax = fax;
		this.email = email;
		this.remarks = remarks;
			try{
				latLng = new LatLng(latitude, longitude);
			}catch(NumberFormatException nfe){}
		this.ischeckIn = ischeckIn;
		this.checkInCount = checkInCount;
	}
	
	
	public long getKEY_ID() {
		return KEY_ID;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTk() {
		return tk;
	}
	public void setTk(String tk) {
		this.tk = tk;
	}
	public String getPhones() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < phones.length; i++)
			sb.append(phones[i] + " ");
		return sb.toString();
	}
	public String[] getPhonesTable() {
		return phones;
	}
	public void setPhones(String[] phones) {
		this.phones = phones;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public LatLng getLatLng() {
		return latLng;
	}
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
	
	public boolean isIscheckIn() {
		return ischeckIn;
	}

	public void setIscheckIn(boolean ischeckIn) {
		this.ischeckIn = ischeckIn;
	}

	public int getCheckInCount() {
		return checkInCount;
	}

	public void setCheckInCount(int checkInCount) {
		this.checkInCount = checkInCount;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(!(o instanceof Parking))
			return false;
		
		Parking otherParking = (Parking)o;
		return this.title.equals(otherParking.title) &&
			   this.region.equals(otherParking.region) &&
			   this.state.equals(otherParking.state) &&
			   this.district.equals(otherParking.district) &&
			   this.address.equals(otherParking.address) &&
			   this.tk.equals(otherParking.tk) &&
			   this.email.equals(otherParking.email);
	}
	
	@Override
	public int hashCode() {
		return this.title.hashCode() +
			   this.region.hashCode() +
			   this.state.hashCode() +
			   this.district.hashCode() +
			   this.address.hashCode() +
			   this.tk.hashCode() +
			   this.email.hashCode();
	}

	/**
	 * Override of the toString() method, for testing/logging
	 */
	@Override
	public String toString() {
		return " KEY_ID: " + KEY_ID +
			   " title: " + title + 
			   " region: " + region +
			   " state: " + state +
			   " district: " + district +
			   " address: " + address +
			   " tk: " + tk +
			   " email: " + email + 
			   " checkInCount: " + checkInCount;
	}
	
	/**
	 * Helper Method that allows easy conversion of object's data into an
	 * appropriate ContentValues
	 * 
	 * @return contentValues A new ContentValues object
	 */
	public ContentValues getCV() {
		return ParkingCreator.getCVfromParking(this);
	}

	/**
	 * Clone this object into a new StoryData
	 */
	public Parking clone() {
		return new Parking(title, region, state, district,
						   address, tk, phones, fax, email, remarks,
						   latLng.latitude, latLng.longitude, ischeckIn, checkInCount);
	}

	// these are for parcelable interface
	@Override
	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	public int describeContents() {
		return 0;
	}

	@Override
	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(KEY_ID);
		dest.writeString(title);
		dest.writeString(region);
		dest.writeString(state);
		dest.writeString(district);
		dest.writeString(address);
		dest.writeString(tk);
		dest.writeStringArray(phones);
		dest.writeString(fax);
		dest.writeString(email);
		dest.writeString(remarks);
		dest.writeDouble(latLng.latitude);
		dest.writeDouble(latLng.longitude);
		dest.writeBooleanArray(new boolean[]{ischeckIn});
		dest.writeInt(checkInCount);
	}

	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	public static final Parcelable.Creator<Parking> CREATOR = new Parcelable.Creator<Parking>() {
		public Parking createFromParcel(Parcel in) {
			return new Parking(in);
		}

		public Parking[] newArray(int size) {
			return new Parking[size];
		}
	};

	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	private Parking(Parcel in) {
		KEY_ID = in.readLong();
		title = in.readString();
		region = in.readString();
		state = in.readString();
		district = in.readString();
		address = in.readString();
		tk = in.readString();
		in.readStringArray(phones);
		fax = in.readString();
		email = in.readString();
		remarks = in.readString();
		double lat = in.readDouble();
		double lon = in.readDouble();
		latLng = new LatLng(lat, lon);
		boolean[] ifa = new boolean[1];
		in.readBooleanArray(ifa);
		ischeckIn = ifa[0];
		checkInCount = in.readInt();
	}
}
