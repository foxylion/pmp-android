package de.unistuttgart.ipvs.pmp.api.handler;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;

public class PMPRequestResourceHandler extends PMPHandler {
    
    public void onReceiveResource(PMPResourceIdentifier res, IBinder resource) {
        throw new UnsupportedOperationException();
    }
}
