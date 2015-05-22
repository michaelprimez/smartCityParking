package gr.teilam.smartcity.parking.adapter;

import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.fragments.ParkingMapFragment;
import gr.teilam.smartcity.parking.model.Parking;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindowAdapter implements InfoWindowAdapter, OnInfoWindowClickListener {

	final private WeakReference<Fragment> refFragment;
	private final View markerContents;
	
	public MarkerInfoWindowAdapter(Fragment fragment){
		refFragment = new WeakReference<Fragment>(fragment);
		markerContents = refFragment.get().getActivity().getLayoutInflater().inflate(
				R.layout.marker_content, 
				(ViewGroup)refFragment.get().getActivity().findViewById(R.layout.fragment_parking_map));
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		if(refFragment != null && refFragment.get() instanceof ParkingMapFragment){
			Parking parking = ((ParkingMapFragment)refFragment.get()).getParkingInfo(marker);
		    String title = parking.getTitle();
		    
		    TextView txtTitle = ((TextView) markerContents.findViewById(R.id.TxtVw_Title_MarkerContent));
		    if (title != null) {
		    	// Spannable string allows us to edit the formatting of the text.
		    	SpannableString titleText = new SpannableString(title);
		    	titleText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, titleText.length(), 0);
		    	txtTitle.setText(titleText);
	
		    }else{
		    	txtTitle.setText("");
		    }
	
		    TextView txtAddress = ((TextView) markerContents.findViewById(R.id.TxtVw_Address_MarkerContent));
		    txtAddress.setText(parking.getAddress());         
	
		    ImageButton mImgBtFavotite = (ImageButton) markerContents.findViewById(R.id.ImgBt_Favorite_MarkerContent);
		    int resId;
			if(parking.ischeckIn){
				resId = ((ParkingMapFragment)refFragment.get()).getActivity().getResources().getIdentifier("ic_launcher", "drawable", 
						((ParkingMapFragment)refFragment.get()).getActivity().getBaseContext().getPackageName());
			}else{
				resId = ((ParkingMapFragment)refFragment.get()).getActivity().getResources().getIdentifier("checkin", "drawable", 
						((ParkingMapFragment)refFragment.get()).getActivity().getBaseContext().getPackageName());
			}
			mImgBtFavotite.setImageResource(resId);
			mImgBtFavotite.refreshDrawableState();
			
			return markerContents;
		}
		return markerContents;
	}

	
	@Override
	public View getInfoWindow(Marker marker) {
		//if you return null, it will just use the default window
		return null;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
//		Parking parking = ((ParkingMapFragment)refFragment.get()).getParkingInfo(marker);
		if(refFragment != null && refFragment.get() instanceof ParkingMapFragment){
			((ParkingMapFragment)refFragment.get()).showSelectionDialog(marker);
		}
//		String label = parking.getTitle();
//		String uriBegin = "geo:" + parking.getLatLng().latitude + "," + parking.getLatLng().longitude;
//		String query = "" + parking.getLatLng().latitude + "," + parking.getLatLng().longitude + "(" + label + ")";
//		String encodedQuery = Uri.encode( query  );
//		String uriString = uriBegin + "?q=" + encodedQuery;
//		Uri uri = Uri.parse( uriString );
//		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri );
//		refFragment.get().getActivity().startActivity( intent );
		
	}

}
