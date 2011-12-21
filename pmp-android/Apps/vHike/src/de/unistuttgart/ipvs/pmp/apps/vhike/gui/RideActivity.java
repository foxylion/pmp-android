package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Intent;
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
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * This Activity gives user freedom to act as a Driver or Passenger
 * 
 * @author Andre Nguyen
 * 
 */
public class RideActivity extends Activity {

	private Spinner spinner;
	private Spinner spinnerSeats;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride);

		registerListener();
	}

	private void registerListener() {

		spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.array_cities,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinnerSeats = (Spinner) findViewById(R.id.spinner_numSeats);
		adapter = ArrayAdapter.createFromResource(this, R.array.array_numSeats,
				android.R.layout.simple_spinner_item);
		spinnerSeats.setAdapter(adapter);

		Button btnDrive = (Button) findViewById(R.id.Button_Drive);
		Button btnSearch = (Button) findViewById(R.id.Button_Search);

		btnDrive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String destination = spinner.getSelectedItem().toString();
				int numSeats = spinnerSeats.getSelectedItemPosition();
				if (numSeats == 0) {
					numSeats = 1;
				}

				Controller ctrl = new Controller();

				switch (ctrl.announceTrip(Model.getInstance().getSid(),
						destination, 0, 0, numSeats)) {
				case Constants.TRIP_STATUS_ANNOUNCED: {
					Toast.makeText(RideActivity.this, "Announced trip",
							Toast.LENGTH_LONG).show();

					vhikeDialogs.getInstance().getAnnouncePD(RideActivity.this)
							.show();

					Intent intent = new Intent(RideActivity.this,
							DriverViewActivity.class);
					RideActivity.this.startActivity(intent);

					break;
				}
				case Constants.TRIP_STATUS_OPEN_TRIP:
					Toast.makeText(RideActivity.this, "Trip already exists",
							Toast.LENGTH_LONG).show();
					break;
				case Constants.STATUS_ERROR:
					Toast.makeText(RideActivity.this, "Error anouncing trip",
							Toast.LENGTH_LONG).show();
					break;

				}

			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				vhikeDialogs.getInstance().getSearchPD(RideActivity.this)
						.show();
				Intent intent = new Intent(RideActivity.this,
						PassengerViewActivity.class);
				RideActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		RideActivity.this.finish();
	}

}
