package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class DeviceBootHandler {
    
    public static void handle(Context context, boolean changedTo) {
        /*
         * Store to database
         */
        DeviceBootEvent dbe = new DeviceBootEvent(-1, System.currentTimeMillis(), changedTo);
        DBConnector.getInstance(context).storeDeviceBootEvent(dbe);
    }
    
}
