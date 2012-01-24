package de.unistuttgart.ipvs.pmp.api.handler;

import de.unistuttgart.ipvs.pmp.api.ipc.command.IPC2PMPRegistrationCommand;

/**
 * Handles reactions for the {@link IPC2PMPRegistrationCommand}. Handlers are guaranteed to be called in a separate
 * {@link Thread}.
 * 
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPRegistrationHandler extends PMPHandler {
    
    /**
     * Called when the registration cannot be completed because the app is already registered.
     */
    public void onAlreadyRegistered() {
    }
    
    
    /**
     * Called when the registration will start, i.e. before the actual registration takes place.
     */
    public void onRegistration() {
    }
    
    
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
    
}
