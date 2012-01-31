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
package de.unistuttgart.ipvs.pmp.apps.locationtestapp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.locationtestapp.R;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class LocationAppActivity extends Activity {

	private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
	private static final String R_NAME = "absoluteLocation";


	private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_NAME,
			R_NAME);
	
	public Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.handler = new Handler();

		PMP.get(getApplication());

		setContentView(R.layout.main);

		addListener();
	}

	@Override
	protected void onResume() {
		super.onResume();

		PMP.get().getResource(R_ID, new PMPRequestResourceHandler() {
			@Override
			public void onReceiveResource(PMPResourceIdentifier resource,
					IBinder binder) {
				resourceCached();
			}
			
			@Override
			public void onBindingFailed() {
				Toast.makeText(LocationAppActivity.this, "Binding Resource failed", Toast.LENGTH_LONG).show();
			}
		});
	}


	private void resourceCached() {
		IBinder binder = PMP.get().getResourceFromCache(R_ID);
		IAbsoluteLocation loc = IAbsoluteLocation.Stub.asInterface(binder);
		try {
			loc.startLocationLookup();
			Toast.makeText(this, "locationFixed: " + loc.isFixed(), Toast.LENGTH_LONG).show();
		} catch (RemoteException e) {
			
		}
		
	};
	
	private void addListener() {
		
	}
}
