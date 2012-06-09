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
package de.unistuttgart.ipvs.pmp.resourcegroups.notification;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.resource.NotificationResource;

public class NotificationResourceGroup extends ResourceGroup {
    
    public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.notification";
    
    public static final String R_NOTIFICATION = "notificationResource";
    
    public static final String PS_USE_NOTIFY = "allowNotification";
    
    
    public NotificationResourceGroup(IPMPConnectionInterface pmpci) {
        super(PACKAGE_NAME, pmpci);
        
        registerResource(R_NOTIFICATION, new NotificationResource(this));
        Log.i(this, "registerResource");
        registerPrivacySetting(PS_USE_NOTIFY, new BooleanPrivacySetting());
    }
    
}
