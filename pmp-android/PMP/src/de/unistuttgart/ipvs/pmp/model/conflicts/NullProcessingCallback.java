package de.unistuttgart.ipvs.pmp.model.conflicts;

/**
 * Null implementation of the {@link IProcessingCallback}.
 * 
 * @author Jakob Jarosch
 */
public class NullProcessingCallback implements IProcessingCallback {
    
    @Override
    public void progressUpdate(int completed, int fullCount) {
    }
    
    
    @Override
    public void stepMessage(String message) {
    }
    
    
    @Override
    public void finished() {
    }
    
}
