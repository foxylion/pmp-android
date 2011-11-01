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

import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;

/**
 * <p>
 * Easy-to-use {@link ResourceGroupApp}: if you're writing an App that only requires one {@link ResourceGroup}. Just put
 * your resource group class as generic parameter, override {@link ResourceGroupSingleApp#createResourceGroup()} to
 * return an instance of your resource group (it will be automatically generated when Android calls onCreate() then).
 * Note that you may need to load data upon creation or store data when onDestroy() gets called.
 * </p>
 * 
 * <p>
 * And finally use getResourceGroup().start(); to register your resource with PMP, if necessary. You may want to already
 * do this in {@link ResourceGroupSingleApp#createResourceGroup()}. That's all there is to it.
 * </p>
 * 
 * 
 * @param T
 *            the ResourceGroup class you want to store.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroupSingleApp<T extends ResourceGroup> extends ResourceGroupApp {
    
    /**
     * Stored {@link ResourceGroup}.
     */
    private T resourceGroup = null;
    
    
    @Override
    public void onCreate() {
        this.resourceGroup = createResourceGroup();
        this.resourceGroup.initPrivacyLevelValues();
    }
    
    
    /**
     * Create an instance of your resource group here.
     * 
     * @return an instance of T.
     */
    protected abstract T createResourceGroup();
    
    
    /**
     * Sets the resourceGroup
     * 
     * @param resourceGroup
     */
    public void setResourceGroup(T resourceGroup) {
        this.resourceGroup = resourceGroup;
    }
    
    
    /**
     * 
     * @return the resourceGroup
     */
    public T getResourceGroup() {
        return this.resourceGroup;
    }
    
    
    @Override
    public ResourceGroup getResourceGroupForService(ResourceGroupService rgs) {
        return this.resourceGroup;
    }
    
    
    @Override
    public ResourceGroup[] getAllResourceGroups() {
        return new ResourceGroup[] { this.resourceGroup };
    }
    
}
