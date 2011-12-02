/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.service;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;

/**
 * Implementation of the {@link IPMPServiceApp.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceStubImpl extends IPMPService.Stub {
    
    @Override
    public boolean getServiceFeatureUpdate(String identifier) throws RemoteException {
        
        IApp app = Model.getInstance().getApp(identifier);
        if (app == null) {
            return false;
        } else {
            
            app.verifyServiceFeatures();
            return true;
        }
    }
    
    
    @Override
    public void registerApp(String identifier) throws RemoteException {
        Model.getInstance().registerApp(identifier);
    }
    
    
    @Override
    public boolean isRegistered(String identifier) throws RemoteException {
        return Model.getInstance().getApp(identifier) != null;
    }
    
    
    @Override
    public IBinder getRessource(String identifier, String resourceGroup, String resource) throws RemoteException {
        IResourceGroup rg = Model.getInstance().getResourceGroup(resourceGroup);
        if (rg == null) {
            return null;
        } else {
            return rg.getResource(identifier, resource);
        }
    }
    
}
