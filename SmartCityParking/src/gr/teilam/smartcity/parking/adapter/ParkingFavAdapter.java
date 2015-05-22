package gr.teilam.smartcity.parking.adapter;

import java.lang.ref.WeakReference;
import java.util.List;

import yuku.iconcontextmenu.IconContextMenu;
import yuku.iconcontextmenu.IconContextMenu.IconContextItemSelectedListener;
import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.MainActivity;
import gr.teilam.smartcity.parking.model.Parking;
import gr.teilam.smartcity.parking.views.ParkingView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ParkingFavAdapter extends ArrayAdapter<Parking> implements IconContextItemSelectedListener {

	private WeakReference<MainActivity> refAvtivity;
	final private List<Parking> origParkingpList;
	
	public ParkingFavAdapter(Context context, int resource, int textViewResourceId, List<Parking> parkingList, MainActivity activity) {
		super(context, resource, textViewResourceId, parkingList);
		refAvtivity = new WeakReference<MainActivity>(activity);
		origParkingpList = parkingList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ParkingView parkingView = null;
		if(convertView == null)
			parkingView = (ParkingView) View.inflate(getContext(), R.layout.view_parking, null);
		else
			parkingView = (ParkingView) convertView;
		parkingView.setParking(origParkingpList.get(position));
		final ParkingView pV = parkingView;
		((ImageView)parkingView.findViewById(R.id.ImgBt_Checkin_ParkingView)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refAvtivity.get().favoriteButtonClicked(pV);
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
				cm.setOnIconContextItemSelectedListener(ParkingFavAdapter.this);
				cm.setInfo(parking);
				cm.show();
			}
		});
	    return parkingView;
	}
	
	@Override
	public int getCount() {
		return origParkingpList.size();
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
	    emailIntent.setData(Uri.parse("mailto:" + strEmail)); 
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
	    emailIntent.putExtra(Intent.EXTRA_TEXT, "Add body");

	    try {
	    	refAvtivity.get().startActivity(Intent.createChooser(emailIntent, "Send email using..."));
	    } catch (android.content.ActivityNotFoundException ex) { }
	}

	private void makeCall(String strPhone){
		
		String url = "tel:" + strPhone;
	    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));

	    try {
	    	refAvtivity.get().startActivity(Intent.createChooser(callIntent, "Phone call using..."));
	    } catch (android.content.ActivityNotFoundException ex) { }
	}	
}
