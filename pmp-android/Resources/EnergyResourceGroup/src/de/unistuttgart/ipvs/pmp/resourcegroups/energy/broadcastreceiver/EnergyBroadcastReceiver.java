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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.BatteryHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.DeviceBootHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.ScreenHandler;

/**
 * This is the broadcast receiver for all intents of interest
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyBroadcastReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        /*
         * Handle the intents 
         */
        if (action.equals(EnergyConstants.ACTION_SCREEN_ON)) {
            ScreenHandler.handle(true, context);
        } else if (action.equals(EnergyConstants.ACTION_SCREEN_OFF)) {
            ScreenHandler.handle(false, context);
        } else if (action.equals(EnergyConstants.ACTION_BATTERY_CHANGED)) {
            BatteryHandler.handle(intent, context);
        } else if (action.equals(EnergyConstants.ACTION_SHOW_DOWN)) {
            DeviceBootHandler.handle(context, false);
        }
    }
}
