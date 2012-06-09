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

import java.util.Timer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Check4Location;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Check4Queries;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyMapActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * The DriverViewActivity is responsible for the representation of the drivers actions. Also, partly handling the
 * interaction and thereby controlling the GUI
 * 
 * @author Andre Nguyen
 * 
 */
public class DriverViewActivity extends ResourceGroupReadyMapActivity {
    
    private Context context;
    private MapView mapView;
    
    private Handler handler;
    private Handler locationHandler;
    private Handler queryHandler;
    
    private Controller ctrl;
    
    private Timer locationTimer;
    private Timer queryTimer;
    
    private Check4Location c4l;
    private Check4Queries c4q;
    
    private Button road_info;
    private EditText et_road_info;
    
    
    public DriverViewActivity() {
        this.context = DriverViewActivity.this;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverview);
        
        PMP.get(getApplication());
        
        this.locationHandler = new Handler();
        this.queryHandler = new Handler();
        this.handler = new Handler();
        
        // Reset timers, lists, dialogs, map
        ViewModel.getInstance().initPassengersList();
        ViewModel.getInstance().initRouteList();
        ViewModel.getInstance().resetTimers();
        ViewModel.getInstance().setNewFound();
        
        vhikeDialogs.getInstance().getAnnouncePD(DriverViewActivity.this).dismiss();
        //        vhikeDialogs.getInstance().clearAnnouncPD();
        
        setMapView();
        ViewModel.getInstance().getDriverOverlayList(this.mapView).clear();
        
        this.road_info = (Button) findViewById(R.id.btn_route_info);
        this.et_road_info = (EditText) findViewById(R.id.et_route_info);
        ViewModel.getInstance().setRoadInfoBtn(this.road_info);
        ViewModel.getInstance().setRoadInfoEt(this.et_road_info);
        this.road_info.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DriverViewActivity.this.road_info.setVisibility(View.GONE);
                DriverViewActivity.this.et_road_info.setVisibility(View.VISIBLE);
            }
        });
        this.et_road_info.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DriverViewActivity.this.road_info.setVisibility(View.VISIBLE);
                DriverViewActivity.this.et_road_info.setVisibility(View.GONE);
            }
        });
        
        if (getvHikeRG(this) != null && getLocationRG(this) != null && getContactRG(this) != null) {
            this.ctrl = new Controller(rgvHike);
            ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
            ViewModel.getInstance().setContactRG(rgContact);
            resourceCached();
            showHitchhikers();
        }
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    DriverViewActivity.this.ctrl = new Controller(rgvHike);
                    ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
                    ViewModel.getInstance().setContactRG(rgContact);
                    resourceCached();
                    showHitchhikers();
                }
            });
        }
        
    }
    
    
    /**
     * adds passengers (hitchhikers) to the notification slider
     */
    private void showHitchhikers() {
        
        ListView pLV = (ListView) findViewById(R.id.ListView_SearchingHitchhikers);
        pLV.setClickable(true);
        pLV.setAdapter(ViewModel.getInstance().getDriverAdapter(this.context, this.mapView));
    }
    
    
    /**
     * adds hitchhiker/passenger to hitchhiker list
     * 
     * @param hitchhiker
     */
    public void addHitchhiker(Profile hitchhiker) {
        ViewModel.getInstance().getHitchPassengers().add(hitchhiker);
        ViewModel.getInstance().getDriverAdapter(this.context, this.mapView).notifyDataSetChanged();
    }
    
    
    /**
     * displays the map from xml file and sets the zoom buttons
     */
    @SuppressWarnings("deprecation")
    private void setMapView() {
        this.mapView = (MapView) findViewById(R.id.driverMapView);
        
        LinearLayout zoomView = (LinearLayout) this.mapView.getZoomControls();
        
        zoomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        
        zoomView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        zoomView.setVerticalScrollBarEnabled(true);
        this.mapView.addView(zoomView);
    }
    
    
    private void resourceCached() {
        try {
            rgLocation.startLocationLookup(5000, 20.0F);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        startContinousLookup();
    };
    
    
    private void startContinousLookup() {
        this.locationTimer = new Timer();
        this.queryTimer = new Timer();
        
        this.c4l = new Check4Location(rgvHike, rgLocation, this.mapView, this.context, this.locationHandler,
                0);
        this.locationTimer.schedule(this.c4l, 10000, 10000);
        Log.i(this, "Location started");
        
        // Start Check4Queries Class to check for queries
        this.c4q = new Check4Queries(rgvHike, this.queryHandler);
        this.queryTimer.schedule(this.c4q, 10000, 10000);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driverview_menu, menu);
        return true;
    }
    
    
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (0):
                if (resultCode == RESULT_OK) {
                    Intent myIntent = new Intent(DriverViewActivity.this, DriverViewActivity.this.getClass());
                    myIntent.putExtra("tab_id", 2);
                    startActivity(myIntent);
                }
        }
    }
    
    
    @Override
    public void onBackPressed() {
        
        DriverViewActivity.this.finish();
        
        return;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_endTrip:
                
                switch (this.ctrl.endTrip(Model.getInstance().getSid(), Model.getInstance().getTripId())) {
                    case Constants.STATUS_SUCCESS: {
                        
                        ViewModel.getInstance().clearDriverOverlayList();
                        ViewModel.getInstance().clearViewModel();
                        ViewModel.getInstance().clearHitchPassengers();
                        ViewModel.getInstance().clearDriverNotificationAdapter();
                        ViewModel.getInstance().clearRoutes();
                        ViewModel.getInstance().resetRoadInfo();
                        stopContinousLookup();
                        
                        Log.i(this, "Trip ended.");
                        DriverViewActivity.this.finish();
                        break;
                    }
                    case Constants.STATUS_ERROR: {
                        Toast.makeText(DriverViewActivity.this, "STATUS_Error", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Constants.STATUS_NO_TRIP: {
                        Toast.makeText(DriverViewActivity.this, "No trip", Toast.LENGTH_SHORT).show();
                        
                        DriverViewActivity.this.finish();
                        break;
                    }
                    case Constants.STATUS_INVALID_USER:
                        Toast.makeText(DriverViewActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            
            case R.id.mi_updateData:
                vhikeDialogs.getInstance().getUpdateDataDialog(rgvHike, this.context, 0).show();
                break;
        }
        return true;
    }
    
    
    private void stopContinousLookup() {
        
        if (this.locationTimer != null) {
            DriverViewActivity.this.locationTimer.cancel();
            ViewModel.getInstance().cancelLocation();
            Log.i(this, "Timer Location canceled");
        }
        
        if (this.queryTimer != null) {
            DriverViewActivity.this.queryTimer.cancel();
            ViewModel.getInstance().cancelQuery();
            Log.i(this, "Timer Query canceled");
        }
        
    }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        switch (this.ctrl.endTrip(Model.getInstance().getSid(), Model.getInstance().getTripId())) {
            case Constants.STATUS_SUCCESS: {
                
                ViewModel.getInstance().clearDriverOverlayList();
                ViewModel.getInstance().clearViewModel();
                ViewModel.getInstance().clearHitchPassengers();
                ViewModel.getInstance().clearDriverNotificationAdapter();
                try {
                    ViewModel.getInstance().clearRoutes();
                } catch (NullPointerException e) {
                    
                }
                ViewModel.getInstance().resetRoadInfo();
                stopContinousLookup();
                
                Log.i(this, "Trip ended.");
                DriverViewActivity.this.finish();
                break;
            }
            case Constants.STATUS_ERROR: {
                Toast.makeText(DriverViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                break;
            }
            case Constants.STATUS_NO_TRIP: {
                Toast.makeText(DriverViewActivity.this, "No trip", Toast.LENGTH_SHORT).show();
                
                DriverViewActivity.this.finish();
                break;
            }
            case Constants.STATUS_INVALID_USER:
                Toast.makeText(DriverViewActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
