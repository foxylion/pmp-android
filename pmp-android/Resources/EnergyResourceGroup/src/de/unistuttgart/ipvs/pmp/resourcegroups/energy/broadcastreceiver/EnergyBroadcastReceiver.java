package de.unistuttgart.ipvs.pmp.resourcegroups.energy.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.BatteryHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.ScreenHandler;

/**
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
        System.out.println("Receive called!!!!!!!!!!!!!!");
        if (action.equals(EnergyConstants.ACTION_SCREEN_ON)) {
            ScreenHandler.handle(true, context);
        } else if (action.equals(EnergyConstants.ACTION_SCREEN_OFF)) {
            ScreenHandler.handle(false, context);
        } else if (action.equals(EnergyConstants.ACTION_BATTERY_CHANGED)) {
            BatteryHandler.handle(intent, context);
        }
    }
}
