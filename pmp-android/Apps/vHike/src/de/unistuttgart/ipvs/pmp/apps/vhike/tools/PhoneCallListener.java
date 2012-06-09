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
package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import android.app.Activity;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * 
 * @author Andre Nguyen
 * 
 *         Monitors phone call activities for returning to vHike after finishing phone call
 */
public class PhoneCallListener extends PhoneStateListener {
    
    Activity activity;
    private boolean isPhoneCalling = false;
    
    
    public PhoneCallListener(Activity activity) {
        this.activity = activity;
    }
    
    
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        
        if (TelephonyManager.CALL_STATE_RINGING == state) {
            // phone ringing
            Log.i(this, "RINGING, number: " + incomingNumber);
        }
        
        if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
            // active
            Log.i(this, "OFFHOOK");
            
            this.isPhoneCalling = true;
        }
        
        if (TelephonyManager.CALL_STATE_IDLE == state) {
            // run when class initial and phone call ended, 
            // need detect flag from CALL_STATE_OFFHOOK
            Log.i(this, "IDLE");
            
            if (this.isPhoneCalling) {
                
                Log.i(this, "restart app");
                
                // restart app
                //                Intent i = this.activity.getBaseContext().getPackageManager()
                //                        .getLaunchIntentForPackage(this.activity.getBaseContext().getPackageName());
                Intent in = new Intent(this.activity.getBaseContext(), this.activity.getClass());
                
                this.activity.startActivity(in);
                
                this.isPhoneCalling = false;
            }
            
        }
    }
}
