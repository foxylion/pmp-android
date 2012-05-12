package de.unistuttgart.ipvs.pmp.model.conflicts;

/**
 * The {@link IProcessingCallback} is used to inform the caller about the current progress of conflict calculation. The
 * finished method is invoked when the calculation process is ended and the conflict list is up to date.
 * 
 * @author Jakob Jarosch
 */
public interface IProcessingCallback {
    
    /**
     * Is invoked when the next Preset is being processed.
     * 
     * @param completed
     *            Number of completed tasks.
     * @param fullCount
     *            Number of all tasks.
     */
    public void progressUpdate(int completed, int fullCount);
    
    
    /**
     * Message which informs the user about the current step in calculation process.
     * 
     * @param message
     *            Internationalized message.
     */
    public void stepMessage(String message);
    
    
    /**
     * The method is called on completion of calculation.
     */
    public void finished();
}
