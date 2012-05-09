package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

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
