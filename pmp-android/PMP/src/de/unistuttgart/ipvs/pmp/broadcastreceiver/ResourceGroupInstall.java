package de.unistuttgart.ipvs.pmp.broadcastreceiver;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ResourceGroupInstall extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent incomingIntent) {
        String packageName = incomingIntent.getData().getSchemeSpecificPart();
        
        Log.d("ResourceGroupInstall: "+ packageName + " has been installed on the device");
        
        final PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES
                    | PackageManager.GET_INTENT_FILTERS);
            if (pi.activities != null) {
                for (ActivityInfo ai : pi.activities) {
                    if (ai.name.equals(ResourceGroupActivity.class.getName())) {
                        Log.d(packageName + " :: " + ai.name + " will be started now.");
                        Intent i = new Intent();
                        i.setClassName(packageName, ai.name);
                        i.setPackage(packageName);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            }
        } catch (NameNotFoundException e) {
            Log.e("ResourceGroupInstall: "+ packageName + " can't be found in the PackageManger", e);
        }
        
    }
    
}
