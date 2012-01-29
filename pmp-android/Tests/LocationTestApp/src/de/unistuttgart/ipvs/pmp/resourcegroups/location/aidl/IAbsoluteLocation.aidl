package de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl;

interface IAbsoluteLocation {

	void startLocationLookup();
	void endLocationLookup();

	boolean isFixed();
	
	double getLongitude();
	double getLatitude();
}