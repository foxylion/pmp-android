package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.List;
import java.util.Timer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Check4Offers;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.LocationUpdateHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * PassengerViewActivity displays passengers current location on google maps
 * 
 * @author Andre Nguyen
 * 
 */
public class PassengerViewActivity extends MapActivity {
    
    private Context context;
    private MapView mapView;
    private MapController mapController;
    private LocationManager locationManager;
    private LocationUpdateHandler luh;
    
    private Timer timer;
    private Controller ctrl;
    
    double lat;
    double lng;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengerview);
        
        ctrl = new Controller();
        ViewModel.getInstance().initDriversList();
        
        setMapView();
        showHitchhikers();
        startQuery();
        
        vhikeDialogs.getInstance().getSearchPD(PassengerViewActivity.this).dismiss();
        vhikeDialogs.getInstance().clearSearchPD();
    }
    
    
    public PassengerViewActivity() {
        this.context = PassengerViewActivity.this;
    }
    
    
    /**
     * adds drivers (hitchhikers) to the notification slider
     */
    private void showHitchhikers() {
        
        ListView pLV = (ListView) findViewById(R.id.ListView_DHitchhikers);
        pLV.setClickable(true);
        pLV.setAdapter(ViewModel.getInstance().getPassengerAdapter(context, mapView));
    }
    
    
    /**
     * adds hitchhiker/driver to hitchiker list
     * 
     * @param hitchhiker
     */
    public void addHitchhiker(Profile hitchhiker) {
        ViewModel.getInstance().getHitchDrivers().add(hitchhiker);
        ViewModel.getInstance().getPassengerAdapter(context, mapView).notifyDataSetChanged();
    }
    
    
    /**
     * displays the map from xml file including a button to get current user
     * location
     */
    @SuppressWarnings("deprecation")
    private void setMapView() {
        mapView = (MapView) findViewById(R.id.passengerMapView);
        LinearLayout zoomView = (LinearLayout) mapView.getZoomControls();
        zoomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        zoomView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        zoomView.setVerticalScrollBarEnabled(true);
        mapView.addView(zoomView);
        mapController = mapView.getController();
        
        // check for offers manually
        Button simulation = (Button) findViewById(R.id.Button_SimulateFoundDriver);
        simulation.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                List<OfferObject> loo = ctrl.viewOffers(Model.getInstance().getSid());
                if (loo != null && loo.size() > 0) {
                    for (int i = 0; i < loo.size(); i++) {
                        Profile driver = ctrl.getProfile(Model.getInstance().getSid(), loo.get(i).getUser_id());
                        int lat = (int) (loo.get(i).getLat() * 1E6);
                        int lng = (int) (loo.get(i).getLon() * 1E6);
                        GeoPoint gpsDriver = new GeoPoint(lat, lng);
                        
                        ViewModel.getInstance().add2PassengerOverlay(context, gpsDriver, driver, mapView, 1, 0);
                        ViewModel.getInstance().getHitchDrivers().add(driver);
                        ViewModel.getInstance().fireNotification(context, driver, loo.get(i).getUser_id(), 0, mapView, 1);
                        ViewModel.getInstance().getPassengerAdapter(context, mapView).notifyDataSetChanged();
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        luh = new LocationUpdateHandler(context, locationManager, mapView, mapController);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, luh);
        
        switch (ctrl.startQuery(Model.getInstance().getSid(), ViewModel.getInstance().getDestination(), ViewModel
                .getInstance().getMy_lat(), ViewModel.getInstance().getMy_lon(), ViewModel.getInstance().getNumSeats())) {
            case (Constants.QUERY_ID_ERROR):
                Toast.makeText(PassengerViewActivity.this, "Query error", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(PassengerViewActivity.this, "Query started/updated", Toast.LENGTH_SHORT).show();
                break;
        }
        
        // check for offers every 10 seconds
        Check4Offers c4o = new Check4Offers();
        timer = new Timer();
        timer.schedule(c4o, 300, 10000);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.passengerview_menu, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Provisorisch
        ViewModel.getInstance().clearPassengerOverlayList();
        // Provi
        switch (item.getItemId()) {
            case R.id.mi_passenger_endTrip:
                switch (ctrl.stopQuery(Model.getInstance().getSid(), Model.getInstance().getQueryId())) {
                    case Constants.STATUS_QUERY_DELETED:
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        
                        ViewModel.getInstance().clearPassengerOverlayList();
                        ViewModel.getInstance().clearHitchDrivers();
                        ViewModel.getInstance().clearPassengerNotificationAdapter();
                        locationManager.removeUpdates(luh);
                        timer.cancel();
                        
                        this.finish();
                        break;
                    case Constants.STATUS_NO_QUERY:
                        Toast.makeText(context, "No query", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.STATUS_INVALID_USER:
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.mi_passenger_updateData:
                vhikeDialogs.getInstance().getUpdateDataDialog(context).show();
                break;
        }
        return true;
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
