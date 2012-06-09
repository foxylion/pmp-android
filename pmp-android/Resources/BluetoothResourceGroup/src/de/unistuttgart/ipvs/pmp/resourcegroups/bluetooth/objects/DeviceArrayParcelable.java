package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable Object of the {@link DeviceArray}
 * 
 * @author Alexander Wassiljew
 * 
 */
public class DeviceArrayParcelable implements Parcelable {
	/**
	 * DeviceArray
	 */
	DeviceArray devices;

	/**
	 * Constructor
	 * 
	 * @param devices
	 */
	public DeviceArrayParcelable(DeviceArray devices) {
		this.devices = devices;
	}

	/**
	 * Returns the {@link DeviceArray}
	 * 
	 * @return
	 */
	public DeviceArray getDevices() {
		return this.devices;
	}

	/**
	 * Private Constructor
	 * 
	 * @param source
	 */
	private DeviceArrayParcelable(Parcel source) {
		this.devices = (DeviceArray) source.readSerializable();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(this.devices);
	}

	public static final Parcelable.Creator<DeviceArrayParcelable> CREATOR = new Parcelable.Creator<DeviceArrayParcelable>() {

		@Override
		public DeviceArrayParcelable createFromParcel(Parcel source) {
			return new DeviceArrayParcelable(source);
		}

		@Override
		public DeviceArrayParcelable[] newArray(int size) {
			return new DeviceArrayParcelable[size];
		}
	};
}
