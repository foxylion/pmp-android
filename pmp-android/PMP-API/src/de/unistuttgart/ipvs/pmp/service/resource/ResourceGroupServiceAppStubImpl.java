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
package de.unistuttgart.ipvs.pmp.service.resource;

import java.util.List;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

/**
 * Implementation of the {@link IResourceGroupServiceApp.Stub} stub.
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupServiceAppStubImpl extends IResourceGroupServiceApp.Stub {
    
    /**
     * {@link ResourceGroup} referenced.
     */
    private ResourceGroup rg;
    
    /**
     * The app referenced.
     */
    private String appIdentifier;
    
    
    public void setResourceGroup(ResourceGroup rg) {
        this.rg = rg;
    }
    
    
    public void setAppIdentifier(String identifier) {
        this.appIdentifier = identifier;
    }
    
    
    @SuppressWarnings("rawtypes")
    @Override
    public List getResources() throws RemoteException {
        return this.rg.getResources();
    }
    
    
    @Override
    public IBinder getResource(String resourceIdentifier) throws RemoteException {
        Resource resource = this.rg.getResource(resourceIdentifier);
        if (resource == null) {
            return null;
        } else {
            return resource.getAndroidInterface(this.appIdentifier);
        }
    }
    
}
