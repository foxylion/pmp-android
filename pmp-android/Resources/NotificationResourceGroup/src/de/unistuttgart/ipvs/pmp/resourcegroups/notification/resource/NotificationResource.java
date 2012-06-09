/*
 * Copyright 2012 pmp-android development team
 * Project: NotificationResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.notification.resource;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.NotificationResourceGroup;

public class NotificationResource extends Resource {
    
    NotificationResourceGroup RG;
    
    
    public NotificationResource(NotificationResourceGroup notificationResourceGroup) {
        this.RG = notificationResourceGroup;
    }
    
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        return new NotificationImpl(this.RG, this, appIdentifier);
    }
    
    
    public void notify(String appPackage, String tickerText, String title, String message) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) this.RG.getContext().getSystemService(ns);
        
        int icon = de.unistuttgart.ipvs.pmp.resourcegroups.notification.R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, tickerText, when);
        
        Context context = this.RG.getContext(appPackage);
        CharSequence contentTitle = title;
        CharSequence contentText = message;
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        
        final int HELLO_ID = 1;
        
        mNotificationManager.notify(HELLO_ID, notification);
    }
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        return new NotificationMockImpl();
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        return new NotificationCloakImpl();
    }
}
