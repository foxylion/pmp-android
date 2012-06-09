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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools.BluetoothModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools.BluetoothTools;
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools.Device;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load
 * resource groups
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class BluetoothPlanTripActivity extends ResourceGroupReadyActivity {
    
    Handler handler;
    Handler refresh;
    protected boolean isCanceled;
    Button drive;
    Button search;
    Spinner destination;
    Spinner seats;
    EditText duration;
    private String TAG = "BluetoothPlanTripActivity";
    Timer timer;
    Context context;
    ProgressDialog cancelDialog;
    boolean stopConnectedChecker = false;
    EnabledTimer enabledTimer = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_rides);
        this.context = getBaseContext();
        this.timer = new Timer();
        getBluetoothRG(this);
        registerListener();
        if (rgBluetooth != null) {
            startBluetooth();
        }
        this.stopConnectedChecker = false;
    }
    
    
    public void registerListener() {
        this.drive = (Button) findViewById(R.id.btn_bt_Drive);
        this.drive.setEnabled(false);
        this.search = (Button) findViewById(R.id.btn_bt_Search);
        this.search.setEnabled(false);
        this.destination = (Spinner) findViewById(R.id.sp_bt_destination);
        this.seats = (Spinner) findViewById(R.id.sp_bt_seats);
        
        this.duration = (EditText) findViewById(R.id.et_bt_duration);
        this.duration.setText("5");
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.destination.setAdapter(adapter);
        
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.array_numSeats,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.seats.setAdapter(adapter1);
        
        this.drive.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int seat = Integer.valueOf(BluetoothPlanTripActivity.this.seats.getSelectedItem().toString());
                String dest = BluetoothPlanTripActivity.this.destination.getSelectedItem().toString();
                int dur = Integer.valueOf(BluetoothPlanTripActivity.this.duration.getText().toString());
                
                Log.i(BluetoothPlanTripActivity.this.TAG, "SEAT not NULL: " + seat);
                Log.i(BluetoothPlanTripActivity.this.TAG, "DEST not NULL: " + dest);
                Log.i(BluetoothPlanTripActivity.this.TAG, "DUR not NULL: " + dur);
                
                BluetoothModel.getInstance().setDestination(dest);
                BluetoothModel.getInstance().setDuration(dur);
                BluetoothModel.getInstance().setSeats(seat);
                BluetoothModel.getInstance().setRole(BluetoothModel.ROLE_DRIVER);
                createCancelProgressDialog("Offer ride", "Offering a ride to nearby passengers!",
                        "Stop offering");
                
                BluetoothPlanTripActivity.this.stopConnectedChecker = false;
                BluetoothPlanTripActivity.this.timer.schedule(new ConnectedChecker(), 2000);
                try {
                    if (rgBluetooth == null) {
                        Log.i(BluetoothPlanTripActivity.this.TAG, "rgBluetooth is null");
                    }
                    rgBluetooth.makeDiscoverable("vHike:" + dest + "-" + seat, dur);
                    
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        
        this.search.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int seat = Integer.valueOf(BluetoothPlanTripActivity.this.seats.getSelectedItem().toString());
                String dest = BluetoothPlanTripActivity.this.destination.getSelectedItem().toString();
                int dur = Integer.valueOf(BluetoothPlanTripActivity.this.duration.getText().toString());
                
                BluetoothModel.getInstance().setDestination(dest);
                BluetoothModel.getInstance().setDuration(dur);
                BluetoothModel.getInstance().setSeats(seat);
                BluetoothModel.getInstance().setRole(BluetoothModel.ROLE_PASSENGER);
                createCancelProgressDialog("Searching", "Searching for offered rides nearby!",
                        "Stop searching");
                
                try {
                    rgBluetooth.discover("vHike:" + dest + "-" + seat, dur);
                    
                    BluetoothPlanTripActivity.this.timer.schedule(new Time(rgBluetooth), 2000);
                    
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
    private void createCancelProgressDialog(String title, String message, String buttonText) {
        this.cancelDialog = new ProgressDialog(this);
        this.cancelDialog.setTitle(title);
        this.cancelDialog.setMessage(message);
        this.cancelDialog.setButton(buttonText, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Use either finish() or return() to either close the activity or just the dialog
                BluetoothPlanTripActivity.this.stopConnectedChecker = true;
                return;
            }
        });
        this.cancelDialog.show();
    }
    
    
    private AlertDialog createAlertDialog(final CharSequence[] items, final List<Device> devices) {
        //final CharSequence[] items = { "Red", "Blue" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothPlanTripActivity.this);
        builder.setTitle("Pick a driver");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                BluetoothModel.getInstance().setToConnectDevice(devices.get(item));
                BluetoothPlanTripActivity.this.finish();
            }
        });
        
        AlertDialog alert = builder.create();
        return alert;
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgBluetooth != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    startBluetooth();
                }
            });
        }
    }
    
    
    public void startBluetooth() {
        try {
            if (rgBluetooth.isBluetoothAvailable()) {
                
                Toast.makeText(this, "Starting bluetooth!", Toast.LENGTH_LONG).show();
                rgBluetooth.enableBluetooth(true);
                this.timer.schedule(new EnabledTimer(), 5000);
                
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
    }
    
    public class Time extends TimerTask {
        
        IBluetooth rgBluetooth;
        Handler refresh;
        boolean running = false;
        
        
        public Time(IBluetooth rgBluetooth) {
            this.rgBluetooth = rgBluetooth;
            this.refresh = new Handler(Looper.getMainLooper());
        }
        
        
        @Override
        public void run() {
            try {
                if (!this.rgBluetooth.isDiscovering()) {
                    Log.i(BluetoothPlanTripActivity.this.TAG, "Time: adding devices");
                    DeviceArrayParcelable foundDevices = this.rgBluetooth.getFoundDevices();
                    Log.i(BluetoothPlanTripActivity.this.TAG, "nach getFoundDevices");
                    DeviceArray foundDevicesArray = foundDevices.getDevices();
                    Log.i(BluetoothPlanTripActivity.this.TAG, "nach getDevices");
                    final List<String> founddevices = foundDevicesArray.getDevices();
                    Log.i(BluetoothPlanTripActivity.this.TAG, "nach getDevices");
                    final List<Device> devices = BluetoothTools.DeviceArrayListToDeviceList(founddevices);
                    Log.i(BluetoothPlanTripActivity.this.TAG, "nach getFoundDevices to list");
                    final List<Device> filteredDevices = BluetoothTools.filterForVHike(devices);
                    
                    final List<Device> filteredDestination = BluetoothTools.filterForDestination(
                            filteredDevices,
                            BluetoothModel.getInstance().getDestination());
                    BluetoothPlanTripActivity.this.cancelDialog.dismiss();
                    Log.i(BluetoothPlanTripActivity.this.TAG, "nach dismiss dialog");
                    this.refresh.post(new Runnable() {
                        
                        @Override
                        public void run() {
                            CharSequence[] items = new CharSequence[filteredDestination.size()];
                            for (int i = 0; i < filteredDestination.size(); i++) {
                                items[i] = filteredDestination.get(i).getName();
                                Log.i(BluetoothPlanTripActivity.this.TAG, "Added device: "
                                        + filteredDestination.get(i).getName() + " "
                                        + filteredDestination.get(i).getAddress());
                            }
                            createAlertDialog(items, filteredDestination).show();
                        }
                    });
                } else {
                    BluetoothPlanTripActivity.this.timer.schedule(new Time(this.rgBluetooth), 2000);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    
    public class ConnectedChecker extends TimerTask {
        
        @Override
        public void run() {
            try {
                if (rgBluetooth.isConnected()) {
                    Log.i(BluetoothPlanTripActivity.this.TAG, "ConnectedChecker is Connected");
                    BluetoothModel.getInstance().setConnected(rgBluetooth.isConnected());
                    
                    BluetoothPlanTripActivity.this.cancelDialog.dismiss();
                    
                    finish();
                } else {
                    Log.i(BluetoothPlanTripActivity.this.TAG, "ConnectedChecker rescheduled");
                    if (!BluetoothPlanTripActivity.this.stopConnectedChecker) {
                        BluetoothPlanTripActivity.this.timer.schedule(new ConnectedChecker(), 2000);
                    }
                }
                
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public class EnabledTimer extends TimerTask {
        
        Handler refresh;
        
        
        public EnabledTimer() {
            this.refresh = new Handler(Looper.getMainLooper());
        }
        
        
        @Override
        public void run() {
            try {
                if (rgBluetooth.isEnabled()) {
                    this.refresh.post(new Runnable() {
                        
                        @Override
                        public void run() {
                            BluetoothPlanTripActivity.this.search.setEnabled(true);
                            BluetoothPlanTripActivity.this.drive.setEnabled(true);
                        }
                    });
                    
                } else {
                    BluetoothPlanTripActivity.this.timer.schedule(new EnabledTimer(), 2000);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
}
