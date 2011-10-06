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

import android.app.Application;
import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;

/**
 * The default resource group app to implement for creating an App that stores {@link ResourceGroup}s.
 * 
 * You can use {@link ResourceGroupSingleApp}, if you intend to store only one {@link ResourceGroup}.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroupApp extends Application {
    
    /**
     * Fetches the applicable {@link ResourceGroup} for an instance of an {@link ResourceGroupService}.
     * 
     * @param rgs
     *            the service asking for the resourcegroup
     * @return the {@link ResourceGroup} associated with that service
     */
    public abstract ResourceGroup getResourceGroupForService(ResourceGroupService rgs);
    
    
    /**
     * Fetches all {@link ResourceGroup}s held by this App.
     * 
     * @return all the {@link ResourceGroup}s that could possibly returned by
     *         {@link ResourceGroupApp#getResourceGroupForService(ResourceGroupService)}
     */
    public abstract ResourceGroup[] getAllResourceGroups();
    
}
