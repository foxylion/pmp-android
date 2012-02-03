package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.Location;

public class AbsoluteLocationResource extends Resource {
	
	private TimerTask timeoutTask = null;
	private Timer timeoutTimer = null;
	
	private Location locationRG;
	
	private LocationManager locationManager;
	
	private LocationListener locationListener = null;
	private GpsStatus.Listener gpsStatusListener = null;
	
	Map<String, UpdateRequest> requests = new HashMap<String, UpdateRequest>();
	
	private boolean gpsEnabled = false;
	private boolean fixed = false;
	
	private double longitude = 0.0;
	private double latitude = 0.0;
	
	private float accuracy = 0.0F;
	private float speed = 0.0F;
	
	private long lastUpdate = 0;
	
	
	public AbsoluteLocationResource(Location locationRG) {
		this.locationRG = locationRG;
		
		locationManager = (LocationManager) this.locationRG.getContext().getSystemService(Context.LOCATION_SERVICE);
	}
	
	
	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new AbsoluteLocationImpl(locationRG, this, appIdentifier);
	}
	
	
	public void startLocationLookup(String appIdentifier, UpdateRequest request) {
		requests.put(appIdentifier, request);
		
		if (locationListener == null) {
			locationListener = new DefaultLocationListener();
			
			timeoutTimer = new Timer();
			timeoutTask = new UpdateRequestVerificator();
			timeoutTimer.schedule(timeoutTask, UpdateRequest.MAX_TIME_BETWEEN_REQUEST,
					UpdateRequest.MAX_TIME_BETWEEN_REQUEST);
		}
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createNotification();
		}
		
		locationManager.removeUpdates(locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, calcMinTime(), calcMinDistance(),
				locationListener);
		locationManager.addGpsStatusListener(gpsStatusListener);
	}
	
	
	public void endLocationLookup(String appIdentifier) {
		requests.remove(appIdentifier);
		
		if (requests.size() == 0 && locationListener != null && gpsStatusListener != null) {
			locationManager.removeGpsStatusListener(gpsStatusListener);
			locationManager.removeUpdates(locationListener);
			locationListener = null;
			gpsStatusListener = null;
			timeoutTimer.cancel();
		}
	}
	
	
	public boolean isGpsEnabled() {
		return gpsEnabled;
	}
	
	public boolean isActive() {
		return (locationListener != null);
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	
	public double getLongitude() {
		return longitude;
	}
	
	
	public double getLatitude() {
		return latitude;
	}
	
	
	public float getAccuracy() {
		return accuracy;
	}
	
	
	public float getSpeed() {
		return speed;
	}
	
	
	private float calcMinDistance() {
		float min = Float.MAX_VALUE;
		
		for (Entry<String, UpdateRequest> request : requests.entrySet()) {
			if (request.getValue().getMinDistance() < min) {
				min = request.getValue().getMinDistance();
			}
		}
		
		return min;
	}
	
	
	private long calcMinTime() {
		long min = Long.MAX_VALUE;
		
		for (Entry<String, UpdateRequest> request : requests.entrySet()) {
			if (request.getValue().getMinTime() < min) {
				min = request.getValue().getMinTime();
			}
		}
		
		return min;
	}
	
	
	private void createNotification() {
		NotificationManager notificationManager = (NotificationManager) this.locationRG.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon_delete, "GPS is disabled, please enable it.", 0);
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.contentIntent = PendingIntent.getActivity(this.locationRG.getContext(), 0, intent, 0);
		notificationManager.notify("gpsDisabledNotification", 0, notification);
	}
	
	class DefaultLocationListener implements LocationListener {
		
		public void onLocationChanged(android.location.Location location) {
			lastUpdate = System.currentTimeMillis();
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			accuracy = location.getAccuracy();
			speed = location.getSpeed();
		}
		
		
		public void onProviderDisabled(String provider) {
			AbsoluteLocationResource.this.gpsEnabled = false;
		}
		
		
		public void onProviderEnabled(String provider) {
			AbsoluteLocationResource.this.gpsEnabled = true;
		}
		
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
				case LocationProvider.AVAILABLE:
					AbsoluteLocationResource.this.fixed = true;
					break;
				
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					AbsoluteLocationResource.this.fixed = false;
					break;
				
				case LocationProvider.OUT_OF_SERVICE:
					AbsoluteLocationResource.this.gpsEnabled = false;
					break;
			}
			
		}
	}
	
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
