package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.AddStopOverListener;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.IDialogFinishedCallBack;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * This Activity gives user freedom to act as a Driver or Passenger, later user will get the chance to start preplanned
 * rides, set needed or available seats depending on acting as driver or passenger, set destination over multiple stop
 * overs
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class MyTripActivity extends Activity implements IDialogFinishedCallBack {
    
    private final int DIALOG_DATE_TIME_PICKER = 1;
    private SparseArray<Dialog> dialogs;
    
    private Controller ctrl;
    
    private RadioButton pickDate;
    private Spinner spinner;
    private Spinner spinnerSeats;
    private Button addButton;
    
    private Calendar plannedDate;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        
        ctrl = new Controller();
        
        registerListener();
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        //        Model.getInstance().
    }
    
    
    private void registerListener() {
        
        // Now
        
        // Pick a date
        pickDate = (RadioButton) findViewById(R.id.radio_later);
        pickDate.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                getDialog(DIALOG_DATE_TIME_PICKER).show();
            }
        });
        
        // Destination Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick(View v) {
                for (Spinner s : ViewModel.getInstance().getDestinationSpinners()) {
                    if (s == (Spinner) v) {
                        ViewModel.getInstance().setClickedSpinner(s);
                    }
                }
                vhikeDialogs.getInstance().spDialog(v.getContext()).show();
                return false;
            }
            
        });
        ViewModel.getInstance().getDestinationSpinners().add(spinner);
        
        // set as add-Button and set the OnClickListener
        addButton = (Button) findViewById(R.id.ib_add);
        addButton.setOnClickListener(new AddStopOverListener());
        
        // Number of Seats spinner
        spinnerSeats = (Spinner) findViewById(R.id.spinner_numSeats);
        adapter = ArrayAdapter.createFromResource(this, R.array.array_numSeats, android.R.layout.simple_spinner_item);
        spinnerSeats.setAdapter(adapter);
        
        // Button Drive and Search
        Button btnDrive = (Button) findViewById(R.id.Button_Drive);
        btnDrive.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                ViewModel.getInstance().setDestination(spinner);
                ViewModel.getInstance().setNumSeats(spinnerSeats);
                
                Log.i(this, "Destination and StopOvers: " + ViewModel.getInstance().getDestination());
                
                switch (ctrl.announceTrip(Model.getInstance().getSid(), ViewModel.getInstance().getDestination(), 0, 0,
                        ViewModel.getInstance().getNumSeats())) {
                    case Constants.TRIP_STATUS_ANNOUNCED: {
                        Log.i(this, "ANNOUNCED trip");
                        
                        vhikeDialogs.getInstance().getAnnouncePD(MyTripActivity.this).show();
                        
                        Intent intent = new Intent(MyTripActivity.this, DriverViewActivity.class);
                        MyTripActivity.this.startActivity(intent);
                        
                        break;
                    }
                    case Constants.TRIP_STATUS_OPEN_TRIP:
                        
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyTripActivity.this);
                        builder.setMessage("Trip already exists.\nDo you want to end the current trip?")
                                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    
                                    public void onClick(DialogInterface dialog, int id) {
                                        Controller ctrl = new Controller();
                                        switch (ctrl.endTrip(Model.getInstance().getSid(), Model.getInstance()
                                                .getTripId())) {
                                            case (Constants.STATUS_UPDATED): {
                                                Toast.makeText(MyTripActivity.this, "Trip ended", Toast.LENGTH_LONG)
                                                        .show();
                                                ViewModel.getInstance().clearDriverOverlayList();
                                                ViewModel.getInstance().clearHitchPassengers();
                                                dialog.cancel();
                                                break;
                                            }
                                            case (Constants.STATUS_UPTODATE): {
                                                Toast.makeText(MyTripActivity.this, "Up to date", Toast.LENGTH_LONG)
                                                        .show();
                                                dialog.cancel();
                                                break;
                                            }
                                            case (Constants.STATUS_NOTRIP): {
                                                Toast.makeText(MyTripActivity.this, "No trip", Toast.LENGTH_LONG)
                                                        .show();
                                                dialog.cancel();
                                                break;
                                            }
                                            case (Constants.STATUS_HASENDED): {
                                                Toast.makeText(MyTripActivity.this, "Trip ended", Toast.LENGTH_LONG)
                                                        .show();
                                                dialog.cancel();
                                                break;
                                            }
                                            case (Constants.STATUS_INVALID_USER):
                                                Toast.makeText(MyTripActivity.this, "Invalid user", Toast.LENGTH_LONG)
                                                        .show();
                                                dialog.cancel();
                                                break;
                                        }
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(MyTripActivity.this, DriverViewActivity.class);
                                        MyTripActivity.this.startActivity(intent);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        
                        break;
                    case Constants.STATUS_ERROR:
                        Toast.makeText(MyTripActivity.this, "Error anouncing trip", Toast.LENGTH_SHORT).show();
                        break;
                
                }
                
            }
        });
        
        // Button Search
        Button btnSearch = (Button) findViewById(R.id.Button_Search);
        btnSearch.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                Log.i(this, "SIZE: " + ViewModel.getInstance().getDestinationSpinners().size());
                
                if (ViewModel.getInstance().getDestinationSpinners().size() > 1) {
                    Toast.makeText(MyTripActivity.this, "Only one destination allowed for passenger",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ViewModel.getInstance().setDestination4Passenger(spinner);
                    ViewModel.getInstance().setNumSeats(spinnerSeats);
                    
                    vhikeDialogs.getInstance().getSearchPD(MyTripActivity.this).show();
                    Intent intent = new Intent(MyTripActivity.this, PassengerViewActivity.class);
                    MyTripActivity.this.startActivity(intent);
                }
                
            }
        });
    }
    
    
    protected Dialog getDialog(int dialogID) {
        dialogs = (dialogs == null) ? new SparseArray<Dialog>(1) : dialogs;
        
        switch (dialogID) {
            case DIALOG_DATE_TIME_PICKER:
                if (dialogs.get(dialogID) == null) {
                    dialogs.append(dialogID,
                            vhikeDialogs.getInstance().getDateTimePicker(MyTripActivity.this, dialogID, plannedDate));
                }
                return dialogs.get(dialogID);
            default:
                return null;
        }
    }
    
    
    public Calendar getPlannedDate() {
        return plannedDate;
    }
    
    
    public void setPlannedDate(Calendar plannedDate) {
        this.plannedDate = plannedDate;
    }
    
    
    @Override
    public void dialogFinished(int dialogId, int buttonId) {
        
        switch (dialogId) {
            case DIALOG_DATE_TIME_PICKER:
                try {
                    DatePicker d = (DatePicker) getDialog(dialogId).findViewById(R.id.dpicker);
                    TimePicker t = (TimePicker) getDialog(dialogId).findViewById(R.id.tpicker);
                    if (plannedDate == null)
                        plannedDate = Calendar.getInstance();
                    plannedDate.set(d.getYear(), d.getMonth(), d.getDayOfMonth(), t.getCurrentHour(),
                            t.getCurrentMinute(), 0);
                    if (plannedDate.before(Calendar.getInstance())) {
                        RadioButton r = (RadioButton) findViewById(R.id.radio_now);
                        r.toggle();
                        r = (RadioButton) findViewById(R.id.radio_later);
                        r.setText(R.string.dialog_choose_date);
                        Toast.makeText(this, R.string.date_in_past, Toast.LENGTH_LONG).show();
                    } else {
                        RadioButton r = (RadioButton) findViewById(R.id.radio_later);
                        r.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(plannedDate.getTime()));
                    }
                } catch (Exception e) {
                    RadioButton r = (RadioButton) findViewById(R.id.radio_now);
                    r.toggle();
                    r = (RadioButton) findViewById(R.id.radio_later);
                    r.setText(R.string.dialog_choose_date);
                    Toast.makeText(this, R.string.date_get_error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        
    }
}
