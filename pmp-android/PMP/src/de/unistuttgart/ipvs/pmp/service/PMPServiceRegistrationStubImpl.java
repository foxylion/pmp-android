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

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;

/**
 * Implementation of the {@link IPMPServiceRegistration.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceRegistrationStubImpl extends IPMPServiceRegistration.Stub {
    
    private String identifier = null;
    
    
    public PMPServiceRegistrationStubImpl(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public byte[] registerApp(final byte[] publicKey) throws RemoteException {
        if (this.identifier == null || this.identifier.length() == 0) {
            Log.e("An App tried to register at PMPService but the identifier was NULL or a String with length 0");
            return null;
        }
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                ModelSingleton.getInstance().getModel()
                        .addApp(PMPServiceRegistrationStubImpl.this.identifier, publicKey);
            }
        }).start();
        
        return PMPApplication.getSignee().getLocalPublicKey();
    }
    
    
    @Override
    public byte[] registerResourceGroup(final byte[] publicKey) throws RemoteException {
        if (this.identifier == null || this.identifier.length() == 0) {
            Log.e("An ResourceGroup tried to register at PMPService but the identifier was NULL or a String with length 0");
            return null;
        }
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                ModelSingleton.getInstance().getModel()
                        .addResourceGroup(PMPServiceRegistrationStubImpl.this.identifier, publicKey);
            }
        }).start();
        
        return PMPApplication.getSignee().getLocalPublicKey();
    }
}
