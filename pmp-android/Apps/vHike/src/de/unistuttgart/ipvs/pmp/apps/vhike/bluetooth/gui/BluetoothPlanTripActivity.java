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
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_rides);
        context = this.getBaseContext();
        timer = new Timer();
        getBluetoothRG(this);
        registerListener();
        if (rgBluetooth != null) {
            startBluetooth();
        }
    }
    
    
    public void registerListener() {
        drive = (Button) findViewById(R.id.btn_bt_Drive);
        
        search = (Button) findViewById(R.id.btn_bt_Search);
        
        destination = (Spinner) findViewById(R.id.sp_bt_destination);
        seats = (Spinner) findViewById(R.id.sp_bt_seats);
        
        duration = (EditText) findViewById(R.id.et_bt_duration);
        duration.setText("5");
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.destination.setAdapter(adapter);
        
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.array_numSeats,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.seats.setAdapter(adapter1);
        
        drive.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int seat = Integer.valueOf(seats.getSelectedItem().toString());
                String dest = destination.getSelectedItem().toString();
                int dur = Integer.valueOf(duration.getText().toString());
                
                BluetoothModel.getInstance().setDestination(dest);
                BluetoothModel.getInstance().setDuration(dur);
                BluetoothModel.getInstance().setSeats(seat);
                BluetoothModel.getInstance().setRole(BluetoothModel.ROLE_DRIVER);
                createCancelProgressDialog("Offer ride", "Offering a ride to nearby passengers!", "Stop offering");
                
                timer.schedule(new ConnectedChecker(), 2000);
                try {
                    rgBluetooth.makeDiscoverable("vHike:" + dest + "-" + seat, dur);
                    
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        
        search.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int seat = Integer.valueOf(seats.getSelectedItem().toString());
                String dest = destination.getSelectedItem().toString();
                int dur = Integer.valueOf(duration.getText().toString());
                
                BluetoothModel.getInstance().setDestination(dest);
                BluetoothModel.getInstance().setDuration(dur);
                BluetoothModel.getInstance().setSeats(seat);
                BluetoothModel.getInstance().setRole(BluetoothModel.ROLE_PASSENGER);
                createCancelProgressDialog("Searching", "Searching for offered rides nearby!", "Stop searching");
                
                try {
                    rgBluetooth.discover();
                    
                    timer.schedule(new Time(rgBluetooth), 2000);
                    
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
    private void createCancelProgressDialog(String title, String message, String buttonText) {
        cancelDialog = new ProgressDialog(this);
        cancelDialog.setTitle(title);
        cancelDialog.setMessage(message);
        cancelDialog.setButton(buttonText, new DialogInterface.OnClickListener() {
            
            public void onClick(DialogInterface dialog, int which) {
                // Use either finish() or return() to either close the activity or just the dialog
                return;
            }
        });
        cancelDialog.show();
    }
    
    
    private AlertDialog createAlertDialog(final CharSequence[] items, final List<Device> devices) {
        //final CharSequence[] items = { "Red", "Blue" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothPlanTripActivity.this);
        builder.setTitle("Pick a driver");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
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
            refresh = new Handler(Looper.getMainLooper());
        }
        
        
        public void run() {
            try {
                if (!rgBluetooth.isDiscovering()) {
                    Log.i(TAG, "Time: adding devices");
                    DeviceArrayParcelable foundDevices = rgBluetooth.getFoundDevices();
                    Log.i(TAG, "nach getFoundDevices");
                    DeviceArray foundDevicesArray = foundDevices.getDevices();
                    Log.i(TAG, "nach getDevices");
                    final List<String> founddevices = foundDevicesArray.getDevices();
                    Log.i(TAG, "nach getDevices");
                    final List<Device> devices = BluetoothTools.DeviceArrayListToDeviceList(founddevices);
                    Log.i(TAG, "nach getFoundDevices to list");
                    final List<Device> filteredDevices = BluetoothTools.filterForVHike(devices);
                    
                    final List<Device> filteredDestination = BluetoothTools.filterForDestination(filteredDevices,
                            BluetoothModel.getInstance().getDestination());
                    cancelDialog.dismiss();
                    Log.i(TAG, "nach dismiss dialog");
                    refresh.post(new Runnable() {
                        
                        public void run() {
                            CharSequence[] items = new CharSequence[filteredDestination.size()];
                            for (int i = 0; i < filteredDestination.size(); i++) {
                                items[i] = filteredDestination.get(i).getName();
                                Log.i(TAG, "Added device: " + filteredDestination.get(i).getName() + " "
                                        + filteredDestination.get(i).getAddress());
                            }
                            createAlertDialog(items, filteredDestination).show();
                        }
                    });
                } else {
                    timer.schedule(new Time(rgBluetooth), 2000);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    
    public class ConnectedChecker extends TimerTask {
        
        public void run() {
            try {
                if (rgBluetooth.isConnected()) {
                    Log.i(TAG, "ConnectedChecker is Connected");
                    BluetoothModel.getInstance().setConnected(rgBluetooth.isConnected());
                    
                    cancelDialog.dismiss();
                    
                    BluetoothPlanTripActivity.this.finish();
                } else {
                    Log.i(TAG, "ConnectedChecker rescheduled");
                    timer.schedule(new ConnectedChecker(), 2000);
                }
                
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
