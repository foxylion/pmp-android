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

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPService;
import de.unistuttgart.ipvs.pmp.service.pmp.RegistrationResult;

/**
 * Implementation of the {@link IPMPServiceApp.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceImplementation extends IPMPService.Stub {
    
    @Override
    public boolean getServiceFeatureUpdate(String appPackage) throws RemoteException {
        
        IApp app = Model.getInstance().getApp(appPackage);
        if (app == null) {
            return false;
        } else {
            app.verifyServiceFeatures();
            return true;
        }
    }
    
    
    @Override
    public RegistrationResult registerApp(String appPackage) throws RemoteException {
        // TODO!
        try {
            if (Model.getInstance().getApp(appPackage) == null) {
                Model.getInstance().registerApp(appPackage);
            }
        } catch (InvalidXMLException e) {
            // if desired one could inform the GUI here
        }
        
        return new RegistrationResult(false, "TODO");
    }
    
    
    @Override
    public boolean isRegistered(String appPackage) throws RemoteException {
        return Model.getInstance().getApp(appPackage) != null;
    }
    
    
    @Override
    public IBinder getResource(String appPackage, String rgPackage, String resource) throws RemoteException {
        IResourceGroup rg = Model.getInstance().getResourceGroup(rgPackage);
        if (rg == null) {
            return null;
        } else {
            return rg.getResource(appPackage, resource);
        }
    }
    
    
    @Override
    public boolean requestServiceFeature(String appPackage, String[] requiredServiceFeatures) throws RemoteException {
        IApp app = Model.getInstance().getApp(appPackage);
        if (app == null) {
            return false;
        } else {
            Intent intent = GUITools.createAppActivityIntent(app);
            intent.putExtra(GUIConstants.ACTIVITY_ACTION, GUIConstants.CHANGE_SERVICEFEATURE);
            intent.putExtra(GUIConstants.REQUIRED_SERVICE_FEATURE, requiredServiceFeatures);
            GUITools.startIntent(intent);
            
            return true;
        }
    }
    
}
