package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.BatteryChangedHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.ScreenHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
		if (action.equals(EnergyConstants.ACTION_SCREEN_ON)) {
			ScreenHandler.handle(true);
		} else if (action.equals(EnergyConstants.ACTION_SCREEN_OFF)) {
			ScreenHandler.handle(false);
		} else if (action.equals(EnergyConstants.ACTION_BATTERY_CHANGED)) {
			BatteryChangedHandler.handle(intent);
		}
	}
}
