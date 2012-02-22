/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-API
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

import android.content.Context;

/**
 * Interface for communication from the {@link ResourceGroup} plugin to the PMP model.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IPMPConnectionInterface {
    
    /**
     * Must first ask the PMP model for the privacy setting identified by rgPackage and psIdentifier, then find out what
     * this privacy setting's value is for appPackage.
     * 
     * @param rgPackage
     * @param psIdentifier
     * @param appPackage
     * @return the value of the privacy setting identified for appPackage, or null, if it is not set or was not found
     */
    public String getPrivacySettingValue(String rgPackage, String psIdentifier, String appPackage);
    
    
    /**
     * Ability to get a context for a resource group.
     * 
     * @param rgPackage
     * @return an Android context
     */
    public Context getContext(String rgPackage);
    
}
