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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.ProgressDialog;

public class SimpleAppActivity extends Activity {

	public Handler handler;

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
					registrationEnded(true, "");
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

	public void refresh() {
		refreshWifi();

		String string = "Current active SF:\n";
		string += Model.SF_WIFI_STATE
				+ ": "
				+ Model.getInstance().getApp()
						.isServiceFeatureEnabled(Model.SF_WIFI_STATE) + "\n";
		string += Model.SF_WIFI_CHANGE
				+ ": "
				+ Model.getInstance().getApp()
						.isServiceFeatureEnabled(Model.SF_WIFI_CHANGE);

		Toast.makeText(SimpleAppActivity.this, string, Toast.LENGTH_LONG)
				.show();
	}

	public void refreshWifi() {
		new Thread() {
			@Override
			public void run() {
				final boolean wifi = Model.getInstance().getWifi();

				handler.post(new Runnable() {

					public void run() {
						if (!Model.getInstance().getApp()
								.isServiceFeatureEnabled(Model.SF_WIFI_STATE)) {
							// Wireless State SF is disabled
							wirelessStateTextView.setText("missingSF");
						} else {
							wirelessStateTextView.setText((wifi ? "enabled"
									: "disabled"));
							wirelessToggleButton.setChecked(wifi);
						}

						if (!Model.getInstance().getApp()
								.isServiceFeatureEnabled(Model.SF_WIFI_CHANGE)) {
							// Wireless State SF is disabled
							wirelessToggleButton.setEnabled(false);
						} else {
							wirelessToggleButton.setEnabled(true);
						}
					}
				});
			}
		}.start();
	}

	public void registrationEnded(final boolean success, final String message) {
		handler.post(new Runnable() {

			public void run() {
				pd.dismiss();
				refresh();

				if (success && message == null) {
					Toast.makeText(getApplicationContext(),
							"SimpleApp: Registration successed.",
							Toast.LENGTH_SHORT).show();
				} else if (!success && message != null) {
					Toast.makeText(
							getApplicationContext(),
							"SimpleApp: Registration failed with the following message: "
									+ message, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"SimpleApp: Already registered.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
