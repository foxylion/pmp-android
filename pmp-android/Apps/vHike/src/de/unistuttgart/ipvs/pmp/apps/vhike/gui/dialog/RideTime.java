package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RideTime extends Dialog {

	private Context mContext;
	private Button btn_back;
	private Button btn_apply;

	public RideTime(Context context) {
		super(context);
		mContext = context;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ride_time);
		setTitle("Pick time");

		registerButtons();
	}

	private void registerButtons() {
		btn_back = (Button) findViewById(R.id.dialog_time_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
				vhikeDialogs.getInstance().getRideDate(mContext).show();
			}
		});

		btn_apply = (Button) findViewById(R.id.dialog_time_apply);
		btn_apply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * TODO: apply picked date and time
				 */
				cancel();
			}
		});
	}

}
