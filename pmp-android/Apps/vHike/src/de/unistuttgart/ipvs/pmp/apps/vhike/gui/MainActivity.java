package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * The main menu after user logged in and the main activity to start other activities
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class MainActivity extends ResourceGroupReadyActivity {
    
    private Handler handler;
    private Controller ctrl;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        handler = new Handler();
        registerListener();
        
        vhikeDialogs.getInstance().getLoginPD(MainActivity.this).dismiss();
        
        vhikeDialogs.getInstance().clearLoginPD();
        
        if (getvHikeRG(this) != null && getLocationRG(this) != null && getContactRG(this) != null) {
            ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
            ViewModel.getInstance().setContactRG(rgContact);
        }
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        vHikeService.getInstance().updateServiceFeatures();
        ctrl = new Controller(rgvHike);
        
        Log.i(this, "");
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
                    ViewModel.getInstance().setContactRG(rgContact);
                }
            });
        }
        
    }
    
    
    private void registerListener() {
        Button btnRide = (Button) findViewById(R.id.Button_Ride);
        Button btnProfile = (Button) findViewById(R.id.Button_Profile);
        Button btnHistory = (Button) findViewById(R.id.Button_History);
        Button btnMyTrips = (Button) findViewById(R.id.Button_MyTrips);
        findViewById(R.id.Button_Message);
        Button btnLogout = (Button) findViewById(R.id.Button_Logout);
        
        btnRide.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlanTripActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        
        btnProfile.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("MY_PROFILE", 0);
                MainActivity.this.startActivity(intent);
            }
        });
        
        btnHistory.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryMenuActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        
        btnMyTrips.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //                List<PrePlannedTrip> lppp = ctrl.getMyTrips(Model.getInstance().getOwnProfile().getID());
                //                Log.i(this, "Lppp: " + lppp.size());
                //                for (PrePlannedTrip ppt : lppp) {
                //                    Log.i(this, "TripID: " + ppt.getTid());
                //                    Log.i(this, "Destination: " + ppt.getDestination());
                //                    Log.i(this, "Time: " + ppt.getDate());
                //                    Log.i(this, "Passengers: " + ppt.getPassengers());
                //                    Log.i(this, "Invites: " + ppt.getInvites());
                //                }
                Intent intent = new Intent(MainActivity.this, MyTripsActivity.class);
                MainActivity.this.startActivity(intent);
            }
            
        });
        
        btnLogout.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    ((Button) findViewById(R.id.Button_Logout)).setEnabled(false);
                    if (MainActivity.this.logoutTimer == null) {
                        MainActivity.this.logoutTimer = new Timer();
                    }
                    if (getvHikeRG(MainActivity.this) != null) {
                        MainActivity.this.logoutTimer.schedule(MainActivity.this.logoutTask, 0);
                    } else {
                        MainActivity.this.logoutTimer.schedule(MainActivity.this.logoutTask, 10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private Timer logoutTimer;
    
    private TimerTask logoutTask = new TimerTask() {
        
        @Override
        public void run() {
            if (rgvHike != null) {
                Controller ctrl = new Controller(rgvHike);
                if (ctrl.logout(Model.getInstance().getSid())) {
                    MainActivity.this.finish();
                } else {
                    ((Button) findViewById(R.id.Button_Logout)).setEnabled(true);
                }
            } else {
                MainActivity.this.logoutTimer.schedule(MainActivity.this.logoutTask, 10);
            }
        }
    };
}
