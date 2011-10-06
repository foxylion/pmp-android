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
package de.unistuttgart.ipvs.pmp.service.app;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.app.AppInformationSetParcelable;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;

/**
 * Implementation of the {@link IAppService.Stub} stub.
 * 
 * @author Thorsten Berberich
 */
public class AppServicePMPStubImpl extends IAppServicePMP.Stub {
    
    /**
     * The {@link App} referenced.
     */
    private App app;
    
    
    public void setApp(App app) {
        this.app = app;
    }
    
    
    @Override
    public AppInformationSetParcelable getAppInformationSet() throws RemoteException {
        // Convert the AppInformationset into an AppInformationSetParcelable
        AppInformationSetParcelable appInfoSetParcelable = new AppInformationSetParcelable(this.app.getInfoSet());
        return appInfoSetParcelable;
    }
    
    
    @Override
    public void setActiveServiceLevel(int level) throws RemoteException {
        this.app.setActiveServiceLevel(level);
    }
    
    
    @Override
    public void setRegistrationState(RegistrationState state) throws RemoteException {
        if (state.getState()) {
            this.app.onRegistrationSuccess();
        } else {
            this.app.onRegistrationFailed(state.getMessage());
        }
    }
    
}
