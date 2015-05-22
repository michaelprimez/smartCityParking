package gr.teilam.smartcity.parking.listeners;

import gr.teilam.smartcity.parking.fragments.ParkingMapFragment;

import java.lang.ref.WeakReference;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

public class MapClickListener implements OnMapClickListener {

	final private WeakReference<Fragment> fragmentRef;
	
	public MapClickListener(Fragment fragment){
		fragmentRef = new WeakReference<Fragment>(fragment);
	}
	
	@Override
	public void onMapClick(LatLng point) {
		if(fragmentRef.get() != null && fragmentRef.get() instanceof ParkingMapFragment){
			((ParkingMapFragment)fragmentRef.get()).setMarker(point);
		}
	}

}
