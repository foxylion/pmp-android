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
            String kind = device.getName().substring(0, 5);
            if (kind.equals("vHike")) {
                drivers.add(device);
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
