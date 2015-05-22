package gr.teilam.smartcity.parking.dialogs;

import gr.teilam.smartcity.parking.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

enum UserSelection{USER_CANCEL, USER_EDIT, USER_DELETE};

public class MarkerHandleDialog extends Dialog implements OnClickListener {

	Button mBtnCancel;
	Button mBtnDelete;
	Button mBtnEdit;
	MarkerHandleDialogListener dialogListener;
	UserSelection userSelection;
	
	public MarkerHandleDialog(Context context) {
		super(context);
		setContentView(R.layout.marker_selection_dialog);
		setTitle(R.string.select);
		mBtnCancel = (Button) findViewById(R.id.Btn_Cancel_MarkerSelectionDialog);
		mBtnDelete = (Button) findViewById(R.id.Btn_Delete_MarkerSelectionDialog);
		mBtnEdit = (Button) findViewById(R.id.Btn_Edit_MarkerSelectionDialog);
		mBtnCancel.setOnClickListener(this);
		mBtnDelete.setOnClickListener(this);
		mBtnEdit.setOnClickListener(this);
		userSelection = UserSelection.USER_CANCEL;
	}
	
	public static interface MarkerHandleDialogListener
	{
		public void onSelectioCancel();
	    public void onSelectioDelete();
	    public void onSelectioEdit();
	}
	
	public void setMarkerHandleDialogListener(MarkerHandleDialogListener dialogListener){
		this.dialogListener = dialogListener;
	}

	@Override
	public void onClick(View v) {
		if(v == mBtnCancel){
			userSelection = UserSelection.USER_CANCEL;
			dialogListener.onSelectioCancel();
		}else if(v == mBtnDelete){
			userSelection = UserSelection.USER_DELETE;
			dialogListener.onSelectioDelete();
		}else if(v == mBtnEdit){
			userSelection = UserSelection.USER_EDIT;
			dialogListener.onSelectioEdit();
		}
	}

	public UserSelection getUserSelection(){
		return userSelection;
	}
}
