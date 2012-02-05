package de.unistuttgart.ipvs.pmp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.service.PMPService;

/**
 * {@link BroadcastReceiver} to start updating contexts when PMP wasn't launched before on that phone.
 * 
 * @author Tobias Kuhn
 * 
 */
public class BootReceiver extends android.content.BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context);
    }
    
    
    /**
     * @param context
     */
    public static void startService(Context context) {
        Intent startService = new Intent(context, PMPService.class);
        context.startService(startService);
    }
    
}
