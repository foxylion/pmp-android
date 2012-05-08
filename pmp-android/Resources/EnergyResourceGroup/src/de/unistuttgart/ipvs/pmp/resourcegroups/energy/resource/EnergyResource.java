package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;

public class EnergyResource extends Resource {
    
    private ResourceGroup rg;
    
    
    public EnergyResource(ResourceGroup rg) {
        this.rg = rg;
    }
    
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        EnergyConstants.APP_IDENTIFIER = appIdentifier;
        return new EnergyImpl(this.rg);
    }
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        EnergyConstants.APP_IDENTIFIER = appIdentifier;
        return new EnergyImplMock(this.rg);
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        EnergyConstants.APP_IDENTIFIER = appIdentifier;
        return new EnergyImplCloak(this.rg);
    }
    
}
