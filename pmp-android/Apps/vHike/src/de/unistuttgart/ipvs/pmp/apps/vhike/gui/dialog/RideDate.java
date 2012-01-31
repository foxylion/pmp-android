package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RideDate extends Dialog {

	private Context mContext;
	private Button btn_cancel;
	private Button btn_next;

	public RideDate(Context context) {
		super(context);
		mContext = context;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ride_date);
		setTitle("Pick date");

		registerButtons();
	}

	private void registerButtons() {
		btn_cancel = (Button) findViewById(R.id.dialog_date_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});

		btn_next = (Button) findViewById(R.id.dialog_date_next);
		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
				vhikeDialogs.getInstance().getRideTime(mContext).show();
			}
		});
	}

}
