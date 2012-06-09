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

import java.util.List;
import java.util.Timer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Check4Location;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Check4Offers;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyMapActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;

/**
 * PassengerViewActivity displays passenger, found drivers, a list of found drivers, the
 * possibility to update the available seats, accept offers and to end a trip
 * 
 * @author Andre Nguyen
 * 
 */
public class PassengerViewActivity extends ResourceGroupReadyMapActivity {
    
    private Context context;
    private MapView mapView;
    
    private Handler handler;
    private Handler locationHandler;
    private Timer timer;
    private Timer locationTimer;
    private Controller ctrl;
    
    double lat;
    double lng;
    
    private Button road_info;
    private EditText et_road_info;
    
    
    public PassengerViewActivity() {
        this.context = PassengerViewActivity.this;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengerview);
        
        this.handler = new Handler();
        this.locationHandler = new Handler();
        this.locationTimer = new Timer();
        ViewModel.getInstance().initDriversList();
        ViewModel.getInstance().initRouteList();
        ViewModel.getInstance().resetTimers();
        ViewModel.getInstance().setNewFound();
        
        //        vhikeDialogs.getInstance().getSearchPD(PassengerViewActivity.this).dismiss();
        //        vhikeDialogs.getInstance().clearSearchPD();
        
        setMapView();
        ViewModel.getInstance().getPassengerOverlayList(this.mapView).clear();
        
        this.road_info = (Button) findViewById(R.id.passenger_btn_route_info);
        this.et_road_info = (EditText) findViewById(R.id.passenger_et_route_info);
        ViewModel.getInstance().setRoadInfoBtn(this.road_info);
        ViewModel.getInstance().setRoadInfoEt(this.et_road_info);
        this.road_info.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PassengerViewActivity.this.road_info.setVisibility(View.GONE);
                PassengerViewActivity.this.et_road_info.setVisibility(View.VISIBLE);
            }
        });
        this.et_road_info.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PassengerViewActivity.this.road_info.setVisibility(View.VISIBLE);
                PassengerViewActivity.this.et_road_info.setVisibility(View.GONE);
            }
        });
        
        if (getvHikeRG(this) != null && getLocationRG(this) != null && getContactRG(this) != null) {
            this.ctrl = new Controller(rgvHike);
            ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
            ViewModel.getInstance().setContactRG(rgContact);
            showHitchhikers();
            startQuery();
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
                    PassengerViewActivity.this.ctrl = new Controller(rgvHike);
                    ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
                    ViewModel.getInstance().setContactRG(rgContact);
                    showHitchhikers();
                    startQuery();
                }
            });
        }
        
    }
    
    
    /**
     * adds drivers (hitchhikers) to the notification slider
     */
    private void showHitchhikers() {
        
        ListView pLV = (ListView) findViewById(R.id.ListView_DHitchhikers);
        pLV.setClickable(true);
        pLV.setAdapter(ViewModel.getInstance().getPassengerAdapter(this.context, this.mapView));
    }
    
    
    /**
     * adds hitchhiker/driver to hitchiker list
     * 
     * @param hitchhiker
     */
    public void addHitchhiker(Profile hitchhiker) {
        ViewModel.getInstance().getHitchDrivers().add(hitchhiker);
        ViewModel.getInstance().getPassengerAdapter(this.context, this.mapView).notifyDataSetChanged();
    }
    
    
    /**
     * displays the map from xml file including a button to get current user
     * location
     */
    @SuppressWarnings("deprecation")
    private void setMapView() {
        this.mapView = (MapView) findViewById(R.id.passengerMapView);
        LinearLayout zoomView = (LinearLayout) this.mapView.getZoomControls();
        zoomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        zoomView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        zoomView.setVerticalScrollBarEnabled(true);
        this.mapView.addView(zoomView);
        //        this.mapController = this.mapView.getController();
        
        // check for offers manually
        Button simulation = (Button) findViewById(R.id.Button_SimulateFoundDriver);
        simulation.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                List<OfferObject> loo = PassengerViewActivity.this.ctrl.viewOffers(Model.getInstance()
                        .getSid());
                if (loo != null && loo.size() > 0) {
                    for (int i = 0; i < loo.size(); i++) {
                        Profile driver = PassengerViewActivity.this.ctrl.getProfile(Model.getInstance()
                                .getSid(), loo
                                .get(i).getUser_id());
                        int lat = (int) (loo.get(i).getLat() * 1E6);
                        int lng = (int) (loo.get(i).getLon() * 1E6);
                        GeoPoint gpsDriver = new GeoPoint(lat, lng);
                        
                        ViewModel.getInstance().add2PassengerOverlay(PassengerViewActivity.this.context,
                                gpsDriver,
                                driver, PassengerViewActivity.this.mapView, 1, 0);
                        ViewModel.getInstance().getHitchDrivers().add(driver);
                        ViewModel.getInstance().fireNotification(PassengerViewActivity.this.context, driver,
                                loo.get(i).getUser_id(), 0, PassengerViewActivity.this.mapView, 1);
                        ViewModel
                                .getInstance()
                                .getPassengerAdapter(PassengerViewActivity.this.context,
                                        PassengerViewActivity.this.mapView).notifyDataSetChanged();
                    }
                }
            }
        });
    }
    
    
    /**
     * start query by sending gps, destination and number of needed seats to
     * server
     */
    private void startQuery() {
        //        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //        this.luh = new LocationUpdateHandler(this.context, this.locationManager, this.mapView, this.mapController, 1);
        //        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this.luh);
        
        switch (this.ctrl.startQuery(Model.getInstance().getSid(), ViewModel.getInstance()
                .getDestination4Passenger(),
                ViewModel.getInstance().getMy_lat(), ViewModel.getInstance().getMy_lon(), ViewModel
                        .getInstance()
                        .getNumSeats())) {
            case (Constants.QUERY_ID_ERROR):
                Log.i(this, "Query error");
                break;
            default:
                Log.i(this, "Query started");
                break;
        }
        
        try {
            rgLocation.startLocationLookup(5000, 20.0F);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        Check4Location c4l = new Check4Location(rgvHike, rgLocation, this.mapView, this.context,
                this.locationHandler,
                1);
        this.locationTimer.schedule(c4l, 10000, 10000);
        Log.i(this, "Location started");
        
        // check for offers every 10 seconds
        Check4Offers c4o = new Check4Offers(rgvHike);
        this.timer = new Timer();
        this.timer.schedule(c4o, 300, 10000);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.passengerview_menu, menu);
        return true;
    }
    
    
    private void stopContinousLookup() {
        
        if (this.locationTimer != null) {
            this.locationTimer.cancel();
            ViewModel.getInstance().cancelLocation();
            Log.i(this, "Timer Location cancel");
        }
        
    }
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        switch (this.ctrl.stopQuery(Model.getInstance().getSid(), Model.getInstance().getQueryId())) {
            case Constants.STATUS_QUERY_DELETED:
                Log.i(this, "Query DELETED");
                ViewModel.getInstance().clearPassengerOverlayList();
                ViewModel.getInstance().clearViewModel();
                ViewModel.getInstance().getHitchDrivers().clear();
                ViewModel.getInstance().clearPassengerNotificationAdapter();
                try {
                    ViewModel.getInstance().clearRoutes();
                } catch (NullPointerException e) {
                    
                }
                ViewModel.getInstance().resetRoadInfo();
                this.timer.cancel();
                this.locationTimer.cancel();
                stopContinousLookup();
                
                break;
            case Constants.STATUS_NO_QUERY:
                Log.i(this, "NO QUERY");
                break;
            case Constants.STATUS_INVALID_USER:
                Log.i(this, "INVALID USER");
                break;
            default:
                // stop query
                Log.i(this, "QUERY DELeTED");
                ViewModel.getInstance().clearPassengerOverlayList();
                ViewModel.getInstance().getHitchDrivers().clear();
                //                this.locationManager.removeUpdates(this.luh);
                this.timer.cancel();
                stopContinousLookup();
                
                ViewModel.getInstance().clearViewModel();
                
                PassengerViewActivity.this.finish();
        }
    }
    
    
    @Override
    public void onBackPressed() {
        
        switch (this.ctrl.stopQuery(Model.getInstance().getSid(), Model.getInstance().getQueryId())) {
            case Constants.STATUS_QUERY_DELETED:
                Log.i(this, "Query DELETED");
                ViewModel.getInstance().clearPassengerOverlayList();
                ViewModel.getInstance().getHitchDrivers().clear();
                ViewModel.getInstance().clearViewModel();
                ViewModel.getInstance().clearPassengerNotificationAdapter();
                try {
                    ViewModel.getInstance().clearRoutes();
                } catch (NullPointerException e) {
                    
                }
                ViewModel.getInstance().resetRoadInfo();
                this.timer.cancel();
                this.locationTimer.cancel();
                ViewModel.getInstance().clearViewModel();
                stopContinousLookup();
                
                PassengerViewActivity.this.finish();
                break;
            case Constants.STATUS_NO_QUERY:
                Log.i(this, "NO QUERY");
                break;
            case Constants.STATUS_INVALID_USER:
                Log.i(this, "INVALID USER");
                break;
            default:
                // stop query
                Log.i(this, "QUERY DELeTED");
                ViewModel.getInstance().clearPassengerOverlayList();
                ViewModel.getInstance().getHitchDrivers().clear();
                //                this.locationManager.removeUpdates(this.luh);
                this.timer.cancel();
                ViewModel.getInstance().clearViewModel();
                stopContinousLookup();
                
                PassengerViewActivity.this.finish();
        }
        
        return;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Provi
        switch (item.getItemId()) {
            case R.id.mi_passenger_endTrip:
                switch (this.ctrl.stopQuery(Model.getInstance().getSid(), Model.getInstance().getQueryId())) {
                    case Constants.STATUS_QUERY_DELETED:
                        Log.i(this, "Query DELETED");
                        ViewModel.getInstance().clearPassengerOverlayList();
                        ViewModel.getInstance().getHitchDrivers().clear();
                        ViewModel.getInstance().clearViewModel();
                        ViewModel.getInstance().clearPassengerNotificationAdapter();
                        //                        this.locationManager.removeUpdates(this.luh);
                        stopContinousLookup();
                        
                        this.timer.cancel();
                        this.locationTimer.cancel();
                        ViewModel.getInstance().clearViewModel();
                        
                        PassengerViewActivity.this.finish();
                        break;
                    case Constants.STATUS_NO_QUERY:
                        Log.i(this, "NO QUERY");
                        break;
                    case Constants.STATUS_INVALID_USER:
                        Log.i(this, "INVALID USER");
                        break;
                    default:
                        // stop query
                        Log.i(this, "QUERY DELeTED");
                        ViewModel.getInstance().clearPassengerOverlayList();
                        ViewModel.getInstance().getHitchDrivers().clear();
                        //                        this.locationManager.removeUpdates(this.luh);
                        this.timer.cancel();
                        ViewModel.getInstance().clearViewModel();
                        
                        stopContinousLookup();
                        
                        PassengerViewActivity.this.finish();
                }
                break;
            case R.id.mi_passenger_updateData:
                vhikeDialogs.getInstance().getUpdateDataDialog(rgvHike, this.context, 1).show();
                break;
        }
        return true;
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
