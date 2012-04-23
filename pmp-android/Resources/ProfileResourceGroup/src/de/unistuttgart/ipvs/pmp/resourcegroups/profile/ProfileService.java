package de.unistuttgart.ipvs.pmp.resourcegroups.profile;

import de.unistuttgart.ipvs.pmp.resourcegroups.profile.observer.SMSObserver;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class ProfileService extends Service {
		
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Constants.LOG_TAG, "Profile service started.");
		Debug.e("Profile service started.", getApplicationContext());
		registerSMSObserver();
	}
	
	@Override
	public void onDestroy() {
		Log.i(Constants.LOG_TAG, "Profile service stopped.");
		Debug.e("Profile service stopped.", getApplicationContext());
		super.onDestroy();
	}
	
	protected void registerSMSObserver() {
		String url = "content://sms/"; 
        Uri uri = Uri.parse(url); 
        SMSObserver smsObserver = new SMSObserver(this);
		getContentResolver().registerContentObserver(uri, true, smsObserver);
	}

	public void processSMSEvent() {
	    Uri uri = Uri.parse("content://sms/");
	    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	    cursor.moveToNext();
	    String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
	    if (protocol == null) {
	    	// Outgoing SMS
	    	Debug.e("Outgoing SMS event.", getApplicationContext());
	    } else {
	    	// Incoming SMS
	    	Debug.e("Incoming SMS event.", getApplicationContext());
	    }
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
