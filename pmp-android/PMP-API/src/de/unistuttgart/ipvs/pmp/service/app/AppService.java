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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.app.App;

/**
 * The {@link AppService} is used to provide PMPService with a connection to the app where the information about the app
 * is stored.
 * 
 * @author Jakob Jarosch
 */
public class AppService extends Service {
    
    @Override
    public IBinder onBind(Intent intent) {
        AppServiceStubImpl assi = new AppServiceStubImpl();
        assi.setApp(findContextApp());
        return assi;
    }
    
    
    private App findContextApp() {
        if (!(getApplication() instanceof App)) {
            throw new RuntimeException("AppService started without appropriate App class in getApplication().");
        } else {
            return (App) getApplication();
        }
    }
    
}
