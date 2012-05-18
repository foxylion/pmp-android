/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.ConnectionConstants;

/**
 * Service that stores the signal strengthF
 * 
 * @author Thorsten Berberich
 * 
 */
public class SignalStrengthService extends Service {
    
    /**
     * Preferences editor
     */
    private SharedPreferences.Editor editor;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences settings = getSharedPreferences(ConnectionConstants.PREF_FILE, Context.MODE_WORLD_READABLE);
        this.editor = settings.edit();
        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telMgr.listen(new CallStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }
    
    
    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    /**
     * Stores the signal strength in the shared preferences
     * 
     * @author Thorsten Berberich
     * 
     */
    class CallStateListener extends PhoneStateListener {
        
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            SignalStrengthService.this.editor.putInt(ConnectionConstants.PREF_SIGNAL_KEY,
                    signalStrength.getGsmSignalStrength());
            SignalStrengthService.this.editor.commit();
        }
    }
    
}
