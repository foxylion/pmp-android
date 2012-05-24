package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
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
