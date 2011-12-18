package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.LocationUpdateHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driverview);

		showHitchhikers();
		setMapView();
		startTripByAnnouncing();
	}

	public DriverViewActivity() {
		this.context = DriverViewActivity.this;
	}

	/**
	 * adds passengers (hitchhikers) to the notification slider
	 */
	private void showHitchhikers() {
		hitchhikers = new ArrayList<Profile>();

		Profile profile = new Profile("User1", null, null, null, null, null,
				null, false, false, false, false, lat, 2.5);
		Profile profile2 = new Profile("User3", null, null, null, null, null,
				null, false, false, false, false, lat, 4);
		Profile profile3 = Model.getInstance().getOwnProfile();

		addHitchhiker(profile);
		addHitchhiker(profile2);
		addHitchhiker(profile3);

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
	 * displays the map from xml file including a button to get current user
	 * location
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

	private void startTripByAnnouncing() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new LocationUpdateHandler(context, locationManager, mapView,
						mapController, new MapOverlay(context, p, 1), p));
		Controller ctrl = new Controller();
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			ctrl.tripUpdatePos(Model.getInstance().getSid(), Model
					.getInstance().getTripId(), (float) location.getLatitude(),
					(float) location.getLongitude());
		} else {
			Toast.makeText(DriverViewActivity.this, "FEHLER", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
