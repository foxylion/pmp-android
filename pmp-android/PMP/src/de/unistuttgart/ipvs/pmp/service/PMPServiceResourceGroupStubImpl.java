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
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceResourceGroup;

;

/**
 * Implementation of the {@link IPMPServiceResourceGroup.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceResourceGroupStubImpl extends IPMPServiceResourceGroup.Stub {
    
    //private String identifier = null;
    
    public PMPServiceResourceGroupStubImpl(String identifier) {
        //this.identifier = identifier;
    }
    
    
    @Override
    public void savePrivacyLevel(String preset, String privacyLevel, String value) throws RemoteException {
        
        /*
         * Note: The Identifier of the ResourceGroup can be found in the
         * identifier variable.
         */
        
        // TODO IMPLEMENT
        throw new UnsupportedOperationException();
    }
}
