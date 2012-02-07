package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.LocationResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.R;

/**
 * The {@link AbsoluteLocationResource} provides access to the android GPS location module.
 * 
 * @author Jakob Jarosch
 */
public class AbsoluteLocationResource extends Resource {
	
	/**
	 * Timer for checking the usage of the resource.
	 */
	private Timer timeoutTimer = null;
	
	/**
	 * Reference to the {@link LocationResourceGroup}.
	 */
	private LocationResourceGroup locationRG;
	
	/**
	 * Location manager which provides access to the GPS location.
	 */
	private LocationManager locationManager;
	
	/**
	 * {@link LocationListener} to fetch informations from the GPS module.
	 */
	private LocationListener locationListener = null;
	
	/**
	 * Map holds all current requests.
	 */
	Map<String, UpdateRequest> requests = new HashMap<String, UpdateRequest>();
	
	/**
	 * Boolean is set to true when GPS is enabled.
	 */
	private boolean gpsEnabled = false;
	
	/**
	 * Current latitude.
	 */
	private double latitude = -1000.0;
	
	/**
	 * Current longitude.
	 */
	private double longitude = -1000.0;
	
	/**
	 * Current accuracy.
	 */
	private float accuracy = 0.0F;
	
	/**
	 * Current speed.
	 */
	private float speed = 0.0F;
	
	/**
	 * Is set to true when the GPS signal is fixed.
	 */
	private boolean fixed = false;
	
	/**
	 * Broadcast intent action indicating that the GPS has either started or stopped receiving GPS
	 * fixes. An intent extra provides this state as a boolean, where {@code true} means that the
	 * GPS is actively receiving fixes.
	 * 
	 * @see #EXTRA_ENABLED
	 */
	public static final String GPS_FIX_CHANGE_ACTION = "android.location.GPS_FIX_CHANGE";
	
	/**
	 * The lookup key for a boolean that indicates whether GPS is enabled or disabled. {@code true}
	 * means GPS is enabled. Retrieve it with
	 * {@link android.content.Intent#getBooleanExtra(String,boolean)}.
	 */
	public static final String EXTRA_ENABLED = "enabled";
	
	/**
	 * A {@link BroadcastReceiver} to detect if the GPS module has a fix on the current location.
	 */
	private BroadcastReceiver receiver = new DefaultBroadcastReceiver();
	
	
	/**
	 * Create a new instance of the {@link AbsoluteLocationImpl}.
	 * 
	 * @param locationRG
	 *            Reference to the {@link LocationResourceGroup}.
	 */
	public AbsoluteLocationResource(LocationResourceGroup locationRG) {
		this.locationRG = locationRG;
		
		locationManager = (LocationManager) this.locationRG.getContext().getSystemService(Context.LOCATION_SERVICE);
	}
	
	
	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new AbsoluteLocationImpl(locationRG, this, appIdentifier);
	}
	
	
	/**
	 * Starts a new location lookup.
	 * 
	 * @param appIdentifier
	 *            Identifier of the App.
	 * @param request
	 *            Used UpdateRequest with details about minTime and minDistance.
	 */
	public void startLocationLookup(String appIdentifier, UpdateRequest request) {
		requests.put(appIdentifier, request);
		
		/* Create new locationListener, and timer if not already done. */
		if (locationListener == null) {
			locationListener = new DefaultLocationListener();
			
			timeoutTimer = new Timer();
			timeoutTimer.schedule(new UpdateRequestVerificator(), UpdateRequest.MAX_TIME_BETWEEN_REQUEST,
					UpdateRequest.MAX_TIME_BETWEEN_REQUEST);
		}
		
		/* If the GPS is not already enabled, create a notification. */
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createNotification();
			gpsEnabled = false;
		} else {
			gpsEnabled = true;
		}
		
		/* Register the BroadcastReceiver for detecting a GPS location fix. */
		this.locationRG.getContext().registerReceiver(receiver, new IntentFilter(GPS_FIX_CHANGE_ACTION));
		
		/* Start the request for location updates in a handler-thread to prevent exceptions. */
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			
			public void run() {
				locationManager.removeUpdates(locationListener);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, calcMinTime(), calcMinDistance(),
						locationListener);
			}
		});
		
	}
	
	
	/**
	 * Ends the location lookup for a specific app.
	 * 
	 * @param appIdentifier
	 *            App which do not require any more location lookups.
	 */
	public void endLocationLookup(String appIdentifier) {
		requests.remove(appIdentifier);
		
		/* When no more Apps are listening for the location, stop updates. */
		if (requests.size() == 0 && locationListener != null) {
			locationManager.removeUpdates(locationListener);
			this.locationRG.getContext().unregisterReceiver(receiver);
			locationListener = null;
			timeoutTimer.cancel();
			gpsEnabled = false;
			accuracy = 0.0F;
			speed = 0.0F;
			longitude = -1000.0;
			latitude = -1000.0;
			fixed = false;
		}
	}
	
	
	/**
	 * @return Returns whether GPS is enabled or not.
	 */
	public boolean isGpsEnabled() {
		return gpsEnabled;
	}
	
	
	/**
	 * @return Returns whether GPS location lookup is active or not.
	 */
	public boolean isActive() {
		return (locationListener != null);
	}
	
	
	/**
	 * @return Returns whether the GPS has a fixed location or not.
	 */
	public boolean isFixed() {
		return fixed;
	}
	
	
	/**
	 * @return Returns the current longitude. Or -1000.0 If there was no previous fix.
	 */
	public double getLongitude() {
		return longitude;
	}
	
	
	/**
	 * @return Returns the current latitude. Or -1000.0 If there was no previous fix.
	 */
	public double getLatitude() {
		return latitude;
	}
	
	
	/**
	 * @return Returns the current accuracy.
	 */
	public float getAccuracy() {
		return accuracy;
	}
	
	
	/**
	 * @return Returns the current speed.
	 */
	public float getSpeed() {
		return speed;
	}
	
	
	/**
	 * @return Calculates the minimal distance of all update requests.
	 */
	private float calcMinDistance() {
		float min = Float.MAX_VALUE;
		
		for (Entry<String, UpdateRequest> request : requests.entrySet()) {
			if (request.getValue().getMinDistance() < min) {
				min = request.getValue().getMinDistance();
			}
		}
		
		return min;
	}
	
	
	/**
	 * @return Calculates the minimal time of all update requests.
	 */
	private long calcMinTime() {
		long min = Long.MAX_VALUE;
		
		for (Entry<String, UpdateRequest> request : requests.entrySet()) {
			if (request.getValue().getMinTime() < min) {
				min = request.getValue().getMinTime();
			}
		}
		
		return min;
	}
	
	
	/**
	 * Creates a new notification.
	 */
	private void createNotification() {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pIntent = PendingIntent.getActivity(this.locationRG.getContext(), 0, intent, 0);
		
		Notification notification = new Notification(R.drawable.pmp_rg_location_error, locationRG.getContext()
				.getString(R.string.pmp_rg_location_notification_infotext), System.currentTimeMillis());
		notification.setLatestEventInfo(this.locationRG.getContext(),
				locationRG.getContext().getString(R.string.pmp_rg_location_notification_title), locationRG.getContext()
						.getString(R.string.pmp_rg_location_notification_description), pIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.vibrate = new long[] { 250, 250, 250 };
		
		NotificationManager notificationManager = (NotificationManager) this.locationRG.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notificationManager.notify("gpsDisabledNotification", 0, notification);
	}
	
	
	/**
	 * Hides the notification.
	 */
	private void removeNotification() {
		NotificationManager notificationManager = (NotificationManager) this.locationRG.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notificationManager.cancel("gpsDisabledNotification", 0);
	}
	
	/**
	 * {@link DefaultLocationListener} used to receive updates from the {@link LocationManager}.
	 * 
	 * @author Jakob Jarosch
	 */
	class DefaultLocationListener implements LocationListener {
		
		public void onLocationChanged(android.location.Location location) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			accuracy = location.getAccuracy();
			speed = location.getSpeed();
			fixed = true;
		}
		
		
		public void onProviderDisabled(String provider) {
			AbsoluteLocationResource.this.gpsEnabled = false;
			AbsoluteLocationResource.this.fixed = false;
			createNotification();
		}
		
		
		public void onProviderEnabled(String provider) {
			AbsoluteLocationResource.this.gpsEnabled = true;
			removeNotification();
		}
		
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			/* Not required. */
		}
	}
	
	/**
	 * {@link DefaultBroadcastReceiver} to receive changes of the gps fix state.
	 * 
	 * @author Jakob Jarosch
	 */
	class DefaultBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			fixed = intent.getBooleanExtra(EXTRA_ENABLED, false);
		}
	}
	
	/**
	 * {@link UpdateRequestVerificator} task is used to check if App has not recently requested any
	 * update and can be removed from the list of active requests.
	 * 
	 * @author Jakob Jarosch
	 */
	class UpdateRequestVerificator extends TimerTask {
		
		public void run() {
			for (Entry<String, UpdateRequest> request : AbsoluteLocationResource.this.requests.entrySet()) {
				if (request.getValue().isOutdated()) {
					AbsoluteLocationResource.this.endLocationLookup(request.getKey());
				}
			}
		}
	}
}
