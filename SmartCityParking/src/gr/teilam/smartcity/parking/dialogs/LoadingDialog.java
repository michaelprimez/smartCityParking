package gr.teilam.smartcity.parking.dialogs;

import gr.teilam.smartcity.parking.R;
import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

public class LoadingDialog extends Dialog {

	ProgressBar mPrgBarLoading;
	
	public LoadingDialog(Context context) {
		super(context);
		setContentView(R.layout.custom_progress_bar);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.custom_progress_bar);
	}
	
	

}
