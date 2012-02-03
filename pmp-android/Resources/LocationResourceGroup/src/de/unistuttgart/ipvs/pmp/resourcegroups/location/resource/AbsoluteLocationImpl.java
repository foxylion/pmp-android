package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.Location;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class AbsoluteLocationImpl extends IAbsoluteLocation.Stub {

	private Location locationRG;
	private AbsoluteLocationResource absoluteLocationR;
	private String appIdentifier;
	private PermissionValidator ps;
	
	private UpdateRequest updateRequest;

	public AbsoluteLocationImpl(Location locationRG, AbsoluteLocationResource absoluteLocationR, String appIdentifier) {
		this.locationRG = locationRG;
		this.absoluteLocationR = absoluteLocationR;
		this.appIdentifier = appIdentifier;
		
		this.ps = new PermissionValidator(this.locationRG,this.appIdentifier);
	}
	
	public void startLocationLookup(long minTime, float minDistance) throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateRequest = new UpdateRequest(minTime, minDistance);
		absoluteLocationR.startLocationLookup(appIdentifier, updateRequest);
	}

	public void endLocationLookup() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		absoluteLocationR.endLocationLookup(this.appIdentifier);
	}

	public boolean isGpsEnabled() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		return this.absoluteLocationR.isGpsEnabled();
	}
	
	public boolean isActive() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		return this.absoluteLocationR.isActive();
	}
	
	public boolean isFixed() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		return this.absoluteLocationR.isFixed();
	}
	
	public boolean isUpdateAvailable() {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		// TODO implement the is update available
		return false;
	}

	public double getLongitude() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		// TODO Use the min Detail Privacy Setting as inaccuracy level.
		return this.absoluteLocationR.getLongitude();
	}

	public double getLatitude() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		updateLastRequest();
		
		// TODO Use the min Detail Privacy Setting as inaccuracy level.
		return this.absoluteLocationR.getLatitude();
	}

	public float getAccuracy() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		ps.validate(Location.PS_SHOW_ACCURACY, "true");
		
		updateLastRequest();

		// TODO Use the min Detail Privacy Setting as lower inaccuracy level.
		return this.absoluteLocationR.getAccuracy();
	}

	public float getSpeed() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		ps.validate(Location.PS_SHOW_SPEED, "true");
		
		updateLastRequest();
		
		return this.absoluteLocationR.getSpeed();
	}
	
	
	private void updateLastRequest() {
		this.updateRequest.setLastRequest(System.currentTimeMillis());
	}
}
