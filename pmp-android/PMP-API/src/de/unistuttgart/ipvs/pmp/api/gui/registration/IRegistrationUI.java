package de.unistuttgart.ipvs.pmp.api.gui.registration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Interface for an equivalent use of methods for UI-operations in {@link Dialog}s and {@link Activity}s.
 * 
 * @author Jakob Jarosch
 * 
 */
public interface IRegistrationUI {
    
    /**
     * @see Dialog#getContext()
     */
    public Context getContext();
    
    
    /**
     * @see View#findViewById(int)
     */
    public View findViewById(int resource);
    
    
    /**
     * Invokes an event on the given ui.
     * 
     * @param eventType
     *            Type of the event which should be invoked.
     * @param parameters
     *            Parameters as for example an error message.
     */
    public void invokeEvent(final RegistrationEventTypes eventType, final Object... parameters);
    
    
    /**
     * Closes the UI.
     */
    public void close();
    
}
