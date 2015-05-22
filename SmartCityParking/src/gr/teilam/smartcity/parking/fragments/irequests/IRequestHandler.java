package gr.teilam.smartcity.parking.fragments.irequests;

import gr.teilam.smartcity.parking.adapter.ParkingArrayAdapter;
import gr.teilam.smartcity.parking.adapter.ParkingFavAdapter;

public interface IRequestHandler {
	public ParkingArrayAdapter getParkingAdapter();
	public ParkingFavAdapter getFavParkingAdapter();
}
