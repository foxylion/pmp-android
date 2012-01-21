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
package de.unistuttgart.ipvs.pmp.service.utils;

import android.app.Service;
import android.os.RemoteException;

/**
 * {@link AbstractConnectorCallback} is used in {@link AbstractConnector}s to get informed when a Service has connected
 * after
 * the binding command.
 * 
 * @param T
 *            the type of connector to be used in the callbacks.
 * 
 * @author Jakob Jarosch
 */
@Deprecated
public abstract class AbstractConnectorCallback {
    
    /**
     * Is called when the {@link Service} has connected.
     * 
     * @param connector
     *            the {@link AbstractConnector} from which this method was invoked
     */
    @Deprecated
    public void onConnect(AbstractConnector connector) throws RemoteException {
    }
    
    
    /**
     * Is called when the {@link Service} disconnected.
     * 
     * @param connector
     *            the {@link AbstractConnector} from which this method was invoked
     */
    @Deprecated
    public void onDisconnect(AbstractConnector connector) {
    }
    
    
    /**
     * Is called when the binding of the {@link Service} failed.
     * 
     * @param connector
     *            the {@link AbstractConnector} from which this method was invoked
     */
    @Deprecated
    public void onBindingFailed(AbstractConnector connector) {
        // the only time this is called there is already a log written from the AbstractConnector
    }
    
}
