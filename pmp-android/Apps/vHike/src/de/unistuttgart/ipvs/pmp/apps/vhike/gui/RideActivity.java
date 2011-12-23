package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * This Activity gives user freedom to act as a Driver or Passenger
 * 
 * @author Andre Nguyen
 * 
 */
public class RideActivity extends Activity {

	private Controller ctrl;

	private Spinner spinner;
	private Spinner spinnerSeats;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride);

		ctrl = new Controller();

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

				MapModel.getInstance().setDestination(spinner);
				MapModel.getInstance().setNumSeats(spinnerSeats);

				switch (ctrl.announceTrip(Model.getInstance().getSid(),
						MapModel.getInstance().getDestination(), 0, 0, MapModel
								.getInstance().getNumSeats())) {
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

					AlertDialog.Builder builder = new AlertDialog.Builder(
							RideActivity.this);
					builder.setMessage(
							"Trip already exists.\nDo you want to end the current trip?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											Controller ctrl = new Controller();
											switch (ctrl.endTrip(Model
													.getInstance().getSid(),
													Model.getInstance()
															.getTripId())) {
											case (Constants.STATUS_UPDATED): {
												Toast.makeText(
														RideActivity.this,
														"Trip ended",
														Toast.LENGTH_LONG)
														.show();
												dialog.cancel();
												break;
											}
											case (Constants.STATUS_UPTODATE): {
												Toast.makeText(
														RideActivity.this,
														"Up to date",
														Toast.LENGTH_LONG)
														.show();
												dialog.cancel();
												break;
											}
											case (Constants.STATUS_NOTRIP): {
												Toast.makeText(
														RideActivity.this,
														"No trip",
														Toast.LENGTH_LONG)
														.show();
												dialog.cancel();
												break;
											}
											case (Constants.STATUS_HASENDED): {
												Toast.makeText(
														RideActivity.this,
														"Trip ended",
														Toast.LENGTH_LONG)
														.show();
												dialog.cancel();
												break;
											}
											case (Constants.STATUS_INVALID_USER):
												Toast.makeText(
														RideActivity.this,
														"Invalid user",
														Toast.LENGTH_LONG)
														.show();
												dialog.cancel();
												break;
											}
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

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
				String destination = spinner.getSelectedItem().toString();
				int numSeats = spinnerSeats.getSelectedItemPosition();
				if (numSeats == 0) {
					numSeats = 1;
				}

				switch (ctrl.startQuery(Model.getInstance().getSid(),
						destination, 0, 0, numSeats)) {
				case (Constants.QUERY_ID_ERROR):
					Toast.makeText(RideActivity.this, "Query error",
							Toast.LENGTH_LONG).show();
					break;
				default:
					vhikeDialogs.getInstance().getSearchPD(RideActivity.this)
							.show();
					Intent intent = new Intent(RideActivity.this,
							PassengerViewActivity.class);
					RideActivity.this.startActivity(intent);
					break;
				}

			}
		});
	}

}
