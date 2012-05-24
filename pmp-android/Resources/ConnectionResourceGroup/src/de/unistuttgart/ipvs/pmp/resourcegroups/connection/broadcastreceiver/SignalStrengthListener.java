/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

/**
 * Service that stores the signal strengthF
 * 
 * @author Thorsten Berberich
 * 
 */
public class SignalStrengthListener extends PhoneStateListener {
    
    /**
     * The only instance
     */
    private static SignalStrengthListener instance;
    
    /**
     * The signal strength
     */
    private int signalStrength = 99;
    
    
    /**
     * Private because of singleton
     */
    private SignalStrengthListener() {
    }
    
    
    /**
     * Get the only instance of this class
     * 
     * @return
     */
    public static SignalStrengthListener getInstance() {
        if (instance == null) {
            instance = new SignalStrengthListener();
        }
        return instance;
    }
    
    
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        this.signalStrength = signalStrength.getGsmSignalStrength();
    }
    
    
    /**
     * Get the actual signal strength
     * 
     * @return the signalStrength signal strength (ASU)
     */
    public int getSignalStrength() {
        return signalStrength;
    }
}
