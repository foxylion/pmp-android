package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.DeviceBootHandler;

public class BootCompletedReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, EnergyService.class));
        DeviceBootHandler.handle(context);
    }
}
