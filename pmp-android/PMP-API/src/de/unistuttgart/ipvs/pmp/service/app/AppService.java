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

import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.service.NullServiceStubImpl;
import de.unistuttgart.ipvs.pmp.service.PMPSignedService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * The {@link AppService} is used to allow PMPService a connection where informations about the App are served.
 * 
 * @author Jakob Jarosch
 */
public class AppService extends PMPSignedService {
    
    @Override
    protected PMPSignee createSignee() {
        return findContextApp().getSignee();
    }
    
    
    @Override
    public IBinder onSignedBind(Intent intent) {
        PMPComponentType type = (PMPComponentType) intent.getSerializableExtra(Constants.INTENT_TYPE);
        
        if (type == PMPComponentType.PMP) {
            AppServicePMPStubImpl assi = new AppServicePMPStubImpl();
            assi.setApp(findContextApp());
            return assi;
        } else {
            return new NullServiceStubImpl("The bound Type is does not allow any Service at the AppService");
        }
    }
    
    
    @Override
    public IBinder onUnsignedBind(Intent intent) {
        return new NullServiceStubImpl("The AppService does not allow any unsigned connection");
    }
    
    
    private App findContextApp() {
        if (!(getApplication() instanceof App)) {
            return null;
        } else {
            return (App) getApplication();
        }
    }
}
