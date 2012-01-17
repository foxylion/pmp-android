package de.unistuttgart.ipvs.pmp.api.handler;

import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPRequestServiceFeaturesCommand;

/**
 * Handles reactions for the {@link IPC2PMPRequestServiceFeaturesCommand}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPRequestServiceFeaturesHandler extends PMPHandler {
    
    /**
     * Called when the request failed, e.g. when the app is not registered yet.
     */
    public void onRequestFailed() {
    }
}
