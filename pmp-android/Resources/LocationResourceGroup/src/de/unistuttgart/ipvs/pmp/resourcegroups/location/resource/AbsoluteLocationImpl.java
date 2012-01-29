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

	public AbsoluteLocationImpl(Location locationRG, AbsoluteLocationResource absoluteLocationR, String appIdentifier) {
		this.locationRG = locationRG;
		this.absoluteLocationR = absoluteLocationR;
		this.appIdentifier = appIdentifier;
		
		this.ps = new PermissionValidator(this.locationRG,this.appIdentifier);
	}
	
	public void startLocationLookup() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		absoluteLocationR.startLocationLookup();
	}

	public void endLocationLookup() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		absoluteLocationR.endLocationLookup();
	}

	public boolean isFixed() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		return false;
	}

	public double getLongitude() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		return 0.0;
	}

	public double getLatitude() throws RemoteException {
		ps.validate(Location.PS_USE_ABSOLUTE_LOCATION, "true");
		
		return 0.0;
	}
	

}
