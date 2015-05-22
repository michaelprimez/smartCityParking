package gr.teilam.smartcity.parking;

import gr.teilam.smartcity.parking.R;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class InfoActivity extends ActionBarActivity implements LocationListener {

	CheckBox mChkBxSendCords;
	EditText mEdtTxtMessageBody;
	private LocationManager locationManager;
	private Location location;
	private String provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		mChkBxSendCords = (CheckBox) findViewById(R.id.ChkBx_Cordinates_InfoActivity);
		mEdtTxtMessageBody = (EditText) findViewById(R.id.EdtTxt_MessageBody_InfoActivity);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locationManager.getBestProvider(criteria, true);
		
		if(provider != null){
			location = locationManager.getLastKnownLocation(provider);
		
			locationManager.requestLocationUpdates(provider, 2500, Float.parseFloat("2.0"), this);
			this.location = locationManager.getLastKnownLocation(provider);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
	
	public void sendEmailOnClick(View v){
		StringBuilder strMessageBody = new StringBuilder();
		if(mChkBxSendCords.isChecked() && location != null){
			strMessageBody.append(getResources().getString(R.string.longitude)).append(location.getLongitude()).append("\n");
			strMessageBody.append(getResources().getString(R.string.latitude)).append(location.getLatitude()).append("\n");;
		}
		strMessageBody.append(mEdtTxtMessageBody.getText().toString());
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
	    emailIntent.setData(Uri.parse("mailto:" + "mailAddress@gmail.com"));
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject));
	    
	    emailIntent.putExtra(Intent.EXTRA_TEXT, strMessageBody.toString());

	    try {
	    	startActivity(Intent.createChooser(emailIntent, "Send email using..."));
	    } catch (android.content.ActivityNotFoundException ex) { }
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
