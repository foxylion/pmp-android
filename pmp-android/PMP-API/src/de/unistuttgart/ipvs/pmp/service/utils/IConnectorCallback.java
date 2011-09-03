package de.unistuttgart.ipvs.pmp.service.utils;

import android.app.Service;

/**
 * {@link IConnectorCallback} is used in {@link AbstractConnector}s to get
 * informed when a Service has connected after the binding command.
 * 
 * @author Jakob Jarosch
 */
public interface IConnectorCallback {

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
