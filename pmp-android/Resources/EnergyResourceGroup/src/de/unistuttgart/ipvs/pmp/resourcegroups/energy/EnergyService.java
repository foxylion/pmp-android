package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyService extends Service {
    
    /**
     * The broadcast receiver
     */
    private BroadcastReceiver br;
    
    
    @Override
    public void onCreate() {
        Log.i(EnergyConstants.LOG_TAG, "Energy service started.");
        
        // Set the Device ID
        TelephonyManager tManager = (TelephonyManager) getApplicationContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        EnergyConstants.DEVICE_ID = tManager.getDeviceId();
        
        // Instantiate the broadcast receiver
        this.br = new EnergyBroadcastReceiver();
        
        // Add broadcast receiver
        getApplicationContext().registerReceiver(this.br, new IntentFilter(EnergyConstants.ACTION_BATTERY_CHANGED));
        getApplicationContext().registerReceiver(this.br, new IntentFilter(EnergyConstants.ACTION_SCREEN_ON));
        getApplicationContext().registerReceiver(this.br, new IntentFilter(EnergyConstants.ACTION_SCREEN_OFF));
        
        super.onCreate();
    }
    
    
    @Override
    public void onDestroy() {
        Log.i(EnergyConstants.LOG_TAG, "Energy service stopped.");
        
        // Remove broadcast receiver
        getApplicationContext().unregisterReceiver(this.br);
        
        super.onDestroy();
    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    
    public Context getConext() {
        return getApplicationContext();
    }
}
