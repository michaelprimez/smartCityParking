package gr.teilam.smartcity.parking.loaders;

import gr.teilam.smartcity.parking.MainActivity;
import gr.teilam.smartcity.parking.db.orm.ParkingResolver;
import gr.teilam.smartcity.parking.model.Parking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.RemoteException;

public class ParkingLoader extends AsyncTaskLoader<Map<String, List<Parking>>> {

	private Map<String, List<Parking>> mData;
	
	public ParkingLoader(Context context) {
		super(context);
	}

	@Override
	public Map<String, List<Parking>> loadInBackground() {
		ParkingResolver parkingResolver = new ParkingResolver(getContext());

		mData = new HashMap<String, List<Parking>>();
		
		try {
			List<Parking> allParkingData = parkingResolver.getAllParkingData();
			mData.put(MainActivity.ALL, allParkingData);

			List<Parking> mostCheckInParkings = parkingResolver.getMostCheckInParkingData();
			if(mostCheckInParkings != null && mostCheckInParkings.size() > 0) {
				mData.put(MainActivity.FAVORITES, mostCheckInParkings);
			}
		} catch (NumberFormatException | RemoteException e) {
			e.printStackTrace();
		} 
		
		return mData;
	}
	
	@Override
	public void deliverResult(Map<String, List<Parking>> data) {
		super.deliverResult(data);
		if(isReset()){
			releaseResources(data);
		}
		
		Map<String, List<Parking>> oldData = mData;
		mData = data;
		if(isStarted()){
			super.deliverResult(data);
		}
		
		if(oldData != null && oldData != data){
			releaseResources(oldData);
		}
	}
	
	@Override
	protected void onStartLoading() {
		if(mData != null){
			deliverResult(mData);
		}
		
		if(takeContentChanged() || mData == null){
			forceLoad();
		}
	}
	
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}
	
	@Override
	protected void onReset() {
		onStopLoading();
		
		if(mData != null)
			releaseResources(mData);
		mData = null;
	}
	
	@Override
	public void onCanceled(Map<String, List<Parking>> data) {
		super.onCanceled(data);
		
		releaseResources(data);
	}
	
	private void releaseResources(Map<String, List<Parking>> data){
		
	}
}
