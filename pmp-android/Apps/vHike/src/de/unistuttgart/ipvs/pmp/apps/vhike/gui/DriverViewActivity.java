package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
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

	private SlidingDrawer drawer;

	double lat;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_driverview);
		showHitchhikers();
		setMapView();
		setUpNotiBar();
		startTripByAnnouncing();

		vhikeDialogs.getInstance().getAnnouncePD(DriverViewActivity.this)
				.dismiss();
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
	 * adds hitchhiker/passenger to hitchhiker list
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

	/**
	 * Simulating notifications per button click if button is pressed slider is
	 * opened and user receives a notification via the status bar
	 */
	private void setUpNotiBar() {
		Button notiButton = (Button) findViewById(R.id.Button_SimulateNoti);
		notiButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// // add marker
				// SearchingHitchhikers hitch2Draw = new
				// SearchingHitchhikers(DriverViewActivity.this);
				// // hitch2Draw.setPointToDraw(gPosition);
				// List<Overlay> listOfOverlays = mapView.getOverlays();
				// listOfOverlays.clear();
				// listOfOverlays.add(hitch2Draw);

				// get reference to notificationManager
				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

				// instantiate the notification
				int icon = R.drawable.passenger_logo;
				CharSequence tickerText = "Found hitchhiker!";
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText,
						when);
				notification.defaults |= Notification.DEFAULT_SOUND;

				// define the notification's message and PendingContent
				Context context = getApplicationContext();
				Profile pro = new Profile("bestHitcher", ns, ns, ns, ns, ns,
						null, false, false, false, false, lat, lat);
				CharSequence contentTitle = pro.getUsername()
						+ " wants a ride!";
				CharSequence contentText = "Touch to open profile";
				Intent notificationIntent = new Intent(DriverViewActivity.this,
						ProfileActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						DriverViewActivity.this, 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle,
						contentText, contentIntent);

				// pass notification to notificationManager
				final int HELLO_ID = 1;

				mNotificationManager.notify(HELLO_ID, notification);

				drawer = (SlidingDrawer) findViewById(R.id.notiSlider);
				drawer.open();
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
			switch (ctrl.tripUpdatePos(Model.getInstance().getSid(), Model
					.getInstance().getTripId(), (float) location.getLatitude(),
					(float) location.getLongitude())) {
			case Constants.STATUS_UPDATED:
				Toast.makeText(DriverViewActivity.this, "Status updated",
						Toast.LENGTH_LONG).show();
				break;
			case Constants.STATUS_UPTODATE:
				Toast.makeText(DriverViewActivity.this, "Status Up to date",
						Toast.LENGTH_LONG).show();
				break;
			case Constants.STATUS_NOTRIP:
				Toast.makeText(DriverViewActivity.this, "Status no trip",
						Toast.LENGTH_LONG).show();
				break;
			case Constants.STATUS_HASENDED:
				Toast.makeText(DriverViewActivity.this, "Status trip ended",
						Toast.LENGTH_LONG).show();
				break;
			case Constants.STATUS_INVALID_USER:
				Toast.makeText(DriverViewActivity.this, "Status invalid user",
						Toast.LENGTH_LONG).show();

			}

		} else {
			Toast.makeText(DriverViewActivity.this,
					"Location could not be updated", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
