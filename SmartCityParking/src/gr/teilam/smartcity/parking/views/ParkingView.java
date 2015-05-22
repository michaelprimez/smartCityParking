package gr.teilam.smartcity.parking.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import gr.teilam.smartcity.parking.R;
import gr.teilam.smartcity.parking.model.Parking;

public class ParkingView extends LinearLayout {

	private Parking parking;
	private TextView mTxtVwTitle;
	private TextView mTxtVwRegion;
	private TextView mTxtVwStateDistrict;
	private TextView mTxtVwAddressTk;
	private TextView mTxtVwPhones;
	private TextView mTxtVwFax;
	private TextView mTxtVwEmail;
	private ImageButton mImgBtCheckIn;
	
	public ParkingView(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTxtVwTitle = (TextView) findViewById(R.id.TxtVw_Title_ParkingView);
		mTxtVwRegion = (TextView) findViewById(R.id.TxtVw_Region_ParkingView);
		mTxtVwStateDistrict = (TextView) findViewById(R.id.TxtVw_State_District_ParkingView);
		mTxtVwAddressTk = (TextView) findViewById(R.id.TxtVw_Address_TK_ParkingView);
		mTxtVwPhones = (TextView) findViewById(R.id.TxtVw_Phone_ParkingView);
		mTxtVwFax = (TextView) findViewById(R.id.TxtVw_Fax_ParkingView);
		mTxtVwEmail = (TextView) findViewById(R.id.TxtVw_Email_TK_ParkingView);
		mImgBtCheckIn = (ImageButton) findViewById(R.id.ImgBt_Checkin_ParkingView);
	}
	
	public Parking getParking(){
		return this.parking;
	}
	
	public void setParking(Parking parking){
		this.parking = parking;
		if(TextUtils.isEmpty(parking.getTitle())){
			mTxtVwTitle.setEnabled(false);
		}else{
			mTxtVwTitle.setText(parking.getTitle());
		}
		
		if(TextUtils.isEmpty(parking.getRegion())){
			mTxtVwRegion.setEnabled(false);
		}else{
			mTxtVwRegion.setText(parking.getRegion());
		}
		
		if(TextUtils.isEmpty(parking.getState()) && TextUtils.isEmpty(parking.getDistrict())){
			mTxtVwStateDistrict.setEnabled(false);
		}else{
			if(TextUtils.isEmpty(parking.getState()) || TextUtils.isEmpty(parking.getDistrict()))
				mTxtVwStateDistrict.setText(parking.getState() + parking.getDistrict());
			else
				mTxtVwStateDistrict.setText(parking.getState() + " - " + parking.getDistrict());
			
		}
		
		if(TextUtils.isEmpty(parking.getAddress()) && TextUtils.isEmpty(parking.getTk())){
			mTxtVwAddressTk.setEnabled(false);
		}else{
			if(TextUtils.isEmpty(parking.getAddress()) || TextUtils.isEmpty(parking.getTk()))
				mTxtVwAddressTk.setText(parking.getAddress() + "" + parking.getTk());
			else
				mTxtVwAddressTk.setText(parking.getAddress() + " - " + parking.getTk());
		}
		
		if(TextUtils.isEmpty(parking.getPhones())){
			mTxtVwPhones.setEnabled(false);
		}else{
			mTxtVwPhones.setText(parking.getPhones());
		}
		
		if(TextUtils.isEmpty(parking.getFax())){
			mTxtVwFax.setEnabled(false);
		}else{
			mTxtVwFax.setText(parking.getFax());
		}
		
		if(TextUtils.isEmpty(parking.getEmail())){
			mTxtVwEmail.setEnabled(false);
		}else{
			mTxtVwEmail.setText(parking.getEmail());
		}
		int resId;
		if(parking.isIscheckIn()){
			resId = getResources().getIdentifier("ic_launcher", "drawable", getContext().getPackageName());
		}else{
			resId = getResources().getIdentifier("checkin", "drawable", getContext().getPackageName());
		}
		mImgBtCheckIn.setImageResource(resId);
		mImgBtCheckIn.refreshDrawableState();
	}

	public void toggleCheckin(){
		int resId;
		if(parking.isIscheckIn()){
			parking.setIscheckIn(false);;
			resId = getResources().getIdentifier("checkin", "drawable", getContext().getPackageName());
		}else{
			parking.setIscheckIn(true);
			resId = getResources().getIdentifier("ic_launcher", "drawable", getContext().getPackageName());
		}
		mImgBtCheckIn.setImageResource(resId);
		mImgBtCheckIn.refreshDrawableState();
	}
}
