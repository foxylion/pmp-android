package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * The main menu after user logged in and the main activity to start other activities
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class MainActivity extends ResourceGroupReadyActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        registerListener();
        
        vhikeDialogs.getInstance().getLoginPD(MainActivity.this).dismiss();
        
        vhikeDialogs.getInstance().clearLoginPD();
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
                vHikeService.requestServiceFeature(MainActivity.this, 0);
            }
            
        });
        
        //        btnMyTrips.setOnClickListener(new OnClickListener() {
        //
        //            @Override
        //            public void onClick(View v) {
        //                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
        //                MainActivity.this.startActivity(intent);
        //            }
        //        });
        //
        //        btnMessage.setOnClickListener(new OnClickListener() {
        //
        //            @Override
        //            public void onClick(View v) {
        //                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
        //                MainActivity.this.startActivity(intent);
        //            }
        //        });
        
        //        Button btnMessage = (Button) findViewById(R.id.Button_Message);
        //        btnMessage.setOnClickListener(new OnClickListener() {
        //            
        //            @Override
        //            public void onClick(View v) {
        //                Log.d(this, "btnMessaged clicked");
        //                
        //                // TODO richtig implementieren;
        //                IAbsoluteLocation loc = null;// vHikeService.getInstance().getLocationResourceGroup();
        //                if (loc == null) {
        //                    Log.d(this, "RG null");
        //                } else {
        //                    try {
        //                        Log.d(this, " " + loc.getAddress());
        //                    } catch (RemoteException e) {
        //                        // TODO Auto-generated catch block
        //                        e.printStackTrace();
        //                    }
        //                }
        //            }
        //        });
        
        //        Button btnmytrips = (Button) findViewById(R.id.Button_Trips);
        //        btnmytrips.setOnClickListener(new OnClickListener() {
        //            
        //            @Override
        //            public void onClick(View v) {
        //                vHikeService.requestServiceFeature(MainActivity.this, 0);
        //            }
        //        });
        
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
