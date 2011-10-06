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
package de.unistuttgart.ipvs.pmp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.service.app.AppService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * This service encapsulates all the dirty signee stuff necessary for authentication of services.
 * 
 * This service requires several informations in the intent, used to bind the {@link PMPService} or the
 * {@link AppService}, put as extra into the {@link Intent}. </p>
 * 
 * <pre>
 * intent.putExtraString(Constants.INTENT_IDENTIFIER, &lt;App/PMP-Identifier>);
 * intent.putExtraString(Constants.INTENT_TYPE, &lt;APP|PMP>);
 * intent.putExtraByteArray(Constants.INTENT_SIGNATURE, PMPSignee signed identifier);
 * </pre>
 * 
 * *
 * <p>
 * Use {@link PMPSignee#signContent(byte[])} with the identifier you're sending the Intent to (likely
 * targetIdentifier.getBytes()). Transmit the result of the signing in "signee".
 * </p>
 * 
 * <p>
 * If you need to have access to a correct copy (one which changes will be reflected in the service and vice versa)
 * create one and override {@link PMPSignedService#createSignee()} to copy that signee to this service (return the
 * signee). Note that signees should be consistently the same object!
 * </p>
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class PMPSignedService extends Service {
    
    /**
     * The signee used for this service.
     */
    private PMPSignee signee;
    
    
    /**
     * Overwrite this method to use your own signee object.
     * 
     * @return a reference to a signee for the service to use.
     */
    protected abstract PMPSignee createSignee();
    
    
    @Override
    public void onCreate() {
        this.signee = createSignee();
    }
    
    
    @Override
    public void onDestroy() {
    }
    
    
    @Override
    public final IBinder onBind(Intent intent) {
        PMPComponentType boundType = (PMPComponentType) intent.getSerializableExtra(Constants.INTENT_TYPE);
        String boundIdentifier = intent.getStringExtra(Constants.INTENT_IDENTIFIER);
        byte[] boundSignature = intent.getByteArrayExtra(Constants.INTENT_SIGNATURE);
        
        // debug code
        if (boundSignature == null) {
            Log.d(toString() + " received a NULL signature from " + boundType + "::" + boundIdentifier);
        }
        
        // actual check
        if ((boundSignature != null)
                && (this.signee.isSignatureValid(boundType, boundIdentifier, this.signee.getIdentifier().getBytes(),
                        boundSignature))) {
            return onSignedBind(intent);
        } else {
            return onUnsignedBind(intent);
        }
    }
    
    
    /**
     * 
     * @return the signee used for signing messages.
     */
    public final PMPSignee getSignee() {
        return this.signee;
    }
    
    
    /**
     * onBind() callback for all trusted and correctly signed messages.
     * 
     * @see {@link Service#onBind(Intent)}
     */
    public abstract IBinder onSignedBind(Intent intent);
    
    
    /**
     * onBind() callback for all messages of unknown origin.
     * 
     * @see {@link Service#onBind(Intent)}
     */
    public abstract IBinder onUnsignedBind(Intent intent);
    
}
