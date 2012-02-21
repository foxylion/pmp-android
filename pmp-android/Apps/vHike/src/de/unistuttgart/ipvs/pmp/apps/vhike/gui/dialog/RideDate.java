/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Set date of ride
 * 
 * @author Andre Nguyen
 *
 */
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
