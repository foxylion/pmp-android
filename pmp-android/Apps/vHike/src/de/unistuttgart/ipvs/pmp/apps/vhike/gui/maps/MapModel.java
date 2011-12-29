package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;

import android.widget.Spinner;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * MapModel grants access to the list of overlays. New elements can be easily
 * added through this model. Future map function may me added later on.
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
}
