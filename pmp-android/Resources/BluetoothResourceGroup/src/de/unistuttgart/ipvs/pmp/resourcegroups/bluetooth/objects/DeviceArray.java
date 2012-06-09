package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

/**
 * Hold the devices in an Array
 * 
 * @author Alexander Wassiljew
 * 
 */
public class DeviceArray implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8851352241912636566L;
	/**
	 * List of Devices
	 */
	public List<String> devices;

	/**
	 * Constructor
	 * 
	 * @param devices
	 */
	public DeviceArray(List<String> devices) {
		this.devices = devices;
	}

	/**
	 * Returns the devices
	 * 
	 * @return devices
	 */
	public List<String> getDevices() {
		return this.devices;
	}

}
