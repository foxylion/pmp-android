package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.gui;

import java.util.List;

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
    IBluetooth bt;
    Button drive;
    Button search;
    Spinner destination;
    Spinner seats;
    EditText duration;
    private String TAG = "BluetoothPlanTripActivity";
    
    Context context;
    ProgressDialog cancelDialog;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_rides);
        context = this.getBaseContext();
        
        bt = getBluetoothRG(this);
        registerListener();
        startBluetooth();
        
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
                ConnectedChecker checker = new ConnectedChecker();
                checker.start();
                
                try {
                    bt.makeDiscoverable(dur);
                    
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
                    bt.discover();
                    
                    Thread timer = new Timer(bt);
                    timer.start();
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
            if (bt.isBluetoothAvailable()) {
                Toast.makeText(this, "Starting bluetooth!", Toast.LENGTH_LONG).show();
                bt.enableBluetooth(true);
                
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
    }
    
    public class Timer extends Thread {
        
        IBluetooth bt;
        Handler refresh;
        boolean running = false;
        
        
        public Timer(IBluetooth bt) {
            this.bt = bt;
            refresh = new Handler(Looper.getMainLooper());
        }
        
        
        public void run() {
            running = true;
            while (running) {
                try {
                    if (!bt.isDiscovering()) {
                        bt.getFoundDevices();
                        DeviceArrayParcelable foundDevices = bt.getFoundDevices();
                        DeviceArray foundDevicesArray = foundDevices.getDevices();
                        final List<String> founddevices = foundDevicesArray.getDevices();
                        final List<Device> devices = BluetoothTools.DeviceArrayListToDeviceList(founddevices);
                        
                        cancelDialog.dismiss();
                        
                        refresh.post(new Runnable() {
                            
                            public void run() {
                                CharSequence[] items = new CharSequence[devices.size()];
                                for (int i = 0; i < devices.size(); i++) {
                                    items[i] = devices.get(i).getName();
                                }
                                createAlertDialog(items, devices).show();
                            }
                        });
                        running = false;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    public class ConnectedChecker extends Thread {
        
        @Override
        public void run() {
            try {
                while (!bt.isConnected()) {
                    sleep(3000);
                }
                BluetoothModel.getInstance().setConnected(bt.isConnected());
                
                cancelDialog.dismiss();
                
                BluetoothPlanTripActivity.this.finish();
                
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
