package gr.teilam.smartcity.parking.listeners;

import java.lang.ref.WeakReference;

import gr.teilam.smartcity.parking.MainActivity;
import gr.teilam.smartcity.parking.filter.ParkingMarkerFilter;
import gr.teilam.smartcity.parking.filter.ParkingMarkerFilter.FilterListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationChangeListener implements LocationListener, FilterListener {

	private final WeakReference<Activity> mWrActivity;
	private WeakReference<GoogleMap> mWrGoogleMap;
	private Circle circle;
	private boolean isShowNearMeChange;
	private final ParkingMarkerFilter pointFilter; 
	
	public LocationChangeListener(Activity activity, GoogleMap googleMap){
		this.mWrActivity = new WeakReference<Activity>(activity);
		if(googleMap != null)
			this.mWrGoogleMap = new WeakReference<GoogleMap>(googleMap);
		
		pointFilter = ((MainActivity)mWrActivity.get()).getParkingAdapter().getPointFilter();
	}
	
	public void setGoogleMap(GoogleMap googleMap){
		if(googleMap != null)
			this.mWrGoogleMap = new WeakReference<GoogleMap>(googleMap);
		
	}
	
	@Override
	public void onLocationChanged(Location location) {	

		if(location != null){
			if(mWrActivity != null && mWrActivity.get() != null && mWrActivity.get() instanceof MainActivity){
				((MainActivity)mWrActivity.get()).setLocation(location);
			}
			if(mWrGoogleMap != null && mWrGoogleMap.get() != null){
				float zoom = mWrGoogleMap.get().getCameraPosition().zoom;
				// Move the camera instantly with a zoom of 15.
				mWrGoogleMap.get().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
			    // Zoom in, animating the camera.
				mWrGoogleMap.get().animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
				
				if(((MainActivity)mWrActivity.get()).getShowParkingNearMe()){
					pointFilter.filter(location, ((MainActivity)mWrActivity.get()).getRadius(), this);
				}else if(isShowNearMeChange != ((MainActivity)mWrActivity.get()).getShowParkingNearMe()){
					pointFilter.filter(null, 0, this);
				}else{
					onFilterComplete(location);
				}
				isShowNearMeChange = ((MainActivity)mWrActivity.get()).getShowParkingNearMe();
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onFilterComplete(Location location) {
		if(location != null){
			if(circle != null){
				circle.remove();
			}
			circle = mWrGoogleMap.get().addCircle(new CircleOptions()
	        .center(new LatLng(location.getLatitude(), location.getLongitude()))
	        .radius(((MainActivity)mWrActivity.get()).getRadius())
	        .strokeColor(0x000000ff)
	        .fillColor(0x1B0000ff));
		}
	}

}
