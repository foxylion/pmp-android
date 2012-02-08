package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.io.IOException;  
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

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
	private GeoPoint gPosition;
	private Location location;
	private int mWhichHitcher;
	private int notiID = 0;

	private Controller ctrl;

	long GPSupdateInterval; // In milliseconds
	float GPSmoveInterval; // In meters

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
			MapController mapController, GeoPoint gPosition, int whichHitcher) {
		this.context = context;
		this.locationManager = locationManager;
		this.mapView = mapView;
		this.mapController = mapController;
		this.gPosition = gPosition;
		mWhichHitcher = whichHitcher;

		ctrl = new Controller();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				GPSupdateInterval, GPSmoveInterval, (LocationListener) this);
	}

	@SuppressWarnings("deprecation")
	public void onLocationChanged(Location location) {
		/**
		 * draw an overlay for driver or passenger
		 */
		mapController = mapView.getController();

		int lat = (int) (location.getLatitude() * 1E6);
		int lng = (int) (location.getLongitude() * 1E6);
		gPosition = new GeoPoint(lat, lng);

		Profile me = Model.getInstance().getOwnProfile();

		// 0, driver is asking for his current location
		// 1, passenger is asking for his current location
		if (mWhichHitcher == 0) {

			// clear list first and draw everything new
			MapModel.getInstance().clearDriverOverlayList();
			MapModel.getInstance().getHitchPassengers().clear();
			notiID = 0;

			// draw me in
			MapModel.getInstance().add2DriverOverlay(context, gPosition, me,
					mapView, 0, 0);

			/**
			 * send server updated latitude and longitude
			 */
			switch (ctrl.tripUpdatePos(Model.getInstance().getSid(), Model
					.getInstance().getTripId(), (float) location.getLatitude(),
					(float) location.getLongitude())) {
			case Constants.STATUS_UPDATED:
				Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT)
						.show();

				/**
				 * search for passenger within perimeter (10 km for testing
				 * purposes)
				 */
				List<QueryObject> lqo = ctrl.searchQuery(Model.getInstance()
						.getSid(), (float) location.getLatitude(),
						(float) location.getLongitude(), 10000);
				if (lqo != null) {
					for (int i = 0; i < lqo.size(); i++) {
						int lati = (int) (lqo.get(i).getCur_lat() * 1E6);
						int lngi = (int) (lqo.get(i).getCur_lon() * 1E6);
						GeoPoint gpsPassenger = new GeoPoint(lati, lngi);

						// create Profile of found passenger
						Profile passenger = ctrl.getProfile(Model.getInstance()
								.getSid(), lqo.get(i).getUserid());

						if (!Model.getInstance().isInBannList(
								lqo.get(i).getUserid())) {
							// add an passenger to overlay
							MapModel.getInstance().add2DriverOverlay(context,
									gpsPassenger, passenger, mapView, 1, lqo.get(i).getUserid());

							// add up ID for statusbar notification
							notiID++;

							// add passenger to slider list
							MapModel.getInstance().getHitchPassengers()
									.add(passenger);
							// notify user
							MapModel.getInstance().fireNotification(context,
									passenger, lqo.get(i).getUserid(), 0,
									notiID, mapView);
							// notify list
							MapModel.getInstance()
									.getDriverAdapter(context, mapView)
									.notifyDataSetChanged();
							mapView.invalidate();
							mapView.postInvalidate();
						}

					}
				} else {
					Toast.makeText(context, "No hitchhikers within perimeter",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.STATUS_UPTODATE:
				Toast.makeText(context, "Status up to date", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.STATUS_NOTRIP:
				Toast.makeText(context, "Status no trip ", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.STATUS_HASENDED:
				Toast.makeText(context, "Status trip ended", Toast.LENGTH_LONG)
						.show();
				break;
			case Constants.STATUS_INVALID_USER:
				Toast.makeText(context, "Status invalid user",
						Toast.LENGTH_LONG).show();

			}
		} else {
			// clear list
			MapModel.getInstance().clearPassengerOverlayList();
			MapModel.getInstance().getHitchDrivers().clear();
			notiID = 0;

			// draw me in
			MapModel.getInstance().add2PassengerOverlay(context, gPosition, me,
					mapView, 0, 0);

			switch (ctrl.userUpdatePos(Model.getInstance().getSid(),
					(float) location.getLatitude(),
					(float) location.getLongitude())) {
			case (Constants.STATUS_UPDATED):
				List<OfferObject> loo = ctrl.viewOffers(Model.getInstance()
						.getSid());
				if (loo != null) {
					for (int i = 0; i < loo.size(); i++) {
						Profile driver = ctrl.getProfile(Model.getInstance()
								.getSid(), loo.get(i).getUser_id());

						GeoPoint gpsDriver = new GeoPoint(
								(int) (location.getLatitude() * 1E6),
								(int) (location.getLongitude() * 1E6));

						// notiID
						notiID++;

						MapModel.getInstance().add2PassengerOverlay(context,
								gpsDriver, driver, mapView, 1, 0);
						MapModel.getInstance().getHitchDrivers().add(driver);
						MapModel.getInstance().fireNotification(context,
								driver, loo.get(i).getUser_id(), 1, notiID,
								mapView);
						MapModel.getInstance()
								.getPassengerAdapter(context, mapView)
								.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(context, "OfferObject null",
							Toast.LENGTH_SHORT).show();
				}

				Toast.makeText(context, "STATUS_UPDATED", Toast.LENGTH_SHORT)
						.show();
				break;
			case Constants.STATUS_UPTODATE:
				Toast.makeText(context, "NO UPDATE, UP 2 DATE",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.STATUS_ERROR:
				Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

		mapController = mapView.getController();
		mapController.setZoom(17);
		mapController.animateTo(gPosition);
		mapController.setCenter(gPosition);
		mapView.invalidate();

		showCurrentLocation();

	}

	public void onProviderDisabled(String provider) {
		locationManager.removeUpdates(this);
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

}
