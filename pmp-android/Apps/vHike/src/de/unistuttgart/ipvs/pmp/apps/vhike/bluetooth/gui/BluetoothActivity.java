package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.tools.BluetoothModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;

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
    IBluetooth bt;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reset();
        handler = new Handler();
        setContentView(R.layout.activity_bluetooth);
        
        rides = (Button) findViewById(R.id.bluetooth_ride);
        rides.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(BluetoothActivity.this, BluetoothPlanTripActivity.class);
                startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
            }
        });
        bt = getBluetoothRG(this);
    }
    
    
    private void reset() {
        BluetoothModel.getInstance().setConnected(false);
        BluetoothModel.getInstance().setDestination(null);
        BluetoothModel.getInstance().setDuration(0);
        BluetoothModel.getInstance().setRole(0);
        BluetoothModel.getInstance().setToConnectDevice(null);
        
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgBluetooth != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    init();
                }
                
            });
        }
    }
    
    
    private void init() {
        bt = getBluetoothRG(this);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (BluetoothModel.getInstance().getToConnectDevice() != null) {
            try {
                String name = BluetoothModel.getInstance().getToConnectDevice().getName();
                String address = BluetoothModel.getInstance().getToConnectDevice().getAddress();
                Log.i(this, "Name: " + name + " Address: " + address);
                if (bt == null) {
                    Log.i(this, "BT null");
                }
                bt.connect(address);
                //                BluetoothModel.getInstance().setToConnectDevice(null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        
        if (BluetoothModel.getInstance().isConnected()) {
            
        }
        
    }
    
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
        try {
            if (bt.isBluetoothAvailable()) {
                if (bt.isEnabled()) {
                    bt.enableBluetooth(false);
                }
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
