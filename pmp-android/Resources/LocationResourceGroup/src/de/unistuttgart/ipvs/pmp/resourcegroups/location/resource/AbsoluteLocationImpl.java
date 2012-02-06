package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import java.util.Random;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.Distance;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.LocationResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class AbsoluteLocationImpl extends IAbsoluteLocation.Stub {
	
	private LocationResourceGroup locationRG;
	private AbsoluteLocationResource absoluteLocationR;
	private String appIdentifier;
	private PermissionValidator psv;
	
	private UpdateRequest updateRequest = null;
	
	private double lastLatitude = -1000.0;
	private double lastLongitude = -1000.0;
	private double randomInaccuracyLat = 0.0;
	private double randomInaccuracyLong = 0.0;
	
	
	public AbsoluteLocationImpl(LocationResourceGroup locationRG, AbsoluteLocationResource absoluteLocationR,
			String appIdentifier) {
		this.locationRG = locationRG;
		this.absoluteLocationR = absoluteLocationR;
		this.appIdentifier = appIdentifier;
		
		this.psv = new PermissionValidator(this.locationRG, this.appIdentifier);
	}
	
	
	public void startLocationLookup(long minTime, float minDistance) throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateRequest = new UpdateRequest(minTime, minDistance);
		absoluteLocationR.startLocationLookup(appIdentifier, updateRequest);
	}
	
	
	public void endLocationLookup() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		this.updateRequest = null;
		absoluteLocationR.endLocationLookup(this.appIdentifier);
	}
	
	
	public boolean isGpsEnabled() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.isGpsEnabled();
	}
	
	
	public boolean isActive() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.isActive();
	}
	
	
	public boolean isFixed() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.isFixed();
	}
	
	
	public boolean isUpdateAvailable() {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		// TODO implement the is update available
		return false;
	}
	
	
	public double getLongitude() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		calculateRandomInaccuracy();
		
		return this.absoluteLocationR.getLongitude() * randomInaccuracyLong;
	}
	
	
	public double getLatitude() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		updateLastRequest();
		
		calculateRandomInaccuracy();
		
		return this.absoluteLocationR.getLatitude() * randomInaccuracyLat;
	}
	
	
	public float getAccuracy() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		psv.validate(LocationResourceGroup.PS_SHOW_ACCURACY, "true");
		updateLastRequest();
		
		if (this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION) > this.absoluteLocationR.getAccuracy()) {
			return this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION);
		} else {
			return this.absoluteLocationR.getAccuracy();
		}
	}
	
	
	public float getSpeed() throws RemoteException {
		psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
		psv.validate(LocationResourceGroup.PS_SHOW_SPEED, "true");
		updateLastRequest();
		
		return this.absoluteLocationR.getSpeed();
	}
	
	
	private void updateLastRequest() {
		if (this.updateRequest != null) {
			this.updateRequest.setLastRequest(System.currentTimeMillis());
		}
	}


	private void calculateRandomInaccuracy() {
		int precision = this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION);
		
		double newLatitude = this.absoluteLocationR.getLatitude();
		double newLongitude = this.absoluteLocationR.getLongitude();
		
		double distanceToLastPosition = Distance.calclateArc(lastLatitude, lastLongitude, newLatitude, newLongitude,
				Distance.KILOMETERS) / 1000;
		
		if (distanceToLastPosition > precision) {
			randomInaccuracyLat = new Random().nextDouble() * precision;
			randomInaccuracyLong = new Random().nextDouble() * precision;
			lastLatitude = newLatitude;
			lastLongitude = newLongitude;
		}
	}
}
