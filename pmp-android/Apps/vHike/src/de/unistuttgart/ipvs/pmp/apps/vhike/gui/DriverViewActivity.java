package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.LocationUpdateHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * DriverViewActivity displays drivers current location on google maps
 * 
 * @author Andre Nguyen
 * 
 */
public class DriverViewActivity extends MapActivity {

	private Context context;
	private List<Profile> hitchhikers;
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private GeoPoint p;

	double lat;
	double lng;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driverview);

		showHitchhikers();
		setMapView();
	}

	public DriverViewActivity() {
		this.context = DriverViewActivity.this;
	}

	/**
	 * adds passengers (hitchhikers) to the notification slider
	 */
	private void showHitchhikers() {
		hitchhikers = new ArrayList<Profile>();

		Profile profile = new Profile("User1", null, null, null, null, null, null, false, false, false, false, lat, lat);
		addHitchhiker(profile);

		ListView pLV = (ListView) findViewById(R.id.ListView_SearchingHitchhikers);
		pLV.setClickable(true);

		NotificationAdapter appsAdapter = new NotificationAdapter(this,
				hitchhikers);
		pLV.setAdapter(appsAdapter);
	}

	/**
	 * adds hitchhiker/passenger to hitchiker list
	 * 
	 * @param hitchhiker
	 */
	private void addHitchhiker(Profile hitchhiker) {
		hitchhikers.add(hitchhiker);
	}

	/**
	 * displays the map from xml file including a button to get current user location
	 */
	private void setMapView() {
		mapView = (MapView) findViewById(R.id.driverMapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();

		Button btnDriverLocation = (Button) findViewById(R.id.Button_DriverLocation);
		btnDriverLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0,
						new LocationUpdateHandler(context, locationManager,
								mapView, mapController, new MapOverlay(context,
										p, 1), p));

			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
