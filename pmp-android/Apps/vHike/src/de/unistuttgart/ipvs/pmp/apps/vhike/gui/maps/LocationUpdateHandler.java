package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Handles location updates
 * 
 * @author Andre
 * 
 */
public class LocationUpdateHandler implements LocationListener {

	private Context context;
	private LocationManager locationManager;
	private MapView mapView;
	private MapController mapController;
	private MapOverlay mapOverlay;
	private GeoPoint gPosition;
	private Location location;

	/**
	 * 
	 * @param context
	 * @param locationManager
	 * @param mapView
	 * @param mapController
	 * @param mapOverlay
	 * @param gPosition
	 */
	public LocationUpdateHandler(Context context,
			LocationManager locationManager, MapView mapView,
			MapController mapController, MapOverlay mapOverlay,
			GeoPoint gPosition) {

		this.context = context;
		this.locationManager = locationManager;
		this.mapView = mapView;
		this.mapController = mapController;
		this.mapOverlay = mapOverlay;
		this.gPosition = gPosition;
	}

	public void onLocationChanged(Location location) {
		mapController = mapView.getController();
		int lat = (int) (location.getLatitude() * 1E6);
		int lng = (int) (location.getLongitude() * 1E6);
		gPosition = new GeoPoint(lat, lng);
		mapController.animateTo(gPosition);
		mapController.setCenter(gPosition);
		mapController.setZoom(17);
		mapView.invalidate();

		// add marker
		mapOverlay.setPointToDraw(gPosition);
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		
		showCurrentLocation();
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * show current location as a street name
	 */
	public void showCurrentLocation() {

		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			String address = convertPointToLocation(gPosition);
			Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * convert location point to streetname
	 * 
	 * @param point
	 * @return streetname of current position
	 */
	public String convertPointToLocation(GeoPoint point) {
		StringBuffer address = new StringBuffer();
		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(
					point.getLatitudeE6() / 1E6, point.getLongitudeE6() / 1E6,
					1);

			if (addresses.size() > 0) {
				for (int index = 0; index < addresses.get(0)
						.getMaxAddressLineIndex(); index++)
					// address += addresses.get(0).getAddressLine(index) + " ";
					address.append(address.toString()
							+ addresses.get(0).getAddressLine(index) + " ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return address.toString();
	}

	/**
	 * get longtitude and latitude of current location
	 * @return
	 */
	public int[] getCoordinates() {
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		int lng = (int) (location.getLongitude() * 1E6);
		int lat = (int) (location.getLatitude() * 1E6);
		int coordinates[] = { lat, lng };
		
		return coordinates;
	}
	
}
