package de.unistuttgart.ipvs.pmp.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.activity.DebugInstallRGActivity;

/**
 * {@link BroadcastReceiver} to directly install RGs once they are installed by Android.
 * 
 * @author Tobias Kuhn
 * 
 */
public class InstallReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentPackage = intent.getData().getSchemeSpecificPart();
        
        try {
            Resources res = context.getPackageManager().getResourcesForApplication(intentPackage);
            InputStream is = res.getAssets().open("rgis.xml");
            is.close();
            // no FileNotFoundException - thus the file exists.           
            
            Intent install = new Intent(PMPApplication.getContext(), DebugInstallRGActivity.class);
            install.putExtra("pkg", intentPackage);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PMPApplication.getContext().startActivity(install);
            
        } catch (IOException ioe) {
            // don't care - desired behavior
            Log.v("InstallReceiver ignoring : " + intentPackage);
            
        } catch (NameNotFoundException nnfe) {
            Log.e("InstallReceiver failed lookup : ", nnfe);
        }
    }
}
