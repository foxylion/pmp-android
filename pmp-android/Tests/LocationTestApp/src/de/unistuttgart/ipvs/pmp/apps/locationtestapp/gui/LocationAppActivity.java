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
import de.unistuttgart.ipvs.pmp.apps.locationtestapp.R;

public class LocationAppActivity extends Activity {

	public Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.handler = new Handler();

		setContentView(R.layout.main);

		addListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void addListener() {
		
	}
}
