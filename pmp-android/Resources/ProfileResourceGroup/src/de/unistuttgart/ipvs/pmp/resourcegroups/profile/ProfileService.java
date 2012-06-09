/*
 * Copyright 2012 pmp-android development team
 * Project: ProfileResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.profile;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.profile.observer.CallObserver;
import de.unistuttgart.ipvs.pmp.resourcegroups.profile.observer.SMSObserver;

public class ProfileService extends Service {
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Constants.LOG_TAG, "Profile service started.");
        Debug.e("Profile service started.", getApplicationContext());
        registerSMSObserver();
        registerCallObserver();
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
    
    
    protected void registerCallObserver() {
        String url = "content://call_log/calls";
        Uri uri = Uri.parse(url);
        CallObserver callObserver = new CallObserver(this);
        getContentResolver().registerContentObserver(uri, true, callObserver);
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
    
    
    public void processCallEvent() {
        Uri uri = Uri.parse("content://call_log/calls");
        String[] fields = new String[] { Calls.TYPE };
        Cursor cursor = getContentResolver().query(uri, fields, null, null, Calls.DATE + " DESC");
        cursor.moveToNext();
        
        int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
        switch (type) {
            case 1:
                Debug.e("Incoming call event.", getApplicationContext());
                break;
            case 2:
                Debug.e("Outgoing call event.", getApplicationContext());
                break;
            case 3:
                Debug.e("Missed call event.", getApplicationContext());
                break;
        }
    }
    
    
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
