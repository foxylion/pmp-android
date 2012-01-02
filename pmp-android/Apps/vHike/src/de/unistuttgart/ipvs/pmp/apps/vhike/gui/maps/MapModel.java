package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Spinner;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

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
}
