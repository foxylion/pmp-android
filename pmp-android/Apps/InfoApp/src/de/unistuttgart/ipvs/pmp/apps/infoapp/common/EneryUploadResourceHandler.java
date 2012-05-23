/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import java.util.concurrent.Semaphore;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;

/**
 * Upload Handler for the energy rg
 * 
 * @author Thorsten Berberich, Marcus Vetter
 * 
 */
public class EneryUploadResourceHandler extends AbstractRequestRessourceHandler {
    
    /**
     * Constructor
     * 
     * @param sem
     *            {@link Semaphore}
     */
    public EneryUploadResourceHandler(Semaphore sem) {
        super(sem);
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        IEnergy energyRG = IEnergy.Stub.asInterface(binder);
        try {
            this.setURL(energyRG.uploadData());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        sem.release();
    }
    
}
