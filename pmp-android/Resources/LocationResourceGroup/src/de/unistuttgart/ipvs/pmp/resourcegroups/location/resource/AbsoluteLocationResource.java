package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import android.content.Context;
import android.location.LocationManager;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.Location;

public class AbsoluteLocationResource extends Resource {

	private Location locationRG;

	private LocationManager locationManager;
	
	public AbsoluteLocationResource(Location locationRG) {
		this.locationRG = locationRG;
		
		locationManager = (LocationManager) this.locationRG.getContext().getSystemService(Context.LOCATION_SERVICE);
	}
	
	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new AbsoluteLocationImpl(locationRG, this, appIdentifier);
	}


	public void startLocationLookup() {
		
	}
	
	public void endLocationLookup() {
		
	}
}
