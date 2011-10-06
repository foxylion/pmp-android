/*
 * Copyright 2011 pmp-android development team
 * Project: FileSystemResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

/**
 * This resource gives access to files saved on the user's Andorid device. To do so, it defines several privacy-levels,
 * such as privacy-levels for reading or writing files on the device's file system. It also registers all corresponding
 * resources.
 * 
 * @author Patrick Strobel
 * @version 0.2.0
 */
public class FileSystemResourceGroup extends ResourceGroup {
    
    public static final String SERVICE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.filesystem";
    
    /**
     * Context of service in which this resource-group is running.
     */
    private final Context context;
    
    
    /**
     * Creates the resource-group including its privacy-levels and resources
     * 
     * @param context
     *            Context of the service giving access to our resource-group
     * @param service
     *            Class of our service.
     * @throws Exception
     *             Throws if at least one privacy-level could not be instantiated.
     */
    public FileSystemResourceGroup(Context context) {
        super(context);
        
        // Store the service' context, because we will need it later
        this.context = context;
        
        // Generate privacy-levels and register them
        PrivacyLevels privacyLevels = new PrivacyLevels(context);
        privacyLevels.addToResourceGroup(this);
        
        // Generate resources and register them
        Resources resources = new Resources();
        resources.addToResourceGroup(this);
    }
    
    
    @Override
    public String getName(String locale) {
        return this.context.getResources().getString(R.string.rg_name);
    }
    
    
    @Override
    public String getDescription(String locale) {
        return this.context.getResources().getString(R.string.rg_desc);
    }
    
    
    @Override
    protected String getServiceAndroidName() {
        return SERVICE_NAME;
    }
    
    
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration was successfull");
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        Log.d("Registration failed: " + message);
    }
    
}
