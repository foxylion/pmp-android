package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import java.util.Random;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.Distance;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.LocationResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

/**
 * {@link AbsoluteLocationImpl} is the AIDL implementation for communication with the app.
 * 
 * @author Jakob Jarosch
 */
public class AbsoluteLocationImpl extends IAbsoluteLocation.Stub {
	
	/**
	 * Reference to the {@link LocationResourceGroup}.
	 */
	private LocationResourceGroup locationRG;
	
	/**
	 * Resource which provides access to the GPS location.
	 */
	private AbsoluteLocationResource absoluteLocationR;
	
	/**
	 * Identifier of the app.
	 */
	private String appIdentifier;
	
	/**
	 * Instance of the {@link PermissionValidator}.
	 */
	private PermissionValidator psv;
	
	/**
	 * Used {@link UpdateRequest} to turn GPS on.
	 */
	private UpdateRequest updateRequest = null;
	
	/**
	 * Last used latitude to calculate the random inaccuracy of latitude.
	 */
	private double lastRILatitude = -1000.0;
	
	/**
	 * Last used longitude to calculate the random inaccuracy of longitude.
	 */
	private double lastRILongitude = -1000.0;
	
	/**
	 * Inaccuracy to force a inaccurate position.
	 */
	private double randomInaccuracyLat = 0.0;
	
	/**
	 * Inaccuracy to force a inaccurate position.
	 */
	private double randomInaccuracyLong = 0.0;
	
	/**
	 * Last returned latitude.
	 */
	private double lastLatitude = -1000.0;
	
	/**
	 * Last returned longitude.
	 */
	private double lastLongitude = -1000.0;
	
	/**
	 * Last time when an update was returned.
	 */
	private long lastUpdate = 0;
	
	
	/**
	 * Creates a new instance of the {@link AbsoluteLocationImpl}.
	 * 
	 * @param locationRG
	 *            The instance of the {@link LocationResourceGroup}.
	 * @param absoluteLocationR
	 *            The instance of the {@link AbsoluteLocationResource}.
	 * @param appIdentifier
	 *            The identifier of the app.
	 */
	public AbsoluteLocationImpl(LocationResourceGroup locationRG, AbsoluteLocationResource absoluteLocationR,
			String appIdentifier) {
		this.locationRG = locationRG;
		this.absoluteLocationR = absoluteLocationR;
		this.appIdentifier = appIdentifier;
		this.psv = new PermissionValidator(this.locationRG, this.appIdentifier);
	}
	
	
	public void startLocationLookup(long minTime, float minDistance) throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		
		this.updateRequest = new UpdateRequest(minTime, minDistance);
		this.absoluteLocationR.startLocationLookup(this.appIdentifier, this.updateRequest);
	}
	
	
	public void endLocationLookup() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		
		this.updateRequest = null;
		this.absoluteLocationR.endLocationLookup(this.appIdentifier);
	}
	
	
	public boolean isGpsEnabled() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.isGpsEnabled();
	}
	
	
	public boolean isActive() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.isActive();
	}
	
	
	public boolean isFixed() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.isFixed();
	}
	
	
	public boolean isUpdateAvailable() {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		double currentLatitude = this.absoluteLocationR.getLatitude();
		double currentLongitude = this.absoluteLocationR.getLongitude();
		
		double distance = Distance.calclateArc(this.lastLatitude, this.lastLongitude, currentLatitude,
				currentLongitude, Distance.KILOMETERS);
		
		boolean update = false;
		
		if (distance > this.updateRequest.getMinDistance()) {
			/* Distance to last coordinate is greater than minDistance. */
			update = true;
		} else if (System.currentTimeMillis() > this.lastUpdate + this.updateRequest.getMinTime()) {
			/* Last update is older than minTime. */
			update = true;
		}
		
		if (update) {
			this.lastLatitude = currentLatitude;
			this.lastLongitude = currentLongitude;
			this.lastUpdate = System.currentTimeMillis();
		}
		
		return update;
	}
	
	
	public double getLongitude() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		calculateRandomInaccuracy();
		
		return this.absoluteLocationR.getLongitude() * this.randomInaccuracyLong;
	}
	
	
	public double getLatitude() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		calculateRandomInaccuracy();
		
		return this.absoluteLocationR.getLatitude() * this.randomInaccuracyLat;
	}
	
	
	public float getAccuracy() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		this.psv.validate(LocationResourceGroup.PS_SHOW_ACCURACY, "true");
		updateLastRequest();
		
		if (this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION) > this.absoluteLocationR.getAccuracy()) {
			return this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION);
		} else {
			return this.absoluteLocationR.getAccuracy();
		}
	}
	
	
	public float getSpeed() throws RemoteException {
		this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		this.psv.validate(LocationResourceGroup.PS_SHOW_SPEED, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.getSpeed();
	}
	
	
	private void updateLastRequest() {
		if (this.updateRequest != null) {
			this.updateRequest.setLastRequest(System.currentTimeMillis());
		}
	}
	
	
	/**
	 * Calculates a random inaccuracy which is used to generate a vague location.
	 */
	private void calculateRandomInaccuracy() {
		int precision = this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION);
		
		double newLatitude = this.absoluteLocationR.getLatitude();
		double newLongitude = this.absoluteLocationR.getLongitude();
		
		double distanceToLastPosition = Distance.calclateArc(this.lastRILatitude, this.lastRILongitude, newLatitude,
				newLongitude, Distance.KILOMETERS) / 1000;
		
		/* 
		 * Only change the inaccuracy when the distance to the last inaccuracy change
		 * is greater than the precision Privacy Setting.
		 */
		if (distanceToLastPosition > precision) {
			this.randomInaccuracyLat = new Random().nextDouble() * precision;
			this.randomInaccuracyLong = new Random().nextDouble() * precision;
			this.lastRILatitude = newLatitude;
			this.lastRILongitude = newLongitude;
		}
	}
}
