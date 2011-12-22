/*
 * Copyright 2011 pmp-android development team
 * Project: SimpleApp
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
package de.unistuttgart.ipvs.pmp.apps.simpleapp.gui;

import de.unistuttgart.ipvs.pmp.apps.simpleapp.R;
import de.unistuttgart.ipvs.pmp.apps.simpleapp.provider.Model;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.ProgressDialog;

public class SimpleAppActivity extends Activity {

	private Handler handler;
	
	private Button buttonServiceFeautres;

	private Button wirelessRefreshButton;
	private TextView wirelessStateTextView;
	private ToggleButton wirelessToggleButton;

	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = new Handler();
		
		setContentView(R.layout.main);

		buttonServiceFeautres = (Button) findViewById(R.id.Button_ChangeServiceFeatures);

		wirelessRefreshButton = (Button) findViewById(R.id.Button_WifiRefresh);
		wirelessStateTextView = (TextView) findViewById(R.id.TextView_WirelessState);
		wirelessToggleButton = (ToggleButton) findViewById(R.id.ToggleButton_Wifi);

		Model.getInstance().setActivity(this);

		addListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

		pd = ProgressDialog.show(this, "Registration running.",
				"Please wait...");
		pd.show();

		new Thread() {
			public void run() {
				if (!Model.getInstance().getApp().isRegistered()) {
					Model.getInstance().getApp().registerAtPMP();
				} else {
					registrationEnded();
				}
			}
		}.start();
	}

	private void addListener() {
		buttonServiceFeautres.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Model.getInstance().getApp().requestServiceFeatures();
			}
		});

		wirelessRefreshButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				refreshWifi();
			}
		});

		wirelessToggleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Model.getInstance().setWifi(wirelessToggleButton.isChecked());
			}
		});
	}

	private void refresh() {
		refreshWifi();
	}

	private void refreshWifi() {
		if (!Model.getInstance().getApp()
				.isServiceFeatureEnabled(Model.SF_WIFI_STATE)) {
			// Wireless State SF is disabled
			wirelessStateTextView.setText("missingSF");
		} else {
			wirelessStateTextView
					.setText((Model.getInstance().getWifi() ? "enabled"
							: "disabled"));
			wirelessToggleButton.setChecked(Model.getInstance().getWifi());
		}

		if (!Model.getInstance().getApp()
				.isServiceFeatureEnabled(Model.SF_WIFI_CHANGE)) {
			// Wireless State SF is disabled
			wirelessToggleButton.setEnabled(false);
		} else {
			wirelessToggleButton.setEnabled(true);
		}
	}

	public void registrationEnded() {
		handler.post(new Runnable() {

			public void run() {
				pd.dismiss();
				refresh();
			}
		});
	}
}
