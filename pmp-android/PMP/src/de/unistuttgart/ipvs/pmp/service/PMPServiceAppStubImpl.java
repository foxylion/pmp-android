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
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;

/**
 * Implementation of the {@link IPMPServiceApp.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class PMPServiceAppStubImpl extends IPMPServiceApp.Stub {
    
    private String identifier = null;
    
    
    public PMPServiceAppStubImpl(String identifier) {
        this.identifier = identifier;
    }
    
    
    @Override
    public void getInitialServiceLevel() throws RemoteException {
        /*Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(PMPApplication.getContext(), ServiceLvlActivity.class);
        intent.putExtra(Constants.INTENT_IDENTIFIER, this.identifier);
        PMPApplication.getContext().startActivity(intent);*/
        // TODO
    }
}
