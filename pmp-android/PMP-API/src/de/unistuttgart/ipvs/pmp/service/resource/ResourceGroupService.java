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

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupApp;
import de.unistuttgart.ipvs.pmp.service.NullServiceStubImpl;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * <p>
 * This is an external service for communication between resource groups and PMP or apps. <br/>
 * 
 * <p>
 * If you are PMP, you will then receive {@link IResourceGroupServicePMP}; if you are an app authorized by PMP you will
 * receive {@link IResourceGroupServiceApp}.
 * </p>
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupService extends PMPSignedService {
    
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceGroup rg = findContextResourceGroup();
        if (rg == null) {
            // invalid context
            Log.e(toString() + " tried to connect to its resource group and failed.");
        }
    }
    
    
    @Override
    protected PMPSignee createSignee() {
        ResourceGroup rg = findContextResourceGroup();
        if (rg == null) {
            // invalid context
            Log.e(toString() + " tried to connect to its resource group and failed.");
            // die with NullPointerExceptions instead of confusing everyone
            return null;
        } else {
            return rg.getSignee();
        }
    }
    
    
    /**
     * Called on startup of the service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        /*
         * We want this service to continue running until it is explicitly
         * stopped, so return sticky.
         */
        return START_STICKY;
    }
    
    
    @Override
    public IBinder onSignedBind(Intent intent) {
        ResourceGroup rg = findContextResourceGroup();
        if (rg == null) {
            // invalid context
            Log.e(toString() + " tried to connect to its resource group and failed.");
            return new NullServiceStubImpl(
                    "[Internal Failure] The ResourceGroup is null, can not accept any connection.");
        }
        
        PMPComponentType boundType = (PMPComponentType) intent.getSerializableExtra(Constants.INTENT_TYPE);
        String boundIdentifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
        
        if (boundType.equals(PMPComponentType.PMP)) {
            ResourceGroupServicePMPStubImpl rgspmpsi = new ResourceGroupServicePMPStubImpl();
            rgspmpsi.setResourceGroup(rg);
            return rgspmpsi;
            
        } else if (boundType.equals(PMPComponentType.APP)) {
            ResourceGroupServiceAppStubImpl rgsasi = new ResourceGroupServiceAppStubImpl();
            rgsasi.setResourceGroup(rg);
            rgsasi.setAppIdentifier(boundIdentifier);
            return rgsasi;
            
        } else {
            // wait, what?
            Log.d("Received a signed bind with unknown TYPE, returning NullService");
            return new NullServiceStubImpl("The bound Type is does not allow any Service at the ResourceGroupService");
        }
    }
    
    
    @Override
    public IBinder onUnsignedBind(Intent intent) {
        // go away, I don't like you!
        Log.d("Received an unsigned bind, returning NullService");
        return new NullServiceStubImpl("The ResourceGroupService does not allow any unsigned connection");
    }
    
    
    private ResourceGroup findContextResourceGroup() {
        if (!(getApplication() instanceof ResourceGroupApp)) {
            Log.e("ResourceGroupService finds its app to be " + getApplication().toString()
                    + ", should be ResourceGroupApp");
            return null;
        } else {
            ResourceGroupApp rga = (ResourceGroupApp) getApplication();
            return rga.getResourceGroupForService(this);
        }
    }
}
