package de.unistuttgart.ipvs.pmp.api.handler;

import de.unistuttgart.ipvs.pmp.api.ipc.command.IPCUpdateSFCommand;
import android.os.Bundle;

public class PMPServiceFeatureUpdateHandler extends PMPHandler {
    
    public IPCUpdateSFCommand handler;
    
    
    protected void onUpdate(Bundle sfs) {
        throw new UnsupportedOperationException();
    }
}
