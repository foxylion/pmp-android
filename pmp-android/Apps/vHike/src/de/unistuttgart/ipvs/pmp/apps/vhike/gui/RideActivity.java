package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * This Activity gives user freedom to act as a Driver or Passenger
 * 
 * @author Andre Nguyen
 * 
 */
public class RideActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride);

		registerListener();
	}

	private void registerListener() {

		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.array_cities,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		Button btnDrive = (Button) findViewById(R.id.Button_Drive);
		Button btnSearch = (Button) findViewById(R.id.Button_Search);

		btnDrive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Controller ctrl = new Controller();

				switch (ctrl.announceTrip(Model.getInstance().getSid(),
						"Berlin", 0, 0, 3)) {
				case Constants.TRIP_STATUS_ANNOUNCED:
					Toast.makeText(RideActivity.this, "Announced trip",
							Toast.LENGTH_LONG).show();

					break;

				case Constants.TRIP_STATUS_OPEN_TRIP:
					Toast.makeText(RideActivity.this, "Trip already exists",
							Toast.LENGTH_LONG).show();

					break;
				case Constants.STATUS_ERROR:
					Toast.makeText(RideActivity.this, "Error anouncing trip",
							Toast.LENGTH_LONG).show();

					break;

				}

				Intent intent = new Intent(RideActivity.this,
						DriverViewActivity.class);
				RideActivity.this.startActivity(intent);

			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RideActivity.this,
						PassengerViewActivity.class);
				RideActivity.this.startActivity(intent);
			}
		});
	}

}
