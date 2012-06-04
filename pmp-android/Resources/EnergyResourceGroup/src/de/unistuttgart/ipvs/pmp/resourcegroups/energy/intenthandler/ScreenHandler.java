package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;

/**
 * This is the handler for all screen events. It is called, if an intent happened.
 * 
 * @author Marcus Vetter
 * 
 */
public class ScreenHandler {
    
    public static void handle(boolean changedTo, Context context) {
        /*
         * Store to database
         */
        ScreenEvent se = new ScreenEvent(-1, System.currentTimeMillis(), changedTo);
        DBConnector.getInstance(context).storeScreenEvent(se);
    }
    
}
