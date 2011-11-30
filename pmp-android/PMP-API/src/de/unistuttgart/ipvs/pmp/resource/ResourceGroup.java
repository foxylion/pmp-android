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

import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;

/**
 * <p>
 * A resource group that bundles {@link Resource}s and {@link PrivacyLevel}s. You can register them by using the methods
 * {@link ResourceGroup#registerResource(String, Resource)} and
 * {@link ResourceGroup#registerPrivacyLevel(String, PrivacyLevel)}.
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
     * The resources present in that resource group.
     */
    private final Map<String, Resource> resources;
    
    /**
     * The privacy levels present in that resource group.
     */
    private final Map<String, PrivacyLevel<?>> privacyLevels;
    
    
    /**
     * Creates a new {@link ResourceGroup}.
     * 
     * @param serviceContext
     *            context of the service for this resource group
     */
    public ResourceGroup() {
        this.resources = new HashMap<String, Resource>();
        this.privacyLevels = new HashMap<String, PrivacyLevel<?>>();
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
     * Registers privacyLevel as privacy level "identifier" in this resource group.
     * 
     * @param identifier
     * @param privacyLevel
     */
    public void registerPrivacyLevel(String identifier, PrivacyLevel<?> privacyLevel) {
        this.privacyLevels.put(identifier, privacyLevel);
    }
    
    
    /**
     * 
     * @param identifier
     * @return the privacy level identified by "identifier", if present, null otherwise
     */
    public PrivacyLevel<?> getPrivacyLevel(String identifier) {
        return this.privacyLevels.get(identifier);
    }
    
    
    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getPrivacyLevels() {
        return new ArrayList<String>(this.privacyLevels.keySet());
    }
    
}
