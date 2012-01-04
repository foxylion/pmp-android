package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.LocationUpdateHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.PassengerOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * PassengerViewActivity displays passengers current location on google maps
 * 
 * @author Andre Nguyen
 * 
 */
public class PassengerViewActivity extends MapActivity {

	private Controller ctrl;
	private NotificationAdapter appsAdapter;

	private Context context;
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private GeoPoint p;

	// private SlidingDrawer drawer;

	double lat;
	double lng;

	private int imAPassenger = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passengerview);

		ctrl = new Controller();
		MapModel.getInstance().initPassengersList();

		showHitchhikers();
		setMapView();
		startQuery();

		vhikeDialogs.getInstance().getSearchPD(PassengerViewActivity.this)
				.dismiss();
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

		appsAdapter = MapModel.getInstance().getPassengerAdapter(context);
		pLV.setAdapter(appsAdapter);
	}

	/**
	 * adds hitchhiker/driver to hitchiker list
	 * 
	 * @param hitchhiker
	 */
	public void addHitchhiker(Profile hitchhiker) {
		MapModel.getInstance().getHitchDrivers().add(hitchhiker);
		appsAdapter.notifyDataSetChanged();
	}

	/**
	 * displays the map from xml file including a button to get current user
	 * location
	 */
	private void setMapView() {
		mapView = (MapView) findViewById(R.id.passengerMapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();

		Button simulation = (Button) findViewById(R.id.Button_SimulateFoundDriver);
		simulation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<OfferObject> loo = ctrl.viewOffers(Model.getInstance()
						.getSid());
				if (loo != null && loo.size() > 0) {
					for (int i = 0; i < loo.size(); i++) {
						Profile driver = ctrl.getProfile(Model.getInstance()
								.getSid(), loo.get(i).getUser_id());
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
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new LocationUpdateHandler(context, locationManager, mapView,
						mapController, p, imAPassenger));
		Controller ctrl = new Controller();
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);

			Profile me = Model.getInstance().getOwnProfile();
			GeoPoint gPosition = new GeoPoint(lat, lng);

			// Passenger drawable and overlay
			Drawable drawablePassenger = context.getResources().getDrawable(
					R.drawable.passenger_logo);
			PassengerOverlay pOverlay = new PassengerOverlay(drawablePassenger,
					context);

			OverlayItem oPassengerItem = new OverlayItem(gPosition,
					"I need a ride!", "User: " + me.getUsername()
							+ ", Rating: " + me.getRating_avg());
			pOverlay.addOverlay(oPassengerItem);

			MapModel.getInstance().getPassengerOverlayList(mapView)
					.add(pOverlay);

			switch (ctrl.startQuery(Model.getInstance().getSid(), MapModel
					.getInstance().getDestination(), (float) location
					.getLatitude(), (float) location.getLongitude(), MapModel
					.getInstance().getNumSeats())) {
			case (Constants.QUERY_ID_ERROR):
				Toast.makeText(PassengerViewActivity.this, "Query error",
						Toast.LENGTH_LONG).show();
				break;
			default:
				Toast.makeText(PassengerViewActivity.this,
						"Query started/updated", Toast.LENGTH_SHORT).show();
				break;
			}
		} else {
			Toast.makeText(context, "Location null", Toast.LENGTH_SHORT).show();
		}
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
		MapModel.getInstance().clearPassengerOverlayList();
		// Provi
		switch (item.getItemId()) {
		case R.id.mi_passenger_endTrip:
			switch (ctrl.stopQuery(Model.getInstance().getSid(), Model
					.getInstance().getQueryId())) {
			case Constants.STATUS_QUERY_DELETED:
				Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
				MapModel.getInstance().clearPassengerOverlayList();
				MapModel.getInstance().getHitchDrivers().clear();
				appsAdapter.notifyDataSetChanged();
				PassengerViewActivity.this.finish();
				break;
			case Constants.STATUS_NO_QUERY:
				Toast.makeText(context, "No query", Toast.LENGTH_SHORT).show();
				break;
			case Constants.STATUS_INVALID_USER:
				Toast.makeText(context, "Invalid user", Toast.LENGTH_SHORT)
						.show();
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
