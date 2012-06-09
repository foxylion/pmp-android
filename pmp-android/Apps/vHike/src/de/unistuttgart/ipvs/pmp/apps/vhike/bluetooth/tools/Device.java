package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools;

public class Device {
    
    String name = "";
    String address = "";
    
    
    public Device(String name, String address) {
        this.name = name;
        this.address = address;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    
    public String getAddress() {
        return this.address;
    }
}
