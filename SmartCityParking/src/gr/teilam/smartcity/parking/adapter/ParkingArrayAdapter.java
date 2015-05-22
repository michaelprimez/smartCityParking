package gr.teilam.smartcity.parking.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import yuku.iconcontextmenu.IconContextMenu;
import yuku.iconcontextmenu.IconContextMenu.IconContextItemSelectedListener;
import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.MainActivity;
import gr.teilam.smartcity.parking.filter.ParkingMarkerFilter;
import gr.teilam.smartcity.parking.model.Parking;
import gr.teilam.smartcity.parking.views.ParkingView;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

public class ParkingArrayAdapter extends ArrayAdapter<Parking> implements Filterable, IconContextItemSelectedListener  {

	final private List<Parking> origParkingList = new ArrayList<Parking>();
	final private List<Parking> filterParkingList;
	private WeakReference<MainActivity> refAvtivity;
	
	public ParkingArrayAdapter(Context context, int resource, int textViewResourceId, List<Parking> ParkingList, MainActivity activity) {
		super(context, resource, textViewResourceId, ParkingList);
		filterParkingList = ParkingList;
		refAvtivity = new WeakReference<MainActivity>(activity);
	}

	public List<Parking> getFilteredParkingList(){
		return filterParkingList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ParkingView parkingView = null;
		if(convertView == null)
			parkingView = (ParkingView) View.inflate(getContext(), R.layout.view_parking, null);
		else
			parkingView = (ParkingView) convertView;
		parkingView.setParking(filterParkingList.get(position));
		final ParkingView kV = parkingView;
		((ImageView)parkingView.findViewById(R.id.ImgBt_Checkin_ParkingView)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refAvtivity.get().favoriteButtonClicked(kV);
			}
		});
		final Parking parking = parkingView.getParking();
		parkingView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IconContextMenu cm = new IconContextMenu(refAvtivity.get(), R.menu.list_item_actions);
				for (String strPhone : parking.getPhonesTable()) {
					MenuItem mIt = cm.getMenu().add(strPhone);
					mIt.setIcon(R.drawable.ic_action_call);
				}
				cm.setOnIconContextItemSelectedListener(ParkingArrayAdapter.this);
				cm.setInfo(parking);
				cm.show();
			}
		});
	    return parkingView;
	}
	
	@Override
	public int getCount() {
		return filterParkingList.size();
	}
	
	@Override
	public void addAll(Collection<? extends Parking> collection) {
		if(collection != null && collection.size() > 0){
			origParkingList.addAll(collection);
		}
		super.addAll(collection);
	}
	
	public List<Parking> getFilterParkingList() {
		return filterParkingList;
	}
	
	@Override
	public Filter getFilter() {
		return new Filter(){
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults results = new FilterResults();
				Locale local = new Locale("gr", "GR");
				if(TextUtils.isEmpty(constraint)){
					synchronized (this)
	                {
						results.values = new ArrayList<Parking>(origParkingList);
						results.count = origParkingList.size();
	                }
				}else{
		            final ArrayList<Parking> nlist = new ArrayList<Parking>();
					for(Parking k : origParkingList){
						if(k.getRegion().toUpperCase(local).startsWith(constraint.toString().toUpperCase(local)) ||
						   k.getState().toUpperCase(local).contains(constraint.toString().toUpperCase(local))    ||
						   k.getDistrict().toUpperCase(local).contains(constraint.toString().toUpperCase(local)) ||
						   k.getAddress().toUpperCase(local).contains(constraint.toString().toUpperCase(local))  ||
						   k.getTitle().toUpperCase(local).contains(constraint.toString().toUpperCase(local))    ||
						   k.getEmail().toUpperCase(local).contains(constraint.toString().toUpperCase(local))    ||
						   k.getPhones().toUpperCase(local).contains(constraint.toString().toUpperCase(local))   ||
						   k.getTk().toUpperCase(local).contains(constraint.toString().toUpperCase(local))       ||
						   k.getRemarks().toUpperCase(local).contains(constraint.toString().toUpperCase(local))){
							nlist.add(k);
						}
					}
					synchronized(this)
	                {
						results.values = nlist;
						results.count = nlist.size();
	                }
				}
				
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				synchronized (this)
                {
				    filterParkingList.clear();
				    filterParkingList.addAll((List<Parking>) results.values);
				    ParkingArrayAdapter.this.notifyDataSetChanged();
                }
				if(updateAdpater != null)
			    	updateAdpater.updatelistAdapter();
				if(refAvtivity != null && (refAvtivity.get() instanceof MainActivity))
					refAvtivity.get().getRequestMapUpdateHandler().updateMap();
			}
			
		};
	}
	
	public ParkingMarkerFilter getPointFilter(){
		return new ParkingMarkerFilter() {
			@Override
			protected FilterResults performFiltering(Location constraint, double radius) {
				final FilterResults results = new FilterResults();
				if(constraint == null){
					synchronized (this)
	                {
						results.values = new ArrayList<Parking>(origParkingList);
						results.count = origParkingList.size();
	                }
				}else{
		            final ArrayList<Parking> nlist = new ArrayList<Parking>();
					for(Parking k : origParkingList){
						Location markerInfoLocation = new Location("GPS");
						markerInfoLocation.setLatitude(k.getLatLng().latitude);
						markerInfoLocation.setLongitude(k.getLatLng().longitude);
						float distance = markerInfoLocation.distanceTo(constraint);
						distance = Math.abs(distance);
						if(distance < radius){
							nlist.add(k);
						}
					}
					synchronized(this)
	                {
						results.values = nlist;
						results.count = nlist.size();
	                }
				}
				
				return results;
			}
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(Location constraint, FilterResults results) {
				synchronized (this)
                {
				    filterParkingList.clear();
				    filterParkingList.addAll((List<Parking>) results.values);
				    ParkingArrayAdapter.this.notifyDataSetChanged();
                }
				if(updateAdpater != null)
			    	updateAdpater.updatelistAdapter();
				if(refAvtivity != null && (refAvtivity.get() instanceof MainActivity))
					refAvtivity.get().getRequestMapUpdateHandler().updateMap();
			}
		};
	}
	
	public static interface UpdatelistAdapter{
		public void updatelistAdapter();
	};
		
	private UpdatelistAdapter updateAdpater;
	
	public void setUpdatelistAdapter(UpdatelistAdapter updateAdpater){
		this.updateAdpater = updateAdpater;
	}

	@Override
	public void onIconContextItemSelected(MenuItem item, Object info) {
		
		switch(item.getItemId()){
		case R.id.Itm_SendEmail_MenuActions:
			sendEmail(((Parking)info).getEmail());
			break;
		default:
			makeCall(item.getTitle().toString());
			break;
		}
	}
	
	private void sendEmail(String strEmail){

	    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
	    emailIntent.setData(Uri.parse("mailto:" + strEmail.trim())); 
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
	    emailIntent.putExtra(Intent.EXTRA_TEXT, "Add body");

	    try {
	    	refAvtivity.get().startActivity(Intent.createChooser(emailIntent, "Send email using..."));
	    } catch (android.content.ActivityNotFoundException ex) { }
	}
	
	private void makeCall(String strPhone){
		
		String strUrl = "tel:" + strPhone.trim();
	    
	    try {
	    	refAvtivity.get().startActivity(Intent.createChooser(new Intent(Intent.ACTION_DIAL, Uri.parse(strUrl)), "Phone call using..."));
	    } catch (Exception ex) {
	    	Log.e("ParkingArrayAdapter", ex.getMessage());
	    }
	}
}
