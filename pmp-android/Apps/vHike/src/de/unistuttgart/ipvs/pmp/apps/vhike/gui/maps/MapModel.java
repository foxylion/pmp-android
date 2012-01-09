package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.SlidingDrawer;
import android.widget.Spinner;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * MapModel grants access to all elements needed to work with the map view
 * 
 * @author andres
 * 
 */
public class MapModel {

	private static MapModel instance;
	private List<Overlay> mapDriverOverlays;
	private List<Overlay> mapPassengerOverlays;
	private String destination;
	private int numSeats = 0;
	private SlidingDrawer slider_Driver;
	private SlidingDrawer slider_Passenger;

	private List<Profile> hitchDrivers;
	private List<Profile> hitchPassengers;

	private NotificationAdapter driverAdapter;
	private NotificationAdapter passengerAdapter;

	public static MapModel getInstance() {
		if (instance == null) {
			instance = new MapModel();
		}
		return instance;
	}

	/**
	 * Holds all overlays of the the drivers Mapview
	 * 
	 * @param mapView
	 * @return
	 */
	public List<Overlay> getDriverOverlayList(MapView mapView) {
		if (mapDriverOverlays == null) {
			mapDriverOverlays = mapView.getOverlays();
		}
		return mapDriverOverlays;
	}

	public void clearDriverOverlayList() {
		if (mapDriverOverlays != null) {
			mapDriverOverlays.clear();
			mapDriverOverlays = null;
		}
	}

	/**
	 * Holds all overlays of the passengers MapView
	 * 
	 * @param mapView
	 * @return
	 */
	public List<Overlay> getPassengerOverlayList(MapView mapView) {
		if (mapPassengerOverlays == null) {
			mapPassengerOverlays = mapView.getOverlays();
		}
		return mapPassengerOverlays;
	}

	public void clearPassengerOverlayList() {
		if (mapPassengerOverlays != null) {
			mapPassengerOverlays.clear();
			mapPassengerOverlays = null;
		}

	}

	/**
	 * set destination in RideActivity
	 * 
	 * @param spDestination
	 */
	public void setDestination(Spinner spDestination) {
		destination = spDestination.getSelectedItem().toString();
	}

	/**
	 * set number of seats available/needed depending on users wishes
	 * 
	 * @param spNumSeats
	 */
	public void setNumSeats(Spinner spNumSeats) {
		numSeats = spNumSeats.getSelectedItemPosition();
		if (numSeats == 0) {
			numSeats = 1;
		}
	}

	/**
	 * get destination set by user
	 * 
	 * @return
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * get number of seats available set by driver or number of seats needed by
	 * a passenger
	 * 
	 * @return
	 */
	public int getNumSeats() {
		return numSeats;
	}

	public void initDriversList() {
		hitchDrivers = new ArrayList<Profile>();
	}

	public void initPassengersList() {
		hitchPassengers = new ArrayList<Profile>();
	}

	/**
	 * list of all drivers who sent an invitation to passengers
	 * 
	 * @return
	 */
	public List<Profile> getHitchDrivers() {
		if (hitchDrivers == null) {
			hitchDrivers = new ArrayList<Profile>();
		}
		return hitchDrivers;
	}

	public void clearHitchDrivers() {
		if (hitchDrivers != null) {
			hitchDrivers.clear();
			hitchDrivers = null;
		}
	}

	/**
	 * list of passengers within perimeter of a driver
	 * 
	 * @return
	 */
	public List<Profile> getHitchPassengers() {
		if (hitchPassengers == null) {
			hitchPassengers = new ArrayList<Profile>();
		}
		return hitchPassengers;
	}

	public void clearHitchPassengers() {
		if (hitchPassengers != null) {
			hitchPassengers.clear();
			hitchPassengers = null;
		}
	}

	/**
	 * Adapter to show found drivers
	 * 
	 * @param context
	 * @return
	 */
	public NotificationAdapter getDriverAdapter(Context context) {
		if (driverAdapter == null) {
			driverAdapter = new NotificationAdapter(context,
					getHitchPassengers(), 0);
		}
		return driverAdapter;
	}

	/**
	 * Adapter to show found passengers
	 * 
	 * @param context
	 * @return
	 */
	public NotificationAdapter getPassengerAdapter(Context context) {
		if (passengerAdapter == null) {
			passengerAdapter = new NotificationAdapter(context,
					getHitchDrivers(), 1);
		}
		return passengerAdapter;
	}

	/**
	 * Simulating notifications per button click if button is pressed slider is
	 * opened and user receives a notification via the status bar
	 * 
	 * @param context
	 * @param profile
	 */
	public void fireNotification(Context context, Profile profile,
			int profileID, int which1, int notiID) {

		// get reference to notificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);

		int icon = 0;
		CharSequence contentTitle;
		CharSequence contentText;
		CharSequence tickerText;

		// instantiate the notification
		if (which1 == 0) {
			icon = R.drawable.passenger_logo;
			contentTitle = profile.getUsername() + " wants a ride!";
			contentText = "Touch to open profile";
			tickerText = "Found passenger!";
		} else {
			icon = R.drawable.icon_ride;
			contentTitle = profile.getUsername() + " says: Hop on in!";
			contentText = "Touch to open profile";
			tickerText = "Found driver!";
		}

		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND;

		// define the notification's message and PendingContent
		Intent notificationIntent = new Intent(context, ProfileActivity.class);
		notificationIntent.putExtra("MY_PROFILE", 1);
		Log.i("ProfileID: " + profileID);
		notificationIntent.putExtra("PASSENGER_ID", profileID);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// pass notification to notificationManager
		mNotificationManager.notify(notiID, notification);

		if (which1 == 0) {
			// getHitchPassengers().clear();
			// getHitchPassengers().add(profile);

			slider_Driver = (SlidingDrawer) ((Activity) context)
					.findViewById(R.id.notiSlider);
			slider_Driver.open();
			MapModel.getInstance().getDriverAdapter(context)
					.notifyDataSetChanged();
		} else {
			// getHitchDrivers().clear();
			// getHitchDrivers().add(profile);
			slider_Passenger = (SlidingDrawer) ((Activity) context)
					.findViewById(R.id.slidingDrawer);
			slider_Passenger.open();
			MapModel.getInstance().getPassengerAdapter(context)
					.notifyDataSetChanged();
		}

	}

	/**
	 * add a Passenger to DriverOverlay
	 * 
	 * @param context
	 * @param gpsPassenger
	 * @param passenger
	 * @param mapView
	 */
	public void addPassenger2Overlay(Context context, GeoPoint gpsPassenger,
			Profile passenger, MapView mapView) {
		Drawable drawablePassenger = context.getResources().getDrawable(
				R.drawable.passenger_logo);
		PassengerOverlay passengerOverlay = new PassengerOverlay(
				drawablePassenger, context);

		OverlayItem opPassengerItem = new OverlayItem(gpsPassenger,
				"I need a ride", "User: " + passenger.getUsername()
						+ ", Rating: " + passenger.getRating_avg());
		passengerOverlay.addOverlay(opPassengerItem);

		// add found passenger to overlay
		getDriverOverlayList(mapView).add(passengerOverlay);
		mapView.invalidate();
	}

	/**
	 * add an driver to Overlay
	 * 
	 * @param context
	 * @param gpsDriver
	 * @param driver
	 * @param mapView
	 */
	public void addDriver2Overlay(Context context, GeoPoint gpsDriver,
			Profile driver, MapView mapView) {
		Drawable drawableDriver = context.getResources().getDrawable(
				R.drawable.icon_ride);
		DriverOverlay driverOverlay = new DriverOverlay(drawableDriver,
				context, gpsDriver);

		OverlayItem opDriverItem = new OverlayItem(gpsDriver, "Hop in man",
				"User: " + driver.getUsername() + ", Rating: "
						+ driver.getRating_avg());
		driverOverlay.addOverlay(opDriverItem);

		MapModel.getInstance().getPassengerOverlayList(mapView)
				.add(driverOverlay);
		mapView.invalidate();
	}

}
