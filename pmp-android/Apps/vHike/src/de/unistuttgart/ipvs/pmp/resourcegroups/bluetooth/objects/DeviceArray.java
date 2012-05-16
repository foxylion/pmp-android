package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class DeviceArray implements Serializable {

	public List<String> devices;
	
	public DeviceArray(List<String> devices) {
		this.devices = devices;
	}

	
	public List<String> getDevices(){
		return devices;
	}
	
}
