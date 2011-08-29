package de.unistuttgart.ipvs.pmp.service.helper;

import android.app.Service;

/**
 * {@link ICallback} is used to get informed when a Service has connected after
 * the binding command.
 * 
 * @author Jakob Jarosch
 */
public interface ICallback {

    /**
     * Is called when the {@link Service} has connected.
     */
    public void connected();

    /**
     * Is called when the binding of the {@link Service} failed.
     */
    public void bindingFailed();

    /**
     * Is called when the {@link Service} disconnected.
     */
    public void disconnected();
}
