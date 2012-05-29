/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

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
     * Flag if the listener is registered or not
     */
    private Boolean registered = false;
    
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
        return this.signalStrength;
    }
    
    
    /**
     * Register the service
     */
    public void register(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        
        // Register the listener
        if (tm != null) {
            tm.listen(SignalStrengthListener.getInstance(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            this.registered = true;
        }
    }
    
    
    /**
     * Check if the listener is registered
     * 
     * @return registered true iff registered, false otherwise
     */
    public Boolean isRegistered() {
        return this.registered;
    }
}
