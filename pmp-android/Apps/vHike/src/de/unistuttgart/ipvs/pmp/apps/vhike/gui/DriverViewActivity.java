package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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
import com.google.android.maps.Overlay;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * DriverViewActivity displays drivers current location on google maps
 * 
 * @author Andre Nguyen
 * 
 */
public class DriverViewActivity extends MapActivity {

	private List<Profile> hitchhikers;
	private MapController mapController;
	private MapView mapView;
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

	/**
	 * adds passengers (hitchhikers) to the notification slider
	 */
	private void showHitchhikers() {
		hitchhikers = new ArrayList<Profile>();

//		addHitchhiker(new Profile("bestHitchhiker", "Hitch", "Hiker",
//				"hitch@hiker.com", "I'm living a gangster Life", "successfull",
//				5, new Date()));

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
	 * set map view
	 */
	private void setMapView() {
		mapView = (MapView) findViewById(R.id.driverMapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();

		// set NewYork-Coordiantes for startup
		String coordinates[] = { "40.717859", "-74.005451" };
		lat = Double.parseDouble(coordinates[0]);
		lng = Double.parseDouble(coordinates[1]);
		p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		// move to coordinates
		mapController.animateTo(p);
		String currentLoc = String.format("Current Location \n Longitude: "
				+ lng + "\n Latitude: " + lat);
		Toast.makeText(DriverViewActivity.this, currentLoc, Toast.LENGTH_LONG)
				.show();

		Button btnDriverLocation = (Button) findViewById(R.id.Button_DriverLocation);
		btnDriverLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0,
						new LocationUpdateHandler());

				showCurrentLocation();
			}
		});
	}

	private void showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			String address = ConvertPointToLocation(p);
			Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * Konvertieren von "Point" zu Stra�e
	 * 
	 * @param point
	 * @return
	 */
	public String ConvertPointToLocation(GeoPoint point) {
		String address = "";
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(
					point.getLatitudeE6() / 1E6, point.getLongitudeE6() / 1E6,
					1);

			if (addresses.size() > 0) {
				for (int index = 0; index < addresses.get(0)
						.getMaxAddressLineIndex(); index++)
					address += addresses.get(0).getAddressLine(index) + " ";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return address;
	}

	public class LocationUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			mapController = mapView.getController();
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			p = new GeoPoint(lat, lng);
			mapController.animateTo(p);
			mapController.setCenter(p);
			mapController.setZoom(17);
			mapView.invalidate();

			// add marker
			MapOverlay mapOverlay = new MapOverlay();
			mapOverlay.setPointToDraw(p);
			List<Overlay> listOfOverlays = mapView.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	/**
	 * Overlay zur den Marker
	 * 
	 * @author Andre
	 * 
	 */
	class MapOverlay extends Overlay {
		private GeoPoint pointToDraw;

		public void setPointToDraw(GeoPoint point) {
			pointToDraw = point;
		}

		public GeoPoint getPointToDraw() {
			return pointToDraw;
		}

		/**
		 * eigene Draw-Implementierung
		 */
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// convert point to pixels
			Point screenPts = new Point();
			mapView.getProjection().toPixels(pointToDraw, screenPts);

			// add marker
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_ride);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 24, null);
			return true;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
