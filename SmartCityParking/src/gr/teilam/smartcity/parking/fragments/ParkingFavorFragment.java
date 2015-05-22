package gr.teilam.smartcity.parking.fragments;

import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.MainActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ParkingFavorFragment extends ListFragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setListAdapter(((MainActivity)activity).getParkingFavAdapter());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_parking_fav, container, false);
		return rootView;
	}
}
