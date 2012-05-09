package de.unistuttgart.ipvs.pmp.resourcegroups.notification.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl.INotification;

public class NotificationMockImpl extends INotification.Stub {
    
    public void notify(String tickerText, String title, String message) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
}
