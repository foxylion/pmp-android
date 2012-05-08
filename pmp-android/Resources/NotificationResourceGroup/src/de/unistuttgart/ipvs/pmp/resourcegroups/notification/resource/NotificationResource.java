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
    
    
    public void notify(String tickerText, String title, String message) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) this.RG.getContext().getSystemService(ns);
        
        int icon = de.unistuttgart.ipvs.pmp.resourcegroups.notification.R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, tickerText, when);
        
        Context context = this.RG.getContext();
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
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
