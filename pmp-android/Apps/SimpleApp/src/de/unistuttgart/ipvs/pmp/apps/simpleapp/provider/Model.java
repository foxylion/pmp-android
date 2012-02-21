/*
 * Copyright 2012 pmp-android development team
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
package de.unistuttgart.ipvs.pmp.apps.simpleapp.provider;

import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.simpleapp.gui.SimpleAppActivity;
import de.unistuttgart.ipvs.pmp.resourcegroups.switches.IWifiSwitch;

public class Model {

	private static final String RG_SWITCHES = "de.unistuttgart.ipvs.pmp.resourcegroups.switches";
	private static final String R_WIFI = "WifiSwitch";
	private static final PMPResourceIdentifier RES_WIFI = PMPResourceIdentifier
			.make(RG_SWITCHES, R_WIFI);

	public static final String SF_WIFI_STATE = "wifiState";
	public static final String SF_WIFI_CHANGE = "wifiChange";

	private static Model instance;

	private SimpleAppActivity activity;

	private Model() {

	}

	public static Model getInstance() {
		if (Model.instance == null) {
			Model.instance = new Model();
		}

		return Model.instance;
	}

	public void setWifi(final boolean state) {
		PMP.get().getResource(RES_WIFI, new PMPRequestResourceHandler() {

			@Override
			public void onReceiveResource(PMPResourceIdentifier resource,
					IBinder binder) {
				IWifiSwitch remote = IWifiSwitch.Stub.asInterface(binder);
				Model.this.activity.runOnUiThread(new Runnable() {

					public void run() {
						getActivity().refreshWifi();
					}
				});

				try {
					remote.setState(state);
				} catch (Exception e) {
					Log.e(this, "Could not set Wifi State", e);
					makeToast("Wifi state could not be changed");
				}
			}
		});

	}

	public void getWifi(final Handler handler,
			final TextView wirelessStateTextView,
			final ToggleButton wirelessToggleButton) {
		PMP.get().getResource(RES_WIFI, new PMPRequestResourceHandler() {

			@Override
			public void onReceiveResource(PMPResourceIdentifier resource,
					IBinder binder) {
				final IWifiSwitch remote = IWifiSwitch.Stub.asInterface(binder);

				Model.this.activity.runOnUiThread(new Runnable() {

					public void run() {
						try {

							boolean state = remote.getState();
							wirelessStateTextView.setText(state ? "enabled"
									: "disabled");
							wirelessToggleButton.setChecked(state);

						} catch (Exception e) {
							Log.e(this, "Could not get Wifi State", e);
							makeToast("Wifi state couldn't be fetched");
						}
					}
				});

			}
		});
	}

	public void setActivity(SimpleAppActivity activity) {
		this.activity = activity;
	}

	public SimpleAppActivity getActivity() {
		return this.activity;
	}

	private void makeToast(final String message) {
		this.activity.runOnUiThread(new Runnable() {

			public void run() {

				Toast.makeText(Model.this.activity, message, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
