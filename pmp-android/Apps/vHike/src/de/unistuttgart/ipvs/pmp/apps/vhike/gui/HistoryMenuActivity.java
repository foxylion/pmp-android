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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;

public class HistoryMenuActivity extends Activity{
	Button btn_driver;
	Button btn_passenger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_history_menu);
		
		btn_driver = (Button) findViewById(R.id.Button_Driver_History);
		btn_passenger = (Button) findViewById(R.id.Button_Passenger_History);
		
		createTouchListener(this.getBaseContext());
	}
	
	private void createTouchListener(final Context context) {
		btn_driver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "Driver", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(HistoryMenuActivity.this,
						HistoryActivity.class);
				intent.putExtra("IS_DRIVER", true);
				HistoryMenuActivity.this.startActivity(intent);
			}
		});
		btn_passenger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "Passenger", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(HistoryMenuActivity.this,
						HistoryActivity.class);
				intent.putExtra("IS_DRIVER", false);
				HistoryMenuActivity.this.startActivity(intent);
			}
		});
	}
	
	
}
