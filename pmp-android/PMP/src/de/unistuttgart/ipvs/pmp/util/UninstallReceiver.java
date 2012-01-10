package de.unistuttgart.ipvs.pmp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.model.Model;

/**
 * {@link BroadcastReceiver} to remove apps once they are uninstalled by Android.
 * 
 * @author Tobias Kuhn
 * 
 */
public class UninstallReceiver extends android.content.BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentPackage = intent.getData().getSchemeSpecificPart();
        
        if (Model.getInstance().getApp(intentPackage) != null) {
            Model.getInstance().unregisterApp(intentPackage);
        }
    }
    
}
