package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.content.Context; 
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.LocationUpdateHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapModel;
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
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private GeoPoint p;

	private Controller ctrl;

	private int imADriver = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driverview);

		ctrl = new Controller();
		MapModel.getInstance().initDriversList();

		showHitchhikers();
		setMapView();
		startTripByUpdating();

		vhikeDialogs.getInstance().getAnnouncePD(DriverViewActivity.this)
				.dismiss();
		vhikeDialogs.getInstance().clearAnnouncPD();
	}

	public DriverViewActivity() {
		this.context = DriverViewActivity.this;
	}

	/**
	 * adds passengers (hitchhikers) to the notification slider
	 */
	private void showHitchhikers() {

		ListView pLV = (ListView) findViewById(R.id.ListView_SearchingHitchhikers);
		pLV.setClickable(true);
		pLV.setAdapter(MapModel.getInstance().getDriverAdapter(context));
	}

	/**
	 * adds hitchhiker/passenger to hitchhiker list
	 * 
	 * @param hitchhiker
	 */
	public void addHitchhiker(Profile hitchhiker) {
		MapModel.getInstance().getHitchPassengers().add(hitchhiker);
		MapModel.getInstance().getDriverAdapter(context).notifyDataSetChanged();
	}

	/**
	 * displays the map from xml file including a button to get current user
	 * location
	 */
	@SuppressWarnings("deprecation")
	private void setMapView() {
		mapView = (MapView) findViewById(R.id.driverMapView);
		LinearLayout zoomView = (LinearLayout) mapView.getZoomControls();

		zoomView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		zoomView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		zoomView.setVerticalScrollBarEnabled(true);
		mapView.addView(zoomView);

//		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
	}

	/**
	 * get current location and notify server that a trip was announced for
	 * possible passengers to see
	 */
	private void startTripByUpdating() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new LocationUpdateHandler(context, locationManager, mapView,
						mapController, p, imADriver));
//		Controller ctrl = new Controller();
//		Location location = locationManager
//				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//		if (location != null) {
//			int lat = (int) (location.getLatitude() * 1E6);
//			int lng = (int) (location.getLongitude() * 1E6);
//			GeoPoint gPosition = new GeoPoint(lat, lng);
//
//			Profile me = Model.getInstance().getOwnProfile();
//
//			Drawable drawableDriver = context.getResources().getDrawable(
//					R.drawable.icon_ride);
//			DriverOverlay dOverlay = new DriverOverlay(drawableDriver, context,
//					gPosition);
//
//			OverlayItem oDriverItem = new OverlayItem(gPosition,
//					"Who wants a ride?", "User: " + me.getUsername()
//							+ ", Rating: " + me.getRating_avg());
//			dOverlay.addOverlay(oDriverItem);
//
//			MapModel.getInstance().getDriverOverlayList(mapView).add(dOverlay);
//
//			switch (ctrl.tripUpdatePos(Model.getInstance().getSid(), Model
//					.getInstance().getTripId(), (float) location.getLatitude(),
//					(float) location.getLongitude())) {
//			case Constants.STATUS_UPDATED:
//				Toast.makeText(DriverViewActivity.this, "Status updated",
//						Toast.LENGTH_SHORT).show();
//				// ctrl.searchQuery(Model.getInstance().getSid(),
//				// (float) location.getLatitude(),
//				// (float) location.getLongitude(), 10000);
//				break;
//			case Constants.STATUS_UPTODATE:
//				Toast.makeText(DriverViewActivity.this, "Status Up to date",
//						Toast.LENGTH_SHORT).show();
//				break;
//			case Constants.STATUS_NOTRIP:
//				Toast.makeText(DriverViewActivity.this, "Status no trip",
//						Toast.LENGTH_LONG).show();
//				break;
//			case Constants.STATUS_HASENDED:
//				Toast.makeText(DriverViewActivity.this, "Status trip ended",
//						Toast.LENGTH_LONG).show();
//				break;
//			case Constants.STATUS_INVALID_USER:
//				Toast.makeText(DriverViewActivity.this, "Status invalid user",
//						Toast.LENGTH_LONG).show();
//
//			}
//		} else {
//			Toast.makeText(context, "Location null", Toast.LENGTH_SHORT).show();
//		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.driverview_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (R.id.mi_endTrip):

			switch (ctrl.endTrip(Model.getInstance().getSid(), Model
					.getInstance().getTripId())) {
			case (Constants.STATUS_UPDATED): {
				Toast.makeText(DriverViewActivity.this, "Trip ended",
						Toast.LENGTH_LONG).show();
				MapModel.getInstance().clearDriverOverlayList(mapView);
				MapModel.getInstance().clearHitchPassengers();
				MapModel.getInstance().clearDriverNotificationAdapter();
				mapView = null;
				mapController = null;
				// appsAdapter.notifyDataSetChanged();
				DriverViewActivity.this.finish();
				break;
			}
			case (Constants.STATUS_UPTODATE): {
				Toast.makeText(DriverViewActivity.this, "Up to date",
						Toast.LENGTH_SHORT).show();
				break;
			}
			case (Constants.STATUS_NOTRIP): {
				Toast.makeText(DriverViewActivity.this, "No trip",
						Toast.LENGTH_SHORT).show();
				DriverViewActivity.this.finish();
				break;
			}
			case (Constants.STATUS_HASENDED): {
				Toast.makeText(DriverViewActivity.this, "Trip ended",
						Toast.LENGTH_SHORT).show();
				DriverViewActivity.this.finish();
				break;
			}
			case (Constants.STATUS_INVALID_USER):
				Toast.makeText(DriverViewActivity.this, "Invalid user",
						Toast.LENGTH_SHORT).show();
				break;
			}
			break;

		case (R.id.mi_updateData):
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
