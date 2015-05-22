package gr.teilam.smartcity.parking.fragments;

import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.MainActivity;
import gr.teilam.smartcity.parking.adapter.ParkingArrayAdapter.UpdatelistAdapter;
import gr.teilam.smartcity.parking.fragments.irequests.IRequestHandler;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ParkingListFragment extends ListFragment {

	private IRequestHandler requestHandler;
	
	public ParkingListFragment(){
		super();
	}
	
	public void setRequestHandler(IRequestHandler requestHandler){
		this.requestHandler = requestHandler;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		requestHandler = ((MainActivity)activity).getRequestHandler();
		if(requestHandler != null){
			requestHandler.getParkingAdapter().setUpdatelistAdapter(new UpdatelistAdapter() {
				@Override
				public void updatelistAdapter() {
					setListAdapter(ParkingListFragment.this.requestHandler.getParkingAdapter());
				}
			});
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_parking_list, container, false);
		setListAdapter(requestHandler.getParkingAdapter());
//		TextView v = (TextView) rootView.findViewById(android.R.id.empty);
		return rootView;
	}
}
