package de.unistuttgart.ipvs.pmp.model.server;

import java.io.ObjectInputStream;

/**
 * Interface for implementing callbacks when the {@link ServerProvider} is currently working.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IServerDownloadCallback {
    
    /**
     * Called for the progress during one download.
     * 
     * @param position
     *            the amount of bytes already read
     * @param length
     *            the amount of bytes total
     * @deprecated pretty much impossible to use with {@link ObjectInputStream}.
     */
    @Deprecated
    void download(int position, int length);
    
    
    /**
     * Called for the amount of steps to be performed.
     * 
     * @param position
     *            the amount of steps already completed
     * @param length
     *            the amount of steps total
     */
    void step(int position, int length);
    
}
