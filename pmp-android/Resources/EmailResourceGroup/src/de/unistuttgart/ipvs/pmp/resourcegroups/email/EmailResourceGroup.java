/*
 * Copyright 2011 pmp-android development team
 * Project: EmailResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.email;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;

public class EmailResourceGroup extends ResourceGroup {
    
    private Context context;
    
    public static final String PRIVACY_SETTING_SEND_EMAIL = "canSendEmail";
    public static final String RESOURCE_EMAIL_OPERATIONS = "emailOperations";
    
    
    public EmailResourceGroup(Context serviceContext) {
        super(serviceContext);
        this.context = serviceContext;
        
        registerPrivacySetting(PRIVACY_SETTING_SEND_EMAIL, new BooleanPrivacySetting("Send Email",
                "Is allowed to send emails."));
        
        registerResource(RESOURCE_EMAIL_OPERATIONS, new EmailResource());
    }
    
    
    @Override
    public String getName(String locale) {
        return "Email ResourceGroup";
    }
    
    
    @Override
    public String getDescription(String locale) {
        return "Allows some basic interactions with Androids Mail app.";
    }
    
    
    @Override
    protected String getServiceAndroidName() {
        return "de.unistuttgart.ipvs.pmp.resourcegroups.email";
    }
    
    
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration success.");
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        Log.e("Registration failed with \"" + message + "\"");
    }
    
    
    public Context getContext() {
        return this.context;
    }
    
}
