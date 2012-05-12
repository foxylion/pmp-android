package de.unistuttgart.ipvs.pmp.model.conflicts;

import java.util.List;

/**
 * The {@link IConflictModel} holds all conflicts between presets.
 * An instance of the interface can be created by calling {@link ConflictModel#getInstance()}.
 * 
 * @author Jakob Jarosch
 */
public interface IConflictModel {
    
    /**
     * Calculates all conflicts between the presets, on a second call it updates only the changed Presets.
     * 
     * @param callback
     *            The callback is invoked during the calculation.
     */
    public void calculateConflicts(IProcessingCallback callback);
    
    
    /**
     * Returns true when the list of conflicts is up to date, otherwise false.
     * To bring the List up to date invoke {@link IConflictModel#calculateConflicts(IProcessingCallback)}.
     * 
     * @return Returns true when the list of conflicts is up to date, otherwise false.
     */
    public boolean isUpToDate();
    
    
    /**
     * @return Returns all conflict pairs.
     */
    public List<ConflictPair> getConflicts();
}
