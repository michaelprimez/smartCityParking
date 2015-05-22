package gr.teilam.smartcity.parking.loaders;

import gr.teilam.smartcity.parking.fragments.ParkingMapFragment;
import gr.teilam.smartcity.parking.model.Parking;

import java.lang.ref.WeakReference;

import android.os.AsyncTask;

public class UpdateMapAsyncTask extends AsyncTask<Parking, Integer, String> {

	WeakReference<ParkingMapFragment> refFragment;
	
	public UpdateMapAsyncTask(ParkingMapFragment parkingMapFragment){
		refFragment = new WeakReference<ParkingMapFragment>(parkingMapFragment);
	}
	
	@Override
	protected String doInBackground(Parking... params) {
		if(refFragment != null && refFragment.get() != null && (refFragment.get() instanceof ParkingMapFragment)){
			if(params != null && params.length > 0){
				for(Parking k : params){
					refFragment.get().setMarker(k);
				}
			}
		}
		return null;
	}

}
