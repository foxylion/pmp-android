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
package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.gui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools.BluetoothModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools.Device;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.MessageArrayParcelable;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load
 * resource groups
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class BluetoothActivity extends ResourceGroupReadyActivity {
    
    Handler handler;
    protected boolean isCanceled;
    Button rides;
    Timer timer;
    boolean stopTimerTasks = false;
    EditText et = null;
    Button send = null;
    ListView conversationView = null;
    ArrayAdapter<String> conversationArrayAdapter = null;
    
    MessageThread messageThread = null;
    public String TAG = "BluetoothActivity";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler();
        this.stopTimerTasks = false;
        setContentView(R.layout.activity_bluetooth);
        this.timer = new Timer();
        this.et = (EditText) findViewById(R.id.edit_text_out);
        this.send = (Button) findViewById(R.id.button_send);
        
        // Initialize the array adapter for the conversation thread
        this.conversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        this.conversationView = (ListView) findViewById(R.id.in);
        this.conversationView.setAdapter(this.conversationArrayAdapter);
        
        this.rides = (Button) findViewById(R.id.bluetooth_ride);
        this.rides.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(BluetoothActivity.this, BluetoothPlanTripActivity.class);
                startActivity(intent);
            }
        });
        
        getBluetoothRG(this);
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgBluetooth != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    
                }
                
            });
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        this.send.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                try {
                    String role = "";
                    if (BluetoothModel.getInstance().getRole() == BluetoothModel.ROLE_DRIVER) {
                        role = "Driver: ";
                    } else {
                        role = "Passenger: ";
                    }
                    if (rgBluetooth != null) {
                        rgBluetooth.sendMessage(role + BluetoothActivity.this.et.getText().toString());
                    }
                    BluetoothActivity.this.et.setText("");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        
        if (rgBluetooth != null) {
            try {
                
                if (BluetoothModel.getInstance().getToConnectDevice() != null) {
                    Device device = BluetoothModel.getInstance().getToConnectDevice();
                    rgBluetooth.connect(device.getAddress());
                    Log.i(this.TAG, "Connecting to " + device.getAddress());
                }
                
                this.timer.schedule(new ConnectionChecker(), 3000);
                
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
    
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
        try {
            if (rgBluetooth.isBluetoothAvailable()) {
                if (rgBluetooth.isEnabled()) {
                    rgBluetooth.sendMessage("Connection Lost!");
                    
                    rgBluetooth.enableBluetooth(false);
                    this.stopTimerTasks = true;
                    
                }
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public class MessageThread extends TimerTask {
        
        @Override
        public void run() {
            
            try {
                if (rgBluetooth != null && rgBluetooth.isConnected()) {
                    try {
                        MessageArrayParcelable messagesParcelable = rgBluetooth.getReceivedMessages();
                        if (messagesParcelable != null) {
                            MessageArray messageArray = messagesParcelable.getDevices();
                            
                            final List<String> founddevices = messageArray.getMessages();
                            Log.i(BluetoothActivity.this.TAG, "Getting messages: " + founddevices.size());
                            
                            Handler refresh = new Handler(Looper.getMainLooper());
                            refresh.post(new Runnable() {
                                
                                @Override
                                public void run() {
                                    for (String string : founddevices) {
                                        if (!string.equals("")) {
                                            BluetoothActivity.this.conversationArrayAdapter.add(string);
                                            Log.i(BluetoothActivity.this.TAG, "Adding messages: " + string);
                                        }
                                        
                                    }
                                }
                            });
                        }
                        if (!BluetoothActivity.this.stopTimerTasks) {
                            BluetoothActivity.this.timer.schedule(new MessageThread(), 2000);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    //                    if (!stopTimerTasks)
                    //                        timer.schedule(new MessageThread(), 2000);
                }
                
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    public class ConnectionChecker extends TimerTask {
        
        @Override
        public void run() {
            try {
                if (rgBluetooth.isConnected()) {
                    if (!BluetoothActivity.this.stopTimerTasks) {
                        BluetoothActivity.this.timer.schedule(new MessageThread(), 2000);
                    }
                    Log.i(BluetoothActivity.this.TAG, "Connected!");
                } else {
                    if (!BluetoothActivity.this.stopTimerTasks) {
                        BluetoothActivity.this.timer.schedule(new ConnectionChecker(), 10000);
                    }
                    Log.i(BluetoothActivity.this.TAG, "Not Connected!");
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
    }
}
