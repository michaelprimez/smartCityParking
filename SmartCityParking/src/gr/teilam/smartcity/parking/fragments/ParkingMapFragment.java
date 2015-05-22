package gr.teilam.smartcity.parking.fragments;

import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.MainActivity;
import gr.teilam.smartcity.parking.MainActivity.RequestMapUpdateHandler;
import gr.teilam.smartcity.parking.adapter.MarkerInfoWindowAdapter;
import gr.teilam.smartcity.parking.db.orm.ParkingResolver;
import gr.teilam.smartcity.parking.dialogs.MarkerHandleDialog;
import gr.teilam.smartcity.parking.dialogs.MarkerHandleDialog.MarkerHandleDialogListener;
import gr.teilam.smartcity.parking.dialogs.MarkerInfoDialog;
import gr.teilam.smartcity.parking.dialogs.MarkerInfoDialog.MarkerInfoDialogListener;
import gr.teilam.smartcity.parking.listeners.MapClickListener;
import gr.teilam.smartcity.parking.model.Parking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ParkingMapFragment extends Fragment {
	
	android.app.Fragment fr;
	private GoogleMap googleMap;
	private HashMap<Marker, Parking> parkingMarkerMap = new HashMap<Marker, Parking>();
	ParkingResolver pResolver;
	
	private RequestMapUpdateHandler requestMapUpdateHandler = new RequestMapUpdateHandler(){
		@Override
		public void updateMap() {
			setMarkers();
		}

		@Override
		public void updateParking(Parking k) {
			if(k != null){
				removeMarker(k);
				Marker marker = setMarker(k);
				parkingMarkerMap.put(marker, k);
			}
		}
	};
	
	public RequestMapUpdateHandler getRequestMapUpdateHandler(){
		return requestMapUpdateHandler;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		pResolver = new ParkingResolver(activity);
		((MainActivity)activity).setRequestMapUpdateHandler(requestMapUpdateHandler);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_parking_map, container, false);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        }else{
			fr = (android.app.Fragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
			googleMap = (GoogleMap)((MapFragment)fr).getMap();
			if (googleMap != null) {
				googleMap.setOnMapClickListener(new MapClickListener(this));
				googleMap.setMyLocationEnabled(true);
				MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(this);
				googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
				googleMap.setOnInfoWindowClickListener(markerInfoWindowAdapter);
				((MainActivity)getActivity()).getLocationChangeListener().setGoogleMap(googleMap);
	        }
        }
		
		return rootView;
	}
	
	public void setMarkers(List<Parking> parkingList){
		final List<Parking> kl = parkingList;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(kl != null && kl.size() > 0){
					for(Parking k : kl){
						ParkingMapFragment.this.setMarker(k);
					}
				}
			}
		});
	}
	
	public void setMarkers(){
		googleMap.clear();
		final List<Parking> kl = ((MainActivity)getActivity()).getRequestHandler().getParkingAdapter().getFilteredParkingList();
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(kl != null && kl.size() > 0){
					for(Parking k : kl){
						Marker marker = ParkingMapFragment.this.setMarker(k);
						if(marker != null)
							parkingMarkerMap.put(marker, k);
					}
				}
			}
		});
	}
	
	public void removeMarker(Parking r){
		if(parkingMarkerMap.containsValue(r)){
			Set<Marker> markersToDel = getMarkersByParking(parkingMarkerMap, r);
			Iterator<Marker> markerIt = markersToDel.iterator();
			while(markerIt.hasNext()){
				Marker marker = markerIt.next();
				parkingMarkerMap.remove(marker);
				marker.remove();
			}
		}
	}
	
	public Set<Marker> getMarkersByParking(Map<Marker, Parking> map, Parking value) {
	    Set<Marker> keys = new HashSet<Marker>();
	    for (Entry<Marker, Parking> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}
	
	public Parking getParkingInfo(Marker marker) {
		return parkingMarkerMap.get(marker);
	}
	
	public Marker setMarker(Parking k){
		LatLng parkingLocation = k.getLatLng();
		if(parkingLocation != null && parkingLocation.latitude > 0 && parkingLocation.longitude > 0){
			Marker marker = null;
			if(k.isIscheckIn()){
				marker = googleMap.addMarker(new MarkerOptions()
				        .position(parkingLocation)
				        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) //BitmapDescriptorFactory.fromResource(R.drawable.green_pin_marker)
				        .title(k.getTitle())
				        .snippet(k.getAddress()));
			}else{
				marker = googleMap.addMarker(new MarkerOptions()
				        .position(parkingLocation)
				        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) //BitmapDescriptorFactory.fromResource(R.drawable.green_pin_marker)
				        .title(k.getTitle())
				        .alpha(0.60F)
				        .snippet(k.getAddress()));
			}
			return marker;
		}
		return null;
	}
	
	public void setMarker(final LatLng point) {
		final MarkerInfoDialog markerInfoDialog = new MarkerInfoDialog(this.getActivity());
		markerInfoDialog.setMarkerInfoDialogListener(new MarkerInfoDialogListener() {
			@Override
			public void onSubmited() {
				String strTitle = markerInfoDialog.getTitle();
				String strState = markerInfoDialog.getState();
				String strDistrict = markerInfoDialog.getDistrict();
				String strRegion = markerInfoDialog.getRegion();
				String strAddress = markerInfoDialog.getAddress();
				String strTk = markerInfoDialog.getTK();
				String strPhone = markerInfoDialog.getPhone();
				String strFax = markerInfoDialog.getFax();
				String strEmail = markerInfoDialog.getEmail();
				String strRemarks = markerInfoDialog.getRemarks();
				if(!TextUtils.isEmpty(strTitle)){
//					Parking(int row, String code, String title, String region, 
//							String state, String district, String address, 
//							String tk, String[] phones, String fax, String email, 
//							String remarks, double latitude, double longitude, boolean isFavorite)
					Parking parking = new Parking(strTitle, strRegion, strState, strDistrict, strAddress, strTk, new String[] {strPhone}, 
							strFax, strEmail, strRemarks, point.longitude, point.latitude, false, 0);
//					Marker marker = placeMarker(parking);
					Marker marker = setMarker(parking);
					parkingMarkerMap.put(marker, parking);
					try {
						pResolver.insert(parking);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					markerInfoDialog.dismiss();
				}
			}
			
			@Override
			public void onCanceled() {
				markerInfoDialog.dismiss();
			}
		});
		markerInfoDialog.show();
	}
	
	public void showSelectionDialog(final Marker marker){
		
		final MarkerHandleDialog markerHandleDialog = new MarkerHandleDialog(this.getActivity());
		markerHandleDialog.setMarkerHandleDialogListener(new MarkerHandleDialogListener() {
			
			@SuppressWarnings("unused")
			@Override
			public void onSelectioEdit() {
				
				Parking markerInfo = parkingMarkerMap.get(marker);
				String strTitle = markerInfo.getTitle();
				String strState = markerInfo.getState();
				String strDistrict = markerInfo.getDistrict();
				String strRegion = markerInfo.getRegion();
				String strAddress = markerInfo.getAddress();
				String strTk = markerInfo.getTk();
				String strPhone = markerInfo.getPhones();
				String strFax = markerInfo.getFax();
				String strEmail = markerInfo.getEmail();
				String strRemarks = markerInfo.getRemarks();
				final LatLng point = markerInfo.getLatLng();			
				
				////////////////////////////////////////////
				final MarkerInfoDialog markerInfoDialog = new MarkerInfoDialog(ParkingMapFragment.this.getActivity());
				markerInfoDialog.setTitle(strTitle);
				markerInfoDialog.setState(strState);
				markerInfoDialog.setDistrict(strDistrict);
				markerInfoDialog.setRegion(strRegion);
				markerInfoDialog.setAddress(strAddress);
				markerInfoDialog.setTk(strTk);
				markerInfoDialog.setPhone(strPhone);
				markerInfoDialog.setFax(strFax);
				markerInfoDialog.setEmai(strEmail);
				markerInfoDialog.setRemarks(strRemarks);
				markerInfoDialog.setMarkerInfoDialogListener(new MarkerInfoDialogListener() {
					@Override
					public void onSubmited() {
						String strTitle = markerInfoDialog.getTitle();
						String strState = markerInfoDialog.getState();
						String strDistrict = markerInfoDialog.getDistrict();
						String strRegion = markerInfoDialog.getRegion();
						String strAddress = markerInfoDialog.getAddress();
						String strTk = markerInfoDialog.getTK();
						String strPhone = markerInfoDialog.getPhone();
						String strFax = markerInfoDialog.getFax();
						String strEmail = markerInfoDialog.getEmail();
						String strRemarks = markerInfoDialog.getRemarks();
						if(!TextUtils.isEmpty(strTitle)){
							long miId = parkingMarkerMap.get(marker).getKEY_ID();
							
							parkingMarkerMap.remove(marker);
							marker.remove();
//							Parking(String title, String region, 
//									String state, String district, String address, 
//									String tk, String[] phones, String fax, String email, 
//									String remarks, double latitude, double longitude, boolean isFavorite)
							Parking parking = new Parking(strTitle, strRegion, strState, strDistrict, strAddress, strTk, new String[] {strPhone}, 
									strFax, strEmail, strRemarks, point.longitude, point.latitude, false, 0);
//							Marker marker = placeMarker(parking);
							Marker marker = setMarker(parking);
							parkingMarkerMap.put(marker, parking);
							try {
								pResolver.updateParkingData(parking);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							markerInfoDialog.dismiss();
						}
					}
					
					@Override
					public void onCanceled() {
						markerInfoDialog.dismiss();
					}
				});
				markerInfoDialog.show();
				////////////////////////////////////////////
				markerHandleDialog.dismiss();
			}
			
			@Override
			public void onSelectioDelete() {
				Parking mi = parkingMarkerMap.get(marker);
				try {
					pResolver.deleteParking(mi);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				parkingMarkerMap.remove(marker);
				marker.remove();
				markerHandleDialog.dismiss();
			}
			
			@Override
			public void onSelectioCancel() {
				markerHandleDialog.dismiss();				
			}
		});
		markerHandleDialog.show();
	}
}
