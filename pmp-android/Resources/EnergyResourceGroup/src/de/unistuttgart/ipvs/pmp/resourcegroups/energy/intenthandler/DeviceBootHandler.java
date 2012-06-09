/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.DeviceBootEvent;

/**
 * This is the handler for all device boot events. It is called, if an intent happened.
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
        
        // Fire a screen event
        ScreenHandler.handle(changedTo, context);
    }
    
}
