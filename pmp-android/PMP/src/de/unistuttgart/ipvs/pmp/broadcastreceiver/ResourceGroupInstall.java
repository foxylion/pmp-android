/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import de.unistuttgart.ipvs.pmp.Log;

public class ResourceGroupInstall extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent incomingIntent) {
        String packageName = incomingIntent.getData().getSchemeSpecificPart();
        
        Log.d("ResourceGroupInstall: " + packageName + " has been installed on the device");
        
        final PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES
                    | PackageManager.GET_INTENT_FILTERS);
            if (pi.activities != null) {
                for (ActivityInfo ai : pi.activities) {
                    /*if (ai.name.equals(RGsActivity.class.getName())) {
                        Log.d(packageName + " :: " + ai.name + " will be started now.");
                        Intent i = new Intent();
                        i.setClassName(packageName, ai.name);
                        i.setPackage(packageName);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }*/
                }
            }
        } catch (NameNotFoundException e) {
            Log.e("ResourceGroupInstall: " + packageName + " can't be found in the PackageManger", e);
        }
        
    }
    
}
