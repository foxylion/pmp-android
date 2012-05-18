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
}
