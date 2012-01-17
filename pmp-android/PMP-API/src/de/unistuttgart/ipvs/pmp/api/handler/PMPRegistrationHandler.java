package de.unistuttgart.ipvs.pmp.api.handler;

import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPRegistrationCommand;

/**
 * Handles reactions for the {@link IPC2PMPRegistrationCommand}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPRegistrationHandler extends PMPHandler {
    
    /**
     * Called when the registration was successful.
     */
    public void onSuccess() {
    }
    
    
    /**
     * Called when the registration failed.
     * 
     * @param message
     *            the failure message
     */
    public void onFailure(String message) {
    }
    
    
    /**
     * Called when the registration cannot be completed because the app is already registered.
     */
    public void onAlreadyRegistered() {
    }
    
}
