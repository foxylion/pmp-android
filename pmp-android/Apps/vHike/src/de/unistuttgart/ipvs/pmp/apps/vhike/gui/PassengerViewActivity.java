package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

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
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.DriverOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.LocationUpdateHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.PassengerOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
		setUpNotiBar();
		startQuery();

		vhikeDialogs.getInstance().getSearchPD(PassengerViewActivity.this)
				.dismiss();
		vhikeDialogs.getInstance().clearSearchPD();
	}

	public PassengerViewActivity() {
		this.context = this;
	}

	/**
	 * adds drivers (hitchhikers) to the notification slider
	 */
	private void showHitchhikers() {

		ListView pLV = (ListView) findViewById(R.id.ListView_DHitchhikers);
		pLV.setClickable(true);

		appsAdapter = new NotificationAdapter(this, MapModel.getInstance()
				.getHitchDrivers(), imAPassenger);
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
	}

	private void setUpNotiBar() {

		Profile driver = new Profile("Driver1", null, null, null, null, null,
				null, false, false, false, false, 4.5, 5);
		float lat = 37.4230182f;
		float lng = -122.0840848f;
		GeoPoint gps = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		Drawable drawable = context.getResources().getDrawable(
				R.drawable.icon_ride);
		DriverOverlay dOverlay = new DriverOverlay(drawable, context, gps);

		OverlayItem oItem = new OverlayItem(gps, "Who wants a ride?", "User: "
				+ driver.getUsername() + ", Rating: " + driver.getRating_avg());
		dOverlay.addOverlay(oItem);
		MapModel.getInstance().getPassengerOverlayList(mapView).add(dOverlay);
		mapView.invalidate();

		// get reference to notificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		// instantiate the notification
		int icon = R.drawable.icon_ride;
		CharSequence tickerText = "You got an invitation!";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND;

		// define the notification's message and PendingContent
		Context context = getApplicationContext();

		CharSequence contentTitle = driver.getUsername() + ": Need a ride?";
		CharSequence contentText = "Touch to open profile";
		Intent notificationIntent = new Intent(PassengerViewActivity.this,
				ProfileActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
				PassengerViewActivity.this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// pass notification to notificationManager
		final int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);

		// drawer = (SlidingDrawer) findViewById(R.id.notiSlider);
		// drawer.open();
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
