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
