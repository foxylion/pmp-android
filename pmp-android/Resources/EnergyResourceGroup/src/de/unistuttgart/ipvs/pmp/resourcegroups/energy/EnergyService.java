package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class EnergyService extends Service {

	private BroadcastReceiver br;

	private final String ACTION_BATTERY_CHANGED = Intent.ACTION_BATTERY_CHANGED;
	private final String ACTION_POWER_CONNECTED = Intent.ACTION_POWER_CONNECTED;
	private final String ACTION_POWER_DISCONNECTED = Intent.ACTION_POWER_DISCONNECTED;

	private final String ACTION_SCREEN_ON = Intent.ACTION_SCREEN_ON;
	private final String ACTION_SCREEN_OFF = Intent.ACTION_SCREEN_OFF;

	private final String ACTION_SHUTDOWN = Intent.ACTION_SHUTDOWN;
	private final String ACTION_BOOT_COMPLETED = Intent.ACTION_BOOT_COMPLETED;

	@Override
	public void onCreate() {
		Log.i(EnergyConstants.LOG_TAG, "Service started.");

		// Instantiate the broadcast receiver
		br = new EnergyBroadcastReceiver();

		// Add broadcast receiver
		getApplicationContext().registerReceiver(br,
				new IntentFilter(ACTION_BATTERY_CHANGED));
		getApplicationContext().registerReceiver(br,
				new IntentFilter(ACTION_POWER_CONNECTED));
		getApplicationContext().registerReceiver(br,
				new IntentFilter(ACTION_POWER_DISCONNECTED));
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(EnergyConstants.LOG_TAG, "Service stopped.");

		// Remove broadcast receiver
		getApplicationContext().unregisterReceiver(br);

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
