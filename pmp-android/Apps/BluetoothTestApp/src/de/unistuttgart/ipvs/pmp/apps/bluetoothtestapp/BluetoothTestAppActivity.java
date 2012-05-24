package de.unistuttgart.ipvs.pmp.apps.bluetoothtestapp;

import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.aidl.IBluetooth;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArray;
import de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth.objects.DeviceArrayParcelable;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothTestAppActivity extends Activity {

	private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.bluetooth";
	private static final String R_NAME = "bluetoothResource";

	private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier
			.make(RG_NAME, R_NAME);

	public Handler handler;
	public int nee;
	TextView tv;
	IBluetooth bt;
	String text ="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.handler = new Handler();
		PMP.get(getApplication());
		
		setContentView(R.layout.main);
		tv = (TextView) findViewById(R.id.textview);
	}

	@Override
	protected void onResume() {
		super.onResume();

		PMP.get().getResource(R_ID, new PMPRequestResourceHandler() {

			@Override
			public void onReceiveResource(PMPResourceIdentifier resource,
					IBinder binder, boolean isMocked) {
				resourceCached();
			}

			@Override
			public void onBindingFailed() {
				Toast.makeText(BluetoothTestAppActivity.this,
						"Binding Resource failed", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void resourceCached() {
		IBinder binder = PMP.get().getResourceFromCache(R_ID);

		if (binder == null) {

			this.handler.post(new Runnable() {

				public void run() {
					Toast.makeText(
							BluetoothTestAppActivity.this,
							"PMP said something like 'resource group does not exists'.",
							Toast.LENGTH_SHORT).show();
				}
			});

			return;
		}

		bt = IBluetooth.Stub.asInterface(binder);
		try {
			Log.i(this, "BLUETOOTH AVAILABLE? : " + bt.isBluetoothAvailable());

			// if(!bt.isEnabled()){
			 bt.enableBluetooth(true);
			//
			// bt.makeDiscoverable(2);
			// }else{
			// bt.makeDiscoverable(2);
			// }
			

			DeviceArrayParcelable array = bt.getPairedDevices();
			DeviceArray devicesArray = array.getDevices();
			List<String> devices = devicesArray.getDevices();
			
			for (String string : devices) {
				text = text + string;
			}
			Log.i(this,"BluetoothGetPairedDevices; " +text);
			
			
			Handler refresh = new Handler(Looper.getMainLooper());
			refresh.post(new Runnable() {
			    public void run()
			    {
			    	tv.setText(text);        
			    }
			});
			
			bt.discover();
			
			Thread refreshThread = new Timer(bt);
			refreshThread.start();
			
			this.handler.post(new Runnable() {

				public void run() {
					Toast.makeText(BluetoothTestAppActivity.this,
							"Notification Resource loaded.", Toast.LENGTH_SHORT)
							.show();
				}
			});

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
			this.handler.post(new Runnable() {

				public void run() {
					Toast.makeText(BluetoothTestAppActivity.this,
							"Please enable the Service Feature.",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	public class Timer extends Thread{
		IBluetooth bt;
		Handler refresh;
		boolean running = false; 
		public Timer(IBluetooth bt){
			this.bt = bt;
			refresh = new Handler(Looper.getMainLooper());
		}
		
		public void run(){
			running = true;
			while(running){
				try {
					if(!bt.isDiscovering()){
						bt.getFoundDevices();
						DeviceArrayParcelable foundDevices = bt.getFoundDevices();
						DeviceArray foundDevicesArray = foundDevices.getDevices();
						List<String> founddevices = foundDevicesArray.getDevices();
						
						for (String string : founddevices) {
							text = text + string;
						}
						Log.i(this,"BluetoothGetFoundDevices; " +text);
						refresh.post(new Runnable() {
						    public void run()
						    {
						    	tv.setText(text);        
						    }
						});
						running = false;
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
}
