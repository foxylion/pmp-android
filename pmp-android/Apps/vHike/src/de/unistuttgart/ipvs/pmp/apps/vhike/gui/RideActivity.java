/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.OnConfirmationDialogFinished;

/**
 * This Activity gives user freedom to act as a Driver or Passenger, later user will get the chance
 * to start future rides, set needed or available seats depending on acting as driver or passenger,
 * set destination over multiple stop overs
 * 
 * @author Andre Nguyen, Dang Huynh
 */
public class RideActivity extends Activity implements OnConfirmationDialogFinished {
    
    @Override
    public void confirmDialogPositive(int callbackFunctionID) {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void confirmDialogNegative(int callbackFunctionID) {
        // TODO Auto-generated method stub
        
    }
    
    // // Function call back ID(s)
    // private static final int CONFIRM_END_TRIP = 0;
    //
    // private Controller ctrl;
    // private final String sid = Model.getInstance().getSid();
    //
    // private Button rideDate;
    // private Spinner spinner;
    // private Spinner spinnerSeats;
    // private Button addButton;
    //
    // @Override
    // public void onCreate(final Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    //
    // // TODO if not logged in
    // setContentView(R.layout.activity_ride);
    //
    // ctrl = new Controller();
    //
    // registerListener();
    // }
    //
    // private void registerListener() {
    //
    // rideDate = (Button) findViewById(R.id.ride_date);
    // final Calendar c = Calendar.getInstance();
    // final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // final String formattedDate = df.format(c.getTime());
    // rideDate.setText(formattedDate);
    // rideDate.setOnClickListener(new View.OnClickListener() {
    //
    // @Override
    // public void onClick(final View v) {
    // }
    // });
    //
    // // Destination Spinner
    // spinner = (Spinner) findViewById(R.id.spinner);
    // ArrayAdapter<CharSequence> adapter =
    // ArrayAdapter.createFromResource(this, R.array.array_cities,
    // android.R.layout.simple_spinner_item);
    // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // spinner.setAdapter(adapter);
    // spinner.setOnLongClickListener(new OnLongClickListener() {
    //
    // @Override
    // public boolean onLongClick(final View v) {
    // for (final Spinner s : ViewModel.getInstance()
    // .getDestinationSpinners()) {
    // if (s == (Spinner) v) {
    // ViewModel.getInstance().setClickedSpinner(s);
    // }
    // }
    // vhikeDialogs.getInstance().spDialog(v.getContext()).show();
    // return false;
    // }
    //
    // });
    // ViewModel.getInstance().getDestinationSpinners().add(spinner);
    //
    // // set as add-Button and set the OnClickListener
    // addButton = (Button) findViewById(R.id.ib_add);
    // addButton.setOnClickListener(new AddStopOverListener());
    //
    // // Number of Seats spinner
    // spinnerSeats = (Spinner) findViewById(R.id.spinner_numSeats);
    // adapter =
    // ArrayAdapter.createFromResource(this, R.array.array_numSeats,
    // android.R.layout.simple_spinner_item);
    // spinnerSeats.setAdapter(adapter);
    //
    // final Button btnDrive = (Button) findViewById(R.id.Button_Drive);
    // final Button btnSearch = (Button) findViewById(R.id.Button_Search);
    //
    // btnDrive.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(final View v) {
    //
    // // TODO IF NOW
    // // See if an open trip is open
    // switch (ctrl.getOpenTrip(sid)) {
    // case Constants.STATUS_ERROR:
    // Toast.makeText(RideActivity.this,
    // "Cannot check for open trip",
    // Toast.LENGTH_LONG).show();
    // return;
    // case Constants.TRUE:
    // // Confirm end trip
    // vhikeDialogs
    // .getConfirmationDialog(RideActivity.this,
    // getString(R.string.confirm_end_trip),
    // getString(R.string.default_yes),
    // getString(R.string.default_no), RideActivity.CONFIRM_END_TRIP);
    //
    // case Constants.FALSE:
    // RideActivity.this.announceTrip();
    //
    // default:
    // Log.d(this, getString(R.string.error_unknown) + ": getOpenTrip");
    // }
    //
    // // TODO IF NOTNOW
    // // announce trip
    // } // end onClick btnDrive
    // });
    //
    // btnSearch.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(final View v) {
    //
    // Log.i(this, "SIZE: "
    // + ViewModel.getInstance().getDestinationSpinners()
    // .size());
    //
    // if (ViewModel.getInstance().getDestinationSpinners().size() > 1) {
    // Toast.makeText(RideActivity.this,
    // "Only one destination allowed for passenger",
    // Toast.LENGTH_SHORT).show();
    // } else {
    // ViewModel.getInstance().setDestination4Passenger(spinner);
    // ViewModel.getInstance().setNumSeats(spinnerSeats);
    //
    // vhikeDialogs.getInstance().getSearchPD(RideActivity.this)
    // .show();
    // final Intent intent = new Intent(RideActivity.this,
    // PassengerViewActivity.class);
    // RideActivity.this.startActivity(intent);
    // }
    //
    // }
    // });
    // }
    //
    // private void announceTrip() {
    // ViewModel.getInstance().setDestination(spinner);
    // ViewModel.getInstance().setNumSeats(spinnerSeats);
    //
    // Log.d(this, "Destination and StopOvers: " + ViewModel.getInstance().getDestination());
    //
    // switch (ctrl.announceTrip(sid, ViewModel.getInstance().getDestination(), 0, 0,
    // ViewModel.getInstance().getNumSeats())) {
    //
    // case Constants.STATUS_SUCCESS:
    // Log.d(this, "Trip announced succesfully");
    //
    // // Show progress dialog for getting position
    // vhikeDialogs.getInstance().getAnnouncePD(RideActivity.this).show();
    //
    // final Intent intent = new Intent(RideActivity.this, DriverViewActivity.class);
    // RideActivity.this.startActivity(intent);
    // break;
    //
    // case Constants.TRIP_STATUS_OPEN_TRIP:
    //
    // final AlertDialog.Builder builder = new AlertDialog.Builder(
    // RideActivity.this);
    // builder.setMessage(
    // "Trip already exists.\nDo you want to end the current trip?")
    // .setCancelable(false)
    // .setPositiveButton(R.string.default_yes,
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(
    // final DialogInterface dialog,
    // final int id) {
    // final Controller ctrl = new Controller();
    // switch (ctrl.endTrip(Model
    // .getInstance().getSid(),
    // Model.getInstance()
    // .getTripId())) {
    // case Constants.STATUS_UPDATED: {
    // Toast.makeText(
    // RideActivity.this,
    // "Trip ended",
    // Toast.LENGTH_LONG)
    // .show();
    // ViewModel
    // .getInstance()
    // .clearDriverOverlayList();
    // ViewModel.getInstance()
    // .clearHitchPassengers();
    // dialog.cancel();
    // break;
    // }
    // case Constants.STATUS_UPTODATE: {
    // Toast.makeText(
    // RideActivity.this,
    // "Up to date",
    // Toast.LENGTH_LONG)
    // .show();
    // dialog.cancel();
    // break;
    // }
    // case Constants.STATUS_NO_TRIP: {
    // Toast.makeText(
    // RideActivity.this,
    // "No trip",
    // Toast.LENGTH_LONG)
    // .show();
    // dialog.cancel();
    // break;
    // }
    // case Constants.STATUS_HASENDED: {
    // Toast.makeText(
    // RideActivity.this,
    // "Trip ended",
    // Toast.LENGTH_LONG)
    // .show();
    // dialog.cancel();
    // break;
    // }
    // case Constants.STATUS_INVALID_USER:
    // Toast.makeText(
    // RideActivity.this,
    // "Invalid user",
    // Toast.LENGTH_LONG)
    // .show();
    // dialog.cancel();
    // break;
    // default:
    // Toast.makeText(
    // RideActivity.this,
    // "Error anouncing trip",
    // Toast.LENGTH_SHORT)
    // .show();
    // }
    // }
    // })
    // .setNegativeButton(R.string.default_no,
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(
    // final DialogInterface dialog,
    // final int id) {
    // dialog.cancel();
    // final Intent intent = new Intent(
    // RideActivity.this,
    // DriverViewActivity.class);
    // RideActivity.this
    // .startActivity(intent);
    // }
    // });
    // final AlertDialog alert = builder.create();
    // alert.show();
    // break;
    //
    // case Constants.STATUS_ERROR:
    // Toast.makeText(RideActivity.this, "Error anouncing trip",
    // Toast.LENGTH_SHORT).show();
    // break;
    // } // end switch
    // } // end announce trip
    //
    // private boolean endOpenTrip() throws Exception {
    // switch (ctrl.endTrip(sid, -1)) {
    // case Constants.STATUS_SUCCESS:
    // return true;
    // case Constants.STATUS_ERROR:
    // // TODO get error message
    // throw new Exception();
    // default:
    // return false;
    // }
    // }
    //
    // @Override
    // public void confirmDialogPositive(int callbackFunctionID) {
    // switch (callbackFunctionID) {
    // case CONFIRM_END_TRIP:
    // try {
    // if (endOpenTrip()) {
    // announceTrip();
    // }
    // } catch (Exception e) {
    // // TODO Handle Exception
    // }
    // return;
    // default:
    // return;
    // }
    // }
    //
    // @Override
    // public void confirmDialogNegative(int callbackFunctionID) {
    // // Do nothing for now
    // }
}
