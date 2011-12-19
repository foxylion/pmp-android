package de.unistuttgart.ipvs.pmp.model.server;

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
     */
    void download(int position, int length);
    
    
    /**
     * Called for the amount of tasks (i.e. downloads) to be performed.
     * 
     * @param position
     *            the amount of tasks already completed
     * @param length
     *            the amount of tasks total, <b>or -1 if unknown</b>
     */
    void tasks(int position, int length);
    
}
