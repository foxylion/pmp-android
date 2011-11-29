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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.model.PersistenceProvider;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

/**
 * <p>
 * External service for communication between PMP and the {@link App}. <br>
 * <b>Normally you will use the {@link PMPServiceConnector} for connection the {@link PMPService}. </b>
 * </p>
 * 
 * @author Jakob Jarosch
 */
public class PMPService extends Service {
    
    @Override
    public IBinder onBind(Intent intent) {
        return new PMPServiceStubImpl();
    }
    
}
