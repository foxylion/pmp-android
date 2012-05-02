package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface IDBConnector {

	/**
	 * Store the current state of the battery
	 * 
	 * @param timestamp
	 *            the time stamp (in ms) of the record
	 * @param level
	 *            level of the battery
	 * @param health
	 *            health of the battery
	 * @param status
	 *            status of the battery
	 * @param plugged
	 *            USB/AC-plugged, nullable
	 * @param present
	 *            true, if the battery is present
	 * @param technology
	 *            the technology of the battery
	 * @param temperature
	 *            the temperature of the battery
	 * @param voltage
	 *            the voltage of the battery
	 */
	public void storeBatteryState(long timestamp, int level, String health,
			String status, String plugged, boolean present, String technology,
			int temparature, int voltage);

	/**
	 * Store the current state of the screen
	 * 
	 * @param timestamp
	 *            the time stamp (in ms) of the record
	 * @param changedTo
	 *            true, if the screen turned on
	 */
	public void storeScreenState(long timestamp, boolean changedTo);

	/**
	 * Store the event, if the device booted completly
	 * 
	 * @param timestamp
	 *            the time stamp (in ms) of the record
	 */
	public void storeDeviceBoot(long timestamp);

}
