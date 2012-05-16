package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceArrayParcelable implements Parcelable{

	DeviceArray devices;
	
	public DeviceArrayParcelable(DeviceArray devices) {
        this.devices = devices;
    }
	
	public DeviceArray getDevices(){
		return this.devices;
	}
	
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
