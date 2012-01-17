package de.unistuttgart.ipvs.pmp.api.handler;

import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPUpdateServiceFeaturesCommand;

/**
 * Handles reactions for the {@link IPC2PMPUpdateServiceFeaturesCommand}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPServiceFeatureUpdateHandler extends PMPHandler {
    
    /**
     * Called when the service feature update has arrived.
     * 
     * @param serviceFeatures
     *            a bundle, mapping the service feature identifiers to a boolean that corresponds to their enabled state
     */
    public void onUpdate(Bundle serviceFeatures) {
    }
    
    
    /**
     * Called when the update request has failed e.g. because the app is not yet registered.
     */
    public void onUpdateFailed() {
    }
    
}
