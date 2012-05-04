package de.unistuttgart.ipvs.pmp.resourcegroups.example.phonereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyPhoneReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                // Show a toast with the current state and incoming number
                Toast.makeText(context, "MyPhoneReceiver: " + state + " / " + phoneNumber, Toast.LENGTH_LONG).show();
            } else {
                // Show a toast with the current state
                Toast.makeText(context, "MyPhoneReceiver: " + state, Toast.LENGTH_LONG).show();
                
            }
        }
    }
}
