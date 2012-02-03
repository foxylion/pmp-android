package de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl;

interface IAbsoluteLocation {

	void startLocationLookup(long minTime, float minDistance);
	void endLocationLookup();

	boolean isGpsEnabled();
	boolean isActive();
	boolean isFixed();
	
	double getLongitude();
	double getLatitude();
	
	float getAccuracy();
	float getSpeed();
}