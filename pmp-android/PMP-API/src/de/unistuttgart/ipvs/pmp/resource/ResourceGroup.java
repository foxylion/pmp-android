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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;

/**
 * <p>
 * A resource group that bundles {@link Resource}s and {@link AbstractPrivacySetting}s. You can register them by using
 * the methods {@link ResourceGroup#registerResource(String, Resource)} and
 * {@link ResourceGroup#registerPrivacySetting(String, AbstractPrivacySetting)}.
 * </p>
 * 
 * <p>
 * In order to work, a ResourceGroup needs a service defined in the manifest file which simply is
 * {@link ResourceGroupService}, and the app containing the ResourceGroup and its service must extend
 * {@link ResourceGroupApp}.
 * </p>
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroup {
    
    /**
     * The package of this ResourceGroup.
     */
    private final String rgPackage;
    
    /**
     * The connection interface to PMP.
     */
    private final IPMPConnectionInterface pmpci;
    
    /**
     * The resources present in that resource group.
     */
    private final Map<String, Resource> resources;
    
    /**
     * The privacy settings present in that resource group.
     */
    private final Map<String, AbstractPrivacySetting<?>> privacySettings;
    
    
    /**
     * Creates a new {@link ResourceGroup}.
     * 
     * @param rgPackage
     *            the package which identifies this RG
     * @param pmpci
     *            interface for connecting to PMP
     */
    public ResourceGroup(String rgPackage, IPMPConnectionInterface pmpci) {
        this.rgPackage = rgPackage;
        this.pmpci = pmpci;
        this.resources = new HashMap<String, Resource>();
        this.privacySettings = new HashMap<String, AbstractPrivacySetting<?>>();
    }
    
    
    /**
     * Registers resource as resource "identifier" in this resource group.
     * 
     * @param identifier
     * @param resource
     */
    public void registerResource(String identifier, Resource resource) {
        resource.assignResourceGroup(this);
        this.resources.put(identifier, resource);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the resource identified by "identifier", if present, null otherwise
     */
    public Resource getResource(String identifier) {
        return this.resources.get(identifier);
    }
    
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getResources() {
        return new ArrayList<String>(this.resources.keySet());
    }
    
    
    /**
     * Registers privacySetting as privacy setting "identifier" in this resource group.
     * 
     * @param identifier
     * @param privacySetting
     */
    public void registerPrivacySetting(String identifier, AbstractPrivacySetting<?> privacySetting) {
        privacySetting.assignResourceGroup(this, identifier);
        this.privacySettings.put(identifier, privacySetting);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the privacy setting identified by "identifier", if present, null otherwise
     */
    public AbstractPrivacySetting<?> getPrivacySetting(String identifier) {
        return this.privacySettings.get(identifier);
    }
    
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getPrivacySettings() {
        return new ArrayList<String>(this.privacySettings.keySet());
    }
    
    
    /**
     * @see IPMPConnectionInterface#getPrivacySettingValue(String, String, String)
     */
    public String getPMPPrivacySettingValue(String privacySettingIdentifier, String appIdentifier) {
        return this.pmpci.getPrivacySettingValue(this.rgPackage, privacySettingIdentifier, appIdentifier);
    }
    
    
    /**
     * @see IPMPConnectionInterface#getContext(String)
     */
    public Context getContext() {
        return this.pmpci.getContext(this.rgPackage);
    }
    
    
    /**
     * 
     * @return the set package name for communication with PMP
     */
    public String getRgPackage() {
        return this.rgPackage;
    }
}
