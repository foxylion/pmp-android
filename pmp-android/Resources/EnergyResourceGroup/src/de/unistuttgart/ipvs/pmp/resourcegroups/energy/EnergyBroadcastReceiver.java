package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class EnergyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String msg = "";
		if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
			msg = "Power connected.";
			Log.i(EnergyConstants.LOG_TAG, msg);
		} else if (action
				.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
			msg = "Power disconnected.";
			Log.i(EnergyConstants.LOG_TAG, msg);
		} else if (action.equals("android.intent.action.BATTERY_CHANGED")) {
			int level = intent.getIntExtra("level", 0);
			msg = "Battery changed to: " + String.valueOf(level) + "%";
			msg = "Temperature: " + intent.getIntExtra("temperature", 0);
			Log.i(EnergyConstants.LOG_TAG, msg);
		} else {
			msg = action;
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
