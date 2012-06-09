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

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.NotificationResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl.INotification;

public class NotificationImpl extends INotification.Stub {
    
    NotificationResourceGroup RG;
    NotificationResource RES;
    String appIdentifier;
    
    PermissionValidator psv;
    
    
    public NotificationImpl(NotificationResourceGroup RG, NotificationResource RES, String appIdentifier) {
        this.RG = RG;
        this.RES = RES;
        this.appIdentifier = appIdentifier;
        this.psv = new PermissionValidator(RG, appIdentifier);
    }
    
    
    public void notify(String tickerText, String title, String message) throws RemoteException {
        this.psv.validate(NotificationResourceGroup.PS_USE_NOTIFY, "true");
        this.RES.notify(this.appIdentifier, tickerText, title, message);
    }
    
}
