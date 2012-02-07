package de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl;

/**
 * The IAbsoluteLocation interface is used to get the current location
 * based on current GPS information.
 * The IAbsoluteLocation can only be used, when the Privacy Setting
 * "useAbsoluteLocation" is set to "true".
 *
 * @author Jakob Jarosch
 */
interface IAbsoluteLocation {

	/**
	 * This method has to be called before a location or other informations
	 * can be fetch from the other methods.
	 * When this method is called GPS has to be enabled.
	 * If GPS is disabled a notification will be created, the User has to enable
	 * GPS manually. Until this change is not made, no location will be returned.
	 * The method {@link IAbsoluteLocation#isGpsEnabled()} can be used to check if
	 * GPS is enabled.
	 *
	 * @param minTime The minimal time between two updates.
	 * @param minDistance The minimal distance between two updates.
	 */
	void startLocationLookup(long minTime, float minDistance);
	
	/**
	 * Ends the update requests for GPS. Call this always before your App exits.
	 */
	void endLocationLookup();

	/**
	 * @return Returns whether GPS is currently enabled or not.
	 */
	boolean isGpsEnabled();
	
	/**
	 * @return Returns true when the GPS location fetch is active.
	 *         Returns false, when not startLocationLookup has been called,
	 *         or the time between two method calls was longer than minTime.
	 */
	boolean isActive();
	
	/**
	 * @return Returns whether the GPS module can calculate a position or not.
	 */
	boolean isFixed();
	
	/**
	 * @return Returns true if the constraints used when calling
	 *  startLocationLookup triggers an update.
	 */
	boolean isUpdateAvailable();
	
	/**
	 * @return Returns the current longitude.
	 */
	double getLongitude();
	
	/**
	 * @return Returns the current latitude.
	 */
	double getLatitude();
	
	/**
	 * Returns the accuracy of the GPS signal. The accuracy will never be better than the
	 * Privacy Setting "locationPrecision". Better precision means the user is better traceable,
	 * but he may rejects a more detailed location.
	 * The Privacy Setting "showAccuracy" must be set to "true".
	 *
	 * @return Returns the current accuracy of the GPS signal.
	 */
	float getAccuracy();
	
	/**
	 * Returns the current speed of the device.
	 * The Privacy Setting "showSpeed" must be set to "true".
	 * 
	 * @return Returns the current speed of the device, in kilometers per hour.
	 */
	float getSpeed();
}