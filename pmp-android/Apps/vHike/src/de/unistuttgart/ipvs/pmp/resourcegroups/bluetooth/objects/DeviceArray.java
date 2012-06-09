package de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects;

import java.io.Serializable;
import java.util.List;

public class DeviceArray implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -8851352241912636566L;
    public List<String> devices;
    
    
    public DeviceArray(List<String> devices) {
        this.devices = devices;
    }
    
    
    public List<String> getDevices() {
        return this.devices;
    }
    
}
