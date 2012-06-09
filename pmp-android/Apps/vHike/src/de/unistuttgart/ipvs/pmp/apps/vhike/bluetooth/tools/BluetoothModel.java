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
    
    Device toConnectDevice;
    
    
    private BluetoothModel() {
        this.pairedDevices = new ArrayList<DeviceArray>();
        this.foundDevices = new ArrayList<DeviceArray>();
        this.messages = new ArrayList<MessageArray>();
        
    }
    
    int seats = 0;
    String destination = "";
    int duration = 0;
    int role = 0;
    private boolean connected = false;
    
    
    public void setRole(int role) {
        this.role = role;
    }
    
    
    public int getRole() {
        return this.role;
    }
    
    
    public int getSeats() {
        return this.seats;
    }
    
    
    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    
    public String getDestination() {
        return this.destination;
    }
    
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    
    public int getDuration() {
        return this.duration;
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
        this.pd = ProgressDialog.show(context, title, message, bool);
    }
    
    
    public void dismissPD() {
        this.pd.dismiss();
    }
    
    
    public void setToConnectDevice(Device device) {
        this.toConnectDevice = device;
    }
    
    
    public Device getToConnectDevice() {
        return this.toConnectDevice;
    }
    
    
    public void setConnected(boolean state) {
        this.connected = state;
    }
    
    
    public boolean isConnected() {
        return this.connected;
    }
}
