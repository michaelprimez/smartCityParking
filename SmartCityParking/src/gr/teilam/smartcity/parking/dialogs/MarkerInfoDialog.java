package gr.teilam.smartcity.parking.dialogs;

import gr.teilam.smartcity.parking.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MarkerInfoDialog extends Dialog implements OnClickListener {

	private Button mBtnSubmit;
	private Button mBtnCancel;
	private EditText mEdtTxtTitle;
	private EditText mEdtTxtState;
	private EditText mEdtTxtDistrict;
	private EditText mEdtTxtRegion;
	private EditText mEdtTxtAddress;
	private EditText mEdtTxtTk;
	private EditText mEdtTxtPhone;
	private EditText mEdtTxtFax;
	private EditText mEdtTxtEmail;
	private EditText mEdtTxtRemarks;
	
	MarkerInfoDialogListener dialogListener;
	
	public MarkerInfoDialog(Context context) {
		super(context);
		setContentView(R.layout.marker_info_dialog);
		setTitle(R.string.add_marker);
		mBtnSubmit = (Button) findViewById(R.id.Btn_Submit_MarkerInfoDialog);
		mBtnCancel = (Button) findViewById(R.id.Btn_Cancel_MarkerInfoDialog);
		mBtnSubmit.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mEdtTxtTitle = (EditText) findViewById(R.id.EdtTxt_Title_MarkerInfoDialog);
		mEdtTxtState = (EditText) findViewById(R.id.EdtTxt_State_MarkerInfoDialog);
		mEdtTxtDistrict = (EditText) findViewById(R.id.EdtTxt_District_MarkerInfoDialog);
		mEdtTxtRegion = (EditText) findViewById(R.id.EdtTxt_Region_MarkerInfoDialog);
		mEdtTxtAddress = (EditText) findViewById(R.id.EdtTxt_Address_MarkerInfoDialog);
		mEdtTxtTk = (EditText) findViewById(R.id.EdtTxt_TK_MarkerInfoDialog);
		mEdtTxtPhone = (EditText) findViewById(R.id.EdtTxt_Phone_MarkerInfoDialog);
		mEdtTxtFax = (EditText) findViewById(R.id.EdtTxt_Fax_MarkerInfoDialog);
		mEdtTxtEmail = (EditText) findViewById(R.id.EdtTxt_Email_MarkerInfoDialog);
		mEdtTxtRemarks = (EditText) findViewById(R.id.EdtTxt_Remarks_MarkerInfoDialog);
	}
	
	public static interface MarkerInfoDialogListener
	{
	    public void onSubmited();
	    public void onCanceled();
	}

	public void setMarkerInfoDialogListener(MarkerInfoDialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	public void onClick(View v) {
		if(v == mBtnSubmit){
			dialogListener.onSubmited();
		}else if(v == mBtnCancel){
			dialogListener.onCanceled();
		}
	}
	
	public String getTitle(){
		return mEdtTxtTitle.getText().toString();
	}
	
	public String getState(){
		return mEdtTxtState.getText().toString();
	}
	
	public String getDistrict(){
		return mEdtTxtDistrict.getText().toString();
	}
	
	public String getRegion(){
		return mEdtTxtRegion.getText().toString();
	}
	
	public String getAddress(){
		return mEdtTxtAddress.getText().toString();
	}
	
	public String getTK(){
		return mEdtTxtTk.getText().toString();
	}
	
	public String getPhone(){
		return mEdtTxtPhone.getText().toString();
	}
	
	public String getFax(){
		return mEdtTxtFax.getText().toString();
	}
	
	public String getEmail(){
		return mEdtTxtEmail.getText().toString();
	}
	
	public String getRemarks(){
		return mEdtTxtRemarks.getText().toString();
	}
	
	
	public void setTitle(String strTitle){
		mEdtTxtTitle.setText(strTitle);
	}
	
	public void setState(String strState){
		mEdtTxtState.setText(strState);
	}
	
	public void setDistrict(String strRistrict){
		mEdtTxtDistrict.setText(strRistrict);
	}
	
	public void setRegion(String strRegion){
		mEdtTxtRegion.setText(strRegion);
	}
	
	public void setAddress(String strAddress){
		mEdtTxtRegion.setText(strAddress);
	}
	
	public void setTk(String strTk){
		mEdtTxtTk.setText(strTk);
	}
	
	public void setPhone(String strPhone){
		mEdtTxtPhone.setText(strPhone);
	}
	
	public void setFax(String strFax){
		mEdtTxtFax.setText(strFax);
	}
	
	public void setEmai(String strEmail){
		mEdtTxtEmail.setText(strEmail);
	}
	
	public void setRemarks(String strRemarks){
		mEdtTxtRemarks.setText(strRemarks);
	}
}
