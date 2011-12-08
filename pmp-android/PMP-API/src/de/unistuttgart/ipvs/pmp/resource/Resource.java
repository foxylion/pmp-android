/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.resource;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;

/**
 * An individual Resource of a {@link ResourceGroup}.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class Resource {
    
    /**
     * The resource group that this resource is assigned to.
     */
    private ResourceGroup resourceGroup;
    
    
    /**
     * Assigns the resource group during registration.
     * 
     * <b>Do not call this method.</b>
     * 
     * @param resourceGroup
     */
    protected final void assignResourceGroup(ResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }
    
    
    /**
     * 
     * @return the associated {@link ResourceGroup}.
     */
    protected final ResourceGroup getResourceGroup() {
        return this.resourceGroup;
    }
    
    
    /**
     * Retrieves an actual privacy setting class.
     * 
     * @param privacySettingIdentifier
     *            the identifier of the privacy setting
     * @return the privacy setting with the the identifier in the resource group.
     */
    public final AbstractPrivacySetting<?> getPrivacySetting(String privacySettingIdentifier) {
        return this.resourceGroup.getPrivacySetting(privacySettingIdentifier);
    }
    
    
    /**
     * Sets the {@link IBinder} defined in AIDL for communicating over a Service.
     * 
     * @see http://developer.android.com/guide/developing/tools/aidl.html
     * 
     * @param appIdentifier
     *            the identifier for the app accessing the interface.
     * 
     * @return The IBinder that shall be returned when an App binds against the {@link ResourceGroupService} requesting
     *         this resource.
     */
    public abstract IBinder getAndroidInterface(String appIdentifier);
    
}
