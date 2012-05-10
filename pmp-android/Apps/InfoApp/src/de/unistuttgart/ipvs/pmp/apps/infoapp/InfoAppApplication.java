/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.infoapp;

import android.app.Application;
import de.unistuttgart.ipvs.pmp.api.PMP;

/**
 * {@link Application} of the InfoApp
 * 
 * @author Thorsten Berberich
 * 
 */
public class InfoAppApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        PMP.get(this);
    }
}
