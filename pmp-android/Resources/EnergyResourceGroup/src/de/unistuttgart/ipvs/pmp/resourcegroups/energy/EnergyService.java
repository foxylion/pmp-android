package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class EnergyService extends Service {

	/**
	 * The broadcast receiver
	 */
	private BroadcastReceiver br;

	@Override
	public void onCreate() {
		Log.i(EnergyConstants.LOG_TAG, "Energy service started.");

		// Instantiate the broadcast receiver
		br = new EnergyBroadcastReceiver();

		// Add broadcast receiver
		getApplicationContext().registerReceiver(br,
				new IntentFilter(EnergyConstants.ACTION_BATTERY_CHANGED));
		getApplicationContext().registerReceiver(br,
				new IntentFilter(EnergyConstants.ACTION_SCREEN_ON));
		getApplicationContext().registerReceiver(br,
				new IntentFilter(EnergyConstants.ACTION_SCREEN_OFF));
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(EnergyConstants.LOG_TAG, "Energy service stopped.");

		// Remove broadcast receiver
		getApplicationContext().unregisterReceiver(br);

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
