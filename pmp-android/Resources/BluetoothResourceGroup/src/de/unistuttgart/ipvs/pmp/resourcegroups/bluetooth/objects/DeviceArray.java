package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Hold the devices in an Array
 * @author Alexander Wassiljew
 *
 */
public class DeviceArray implements Serializable {
	/**
	 * List of Devices
	 */
	public List<String> devices;
	/**
	 * Constructor
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
	public List<String> getDevices(){
		return devices;
	}
	
}
