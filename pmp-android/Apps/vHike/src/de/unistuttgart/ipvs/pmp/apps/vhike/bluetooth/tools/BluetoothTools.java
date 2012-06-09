/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools;

import java.util.ArrayList;
import java.util.List;

public class BluetoothTools {
    
    public static List<Device> DeviceArrayListToDeviceList(List<String> arrayList) {
        List<Device> deviceList = new ArrayList<Device>();
        
        for (int i = 0; i < arrayList.size(); i++) {
            if (i % 2 == 0) {
                Device device = new Device(arrayList.get(i), arrayList.get(i + 1));
                deviceList.add(device);
            }
        }
        return deviceList;
    }
    
    
    public static List<Device> filterForDrivers(List<Device> devices) {
        List<Device> drivers = new ArrayList<Device>();
        for (Device device : devices) {
            String kind = device.getName().substring(0, 1);
            if (kind.equals("D")) {
                drivers.add(device);
            }
        }
        return drivers;
    }
    
    
    public static List<Device> filterForPassengers(List<Device> devices) {
        List<Device> passengers = new ArrayList<Device>();
        for (Device device : devices) {
            String kind = device.getName().substring(0, 1);
            if (kind.equals("P")) {
                passengers.add(device);
            }
        }
        return passengers;
    }
    
    
    public static List<Device> filterForVHike(List<Device> devices) {
        List<Device> drivers = new ArrayList<Device>();
        for (Device device : devices) {
            if (device.getName() != null) {
                String kind = device.getName().substring(0, 5);
                if (kind.equals("vHike")) {
                    drivers.add(device);
                }
            }
        }
        return drivers;
    }
    
    
    public static List<Device> filterForDestination(List<Device> devices, String destination) {
        List<Device> destinations = new ArrayList<Device>();
        for (Device device : devices) {
            if (device.getName().contains(destination)) {
                destinations.add(device);
            }
        }
        return destinations;
    }
}
