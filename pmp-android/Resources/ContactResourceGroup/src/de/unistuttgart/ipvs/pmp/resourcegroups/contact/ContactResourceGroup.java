/*
 * Copyright 2012 pmp-android development team
 * Project: ContactResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.contact;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource.ContactResource;

public class ContactResourceGroup extends ResourceGroup {
    
    public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.contact";
    
    public static final String R_CONTACT = "contactResource";
    
    public static final String PS_OPEN_DIALER = "openDialer";
    public static final String PS_SEND_SMS = "sendSMS";
    public static final String PS_SEND_EMAIL = "sendEmail";
    
    
    public ContactResourceGroup(IPMPConnectionInterface pmpci) {
        super(PACKAGE_NAME, pmpci);
        
        registerResource(R_CONTACT, new ContactResource(this));
        
        Log.i(this, "registerResource");
        registerPrivacySetting(PS_OPEN_DIALER, new BooleanPrivacySetting());
        registerPrivacySetting(PS_SEND_SMS, new BooleanPrivacySetting());
        registerPrivacySetting(PS_SEND_EMAIL, new BooleanPrivacySetting());
    }
    
}
