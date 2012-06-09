/*
 * Copyright 2012 pmp-android development team
 * Project: BluetoothResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
