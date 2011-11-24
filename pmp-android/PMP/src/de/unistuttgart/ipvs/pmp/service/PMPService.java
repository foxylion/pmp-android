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
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceApp;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

/**
 * <p>
 * External service for communication between PMP, the {@link IResourceGroup} and {@link IApp}. <br>
 * <b>Normally you will use the {@link PMPServiceConnector} for connection the {@link PMPService}. </b>
 * </p>
 * 
 * <p>
 * The Service requires several informations in the intent, used to bind the {@link PMPService}, put as extra into the
 * {@link Intent}.
 * </p>
 * 
 * <pre>
 * intent.putExtraString(Constants.INTENT_TYPE, PMPComponentType.*.toString());
 * intent.putExtraString(Constants.INTENT_IDENTIFIER, &lt;App/ResourceGroup-Identifier>);
 * intent.putExtraByteArray(Constants.INTENT_SIGNATURE, {@link PMPSignee} signing {@link PMPService} identifier);
 * </pre>
 * 
 * <p>
 * The signature is optional, if you do not sent a signature, the Service will handle the binding as an registration and
 * gives back the {@link IPMPServiceRegistration} Binder.<br/>
 * With a valid token the {@link IPMPServiceResourceGroup} or {@link IPMPServiceApp} Binder will be given back.<br>
 * 
 * If an authentification fails the Service will give back null.
 * </p>
 * 
 * @author Jakob Jarosch
 */
public class PMPService extends Service {
    
    @Override
    public IBinder onBind(Intent intent) {
        String identifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
        
        return new PMPServiceAppStubImpl(identifier);
    }
    
}
