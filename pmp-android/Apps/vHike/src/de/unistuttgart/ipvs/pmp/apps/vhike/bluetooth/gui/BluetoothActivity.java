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
    
    EditText et = null;
    Button send = null;
    ListView conversationView = null;
    ArrayAdapter<String> conversationArrayAdapter = null;
    
    MessageThread messageThread = null;
    public String TAG = "BluetoothActivity";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_bluetooth);
        timer = new Timer();
        et = (EditText) findViewById(R.id.edit_text_out);
        send = (Button) findViewById(R.id.button_send);
        
        // Initialize the array adapter for the conversation thread
        conversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        conversationView = (ListView) findViewById(R.id.in);
        conversationView.setAdapter(conversationArrayAdapter);
        
        rides = (Button) findViewById(R.id.bluetooth_ride);
        rides.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(BluetoothActivity.this, BluetoothPlanTripActivity.class);
                startActivity(intent);
            }
        });
        
        send.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                try {
                    rgBluetooth.sendMessage(et.getText().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
        if (rgBluetooth != null) {
            try {
                
                if (BluetoothModel.getInstance().getToConnectDevice() != null) {
                    Device device = BluetoothModel.getInstance().getToConnectDevice();
                    rgBluetooth.connect(device.getAddress());
                    Log.i(TAG, "Connecting to " + device.getAddress());
                }
                
                timer.schedule(new ConnectionChecker(), 3000);
                timer.schedule(new MessageThread(), 2000);
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
                    rgBluetooth.enableBluetooth(false);
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
            
            if (rgBluetooth != null) {
                try {
                    MessageArrayParcelable messagesParcelable = rgBluetooth.getReceivedMessages();
                    
                    MessageArray messageArray = messagesParcelable.getDevices();
                    final List<String> founddevices = messageArray.getMessages();
                    Log.i(TAG, "Getting messages: " + founddevices.size());
                    
                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        
                        public void run() {
                            for (String string : founddevices) {
                                conversationArrayAdapter.add(string);
                                Log.i(TAG, "Adding messages: " + string);
                            }
                        }
                    });
                    timer.schedule(new MessageThread(), 2000);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                timer.schedule(new MessageThread(), 2000);
            }
        }
        
    }
    
    public class ConnectionChecker extends TimerTask {
        
        @Override
        public void run() {
            try {
                if (!rgBluetooth.isConnected()) {
                    timer.schedule(new ConnectionChecker(), 30000);
                    Log.i(TAG, "Not Connected!");
                } else {
                    Log.i(TAG, "Connected!");
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
    }
}
