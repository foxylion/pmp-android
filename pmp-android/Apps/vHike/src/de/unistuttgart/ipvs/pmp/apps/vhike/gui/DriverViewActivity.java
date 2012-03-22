package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

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
import android.view.ViewGroup;
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
 * DriverViewActivity displays driver with his perimeter, found hitchhikers, a list of found hitchhikers, the
 * possibility to update the available seats, send offers or reject found hitchhikers and to pick up potential
 * passengers and to
 * end a trip
 * 
 * @author Andre Nguyen
 * 
 */
public class DriverViewActivity extends ResourceGroupReadyMapActivity {
    
    private Context context;
    private MapView mapView;
    //    private LocationManager locationManager;
    //    private LocationUpdateHandler luh;
    
    private Handler handler;
    private Handler locationHandler;
    private Handler queryHandler;
    
    private Controller ctrl;
    
    private Timer locationTimer;
    private Timer queryTimer;
    
    private Check4Location c4l;
    private Check4Queries c4q;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverview);
        
        PMP.get(getApplication());
        
        locationHandler = new Handler();
        queryHandler = new Handler();
        handler = new Handler();
        ViewModel.getInstance().initPassengersList();
        ViewModel.getInstance().resetTimers();
        
        vhikeDialogs.getInstance().getAnnouncePD(DriverViewActivity.this).dismiss();
        vhikeDialogs.getInstance().clearAnnouncPD();
        
        setMapView();
        
        if (getvHikeRG(this) != null && getLocationRG(this) != null) {
            ctrl = new Controller(rgvHike);
            ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
            resourceCached();
            showHitchhikers();
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    ctrl = new Controller(rgvHike);
                    ViewModel.getInstance().setvHikeWSRGandCreateController(rgvHike);
                    resourceCached();
                    showHitchhikers();
                }
            });
        }
        
    }
    
    
    public DriverViewActivity() {
        context = DriverViewActivity.this;
    }
    
    
    /**
     * adds passengers (hitchhikers) to the notification slider
     */
    private void showHitchhikers() {
        
        ListView pLV = (ListView) findViewById(R.id.ListView_SearchingHitchhikers);
        pLV.setClickable(true);
        pLV.setAdapter(ViewModel.getInstance().getDriverAdapter(context, mapView));
    }
    
    
    /**
     * adds hitchhiker/passenger to hitchhiker list
     * 
     * @param hitchhiker
     */
    public void addHitchhiker(Profile hitchhiker) {
        ViewModel.getInstance().getHitchPassengers().add(hitchhiker);
        ViewModel.getInstance().getDriverAdapter(context, mapView).notifyDataSetChanged();
    }
    
    
    /**
     * displays the map from xml file and sets the zoom buttons
     */
    @SuppressWarnings("deprecation")
    private void setMapView() {
        mapView = (MapView) findViewById(R.id.driverMapView);
        
        LinearLayout zoomView = (LinearLayout) mapView.getZoomControls();
        
        zoomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        
        zoomView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        zoomView.setVerticalScrollBarEnabled(true);
        mapView.addView(zoomView);
    }
    
    
    private void resourceCached() {
        try {
            rgLocation.startLocationLookup(5000, 20.0F);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startContinousLookup();
    };
    
    
    private void startContinousLookup() {
        locationTimer = new Timer();
        queryTimer = new Timer();
        
        c4l = new Check4Location(rgvHike, rgLocation, mapView, context, locationHandler, 0);
        locationTimer.schedule(c4l, 10000, 10000);
        // Start Check4Queries Class to check for queries
        
        c4q = new Check4Queries(rgvHike, queryHandler);
        queryTimer.schedule(c4q, 10000, 10000);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driverview_menu, menu);
        return true;
    }
    
    
    @Override
    public void onBackPressed() {
        
        switch (ctrl.endTrip(Model.getInstance().getSid(), Model.getInstance().getTripId())) {
            case Constants.STATUS_SUCCESS: {
                
                ViewModel.getInstance().clearDriverOverlayList();
                ViewModel.getInstance().clearViewModel();
                ViewModel.getInstance().clearHitchPassengers();
                ViewModel.getInstance().clearDriverNotificationAdapter();
                //  locationManager.removeUpdates(luh);
                stopContinousLookup();
                
                Log.i(this, "Trip ENDED");
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
        
        return;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_endTrip:
                
                switch (ctrl.endTrip(Model.getInstance().getSid(), Model.getInstance().getTripId())) {
                    case Constants.STATUS_SUCCESS: {
                        
                        ViewModel.getInstance().clearDriverOverlayList();
                        ViewModel.getInstance().clearViewModel();
                        ViewModel.getInstance().clearHitchPassengers();
                        ViewModel.getInstance().clearDriverNotificationAdapter();
                        //  locationManager.removeUpdates(luh);
                        stopContinousLookup();
                        
                        Log.i(this, "Trip ENDED");
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
                vhikeDialogs.getInstance().getUpdateDataDialog(rgvHike, context).show();
                break;
        }
        return true;
    }
    
    
    private void stopContinousLookup() {
        
        if (locationTimer != null) {
            DriverViewActivity.this.locationTimer.cancel();
            ViewModel.getInstance().cancelLocation();
            Log.i(this, "Timer Location cancel");
        }
        
        if (queryTimer != null) {
            DriverViewActivity.this.queryTimer.cancel();
            ViewModel.getInstance().cancelQuery();
            Log.i(this, "Timer Query cancel");
        }
        
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
