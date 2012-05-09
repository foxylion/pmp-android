/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.connection.resource;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.Connection;

/**
 * The resource of the {@link ResourceGroup}
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionResource extends Resource {
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resource.Resource#getAndroidInterface(java.lang.String)
     */
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        Connection connectionRG = (Connection) getResourceGroup();
        return new ConnectionImpl(connectionRG.getContext(appIdentifier), connectionRG, appIdentifier);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resource.Resource#getMockedAndroidInterface(java.lang.String)
     */
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        Connection connectionRG = (Connection) getResourceGroup();
        return new ConnectionMockImpl(connectionRG, appIdentifier);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resource.Resource#getCloakedAndroidInterface(java.lang.String)
     */
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        Connection connectionRG = (Connection) getResourceGroup();
        return new ConnectionCloakImpl(connectionRG, appIdentifier);
    }
    
}
