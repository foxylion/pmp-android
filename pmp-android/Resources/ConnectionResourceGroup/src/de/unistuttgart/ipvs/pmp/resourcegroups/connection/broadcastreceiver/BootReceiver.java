/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Called when device is booted, starts a service that logs the signal strength
 * 
 * @author Thorsten Berberich
 * 
 */
public class BootReceiver extends BroadcastReceiver {
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent servIntent = new Intent(context, SignalStrengthService.class);
        context.startService(servIntent);
    }
    
}
