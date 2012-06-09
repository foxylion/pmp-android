/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

/**
 * This is the energy resource
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyResource extends Resource {
    
    private ResourceGroup rg;
    
    
    public EnergyResource(ResourceGroup rg) {
        this.rg = rg;
    }
    
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        return new EnergyImpl(this.rg, appIdentifier);
    }
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        return new EnergyImplMock(this.rg, appIdentifier);
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        return new EnergyImplCloak(this.rg, appIdentifier);
    }
    
}
