/*
 * Copyright 2011 pmp-android development team
 * Project: DatabaseResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;

/**
 * @author Dang Huynh
 * 
 */
public class DatabaseResourceGroup extends ResourceGroup {
    
    public static final String DATABASE_RESOURCE_IDENTIFIER = "de.unistuttgart.ipvs.pmp.resourcegroups.database";
    
    private Context context;
    
    public static final String PRIVACY_SETTING_READ = "read";
    public static final String PRIVACY_SETTING_MODIFY = "modify";
    public static final String PRIVACY_SETTING_CREATE = "create";
    
    private DatabaseResource dbr;
    
    // Privacy Setting
    private BooleanPrivacySetting read, modify, create;
    
    
    public DatabaseResourceGroup(Context context) {
        super(context);
        this.context = context;
        
        // TODO Remove Log
        Log.d(context.getResources().getString(R.string.resource_group_name) + getDescription("en"));
        
        // Prepare the privacy settings and resource
        this.read = new BooleanPrivacySetting(context.getResources().getString(R.string.privacy_setting_read_name), context
                .getResources().getString(R.string.privacy_setting_read_description));
        this.modify = new BooleanPrivacySetting(context.getResources().getString(R.string.privacy_setting_modify_name),
                context.getResources().getString(R.string.privacy_setting_modify_description));
        this.create = new BooleanPrivacySetting(context.getResources().getString(R.string.privacy_setting_create_name),
                context.getResources().getString(R.string.privacy_setting_create_description));
        this.dbr = new DatabaseResource(context);
        
        // Register the privacy settings
        registerPrivacySetting(PRIVACY_SETTING_READ, this.read);
        registerPrivacySetting(PRIVACY_SETTING_MODIFY, this.modify);
        registerPrivacySetting(PRIVACY_SETTING_CREATE, this.create);
        
        // Register the resource
        // TODO: Where should the resource be created? Only when an authorized
        // application request, right?
        registerResource(DATABASE_RESOURCE_IDENTIFIER, this.dbr);
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getName(java.lang.String)
     */
    @Override
    public String getName(String locale) {
        // TODO: Locale or not locale?
        return this.context.getResources().getString(R.string.resource_group_name);
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getDescription(java.lang
     * .String)
     */
    @Override
    public String getDescription(String locale) {
        // TODO: Locale or not locale?
        return this.context.getResources().getString(R.string.resource_group_description);
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#getServiceAndroidName()
     */
    @Override
    protected String getServiceAndroidName() {
        return DATABASE_RESOURCE_IDENTIFIER;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#onRegistrationSuccess()
     */
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration with the PMP Service successed");
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.ResourceGroup#onRegistrationFailed(
     * java.lang.String)
     */
    @Override
    public void onRegistrationFailed(String message) {
        // TODO Retry?
        Log.e("Registration with the PMP Service failed: " + message + "");
    }
}
