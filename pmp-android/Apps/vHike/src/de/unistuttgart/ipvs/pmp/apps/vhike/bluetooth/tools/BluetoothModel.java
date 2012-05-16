package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;

public class BluetoothModel {
    
    public static final int ROLE_DRIVER = 0;
    public static final int ROLE_PASSENGER = 1;
    
    static BluetoothModel theInstance;
    
    List<DeviceArray> pairedDevices;
    List<DeviceArray> foundDevices;
    List<MessageArray> messages;
    ProgressDialog pd;
    
    
    private BluetoothModel() {
        pairedDevices = new ArrayList<DeviceArray>();
        foundDevices = new ArrayList<DeviceArray>();
        messages = new ArrayList<MessageArray>();
        
    }
    
    int seats = 0;
    String destination = "";
    int duration = 0;
    int role = 0;
    
    
    public void setRole(int role) {
        role = role;
    }
    
    
    public int getRole() {
        return role;
    }
    
    
    public int getSeats() {
        return seats;
    }
    
    
    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    
    public String getDestination() {
        return destination;
    }
    
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    
    public int getDuration() {
        return duration;
    }
    
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    
    public static BluetoothModel getInstance() {
        if (theInstance != null) {
            return theInstance;
        } else {
            theInstance = new BluetoothModel();
            return theInstance;
        }
    }
    
    
    public void showPD(Context context, String title, String message, boolean bool) {
        pd = ProgressDialog.show(context, title, message, bool);
    }
    
    
    public void dismissPD() {
        pd.dismiss();
    }
}
