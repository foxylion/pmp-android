package de.unistuttgart.ipvs.pmp.model.conflicts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * Implementation of the {@link IConflictModel}.
 * 
 * @author Jakob Jarosch
 */
public class ConflictModel implements IConflictModel {
    
    private static final ConflictModel instance = new ConflictModel();
    
    /**
     * List holds all hashes from the Presets used in the last update.
     */
    private Map<IPreset, String> lastUpdatedHashes = new HashMap<IPreset, String>();
    
    /**
     * List holds all conflicting pairs found during the last calculation.
     */
    private List<ConflictPair> conflictPairs = new ArrayList<ConflictPair>();
    
    
    /**
     * Private constructor, singleton pattern.
     */
    private ConflictModel() {
    }
    
    
    /**
     * @return Returns the one and only instance of the {@link IConflictModel} implmentation.
     */
    public static IConflictModel getInstance() {
        return instance;
    }
    
    
    @Override
    public void calculateConflicts(IProcessingCallback callback) {
        new ConflictCalculator(callback).start();
    }
    
    
    @Override
    public boolean isUpToDate() {
        
        for (IPreset preset : ModelProxy.get().getPresets()) {
            if (!preset.toString().equals(lastUpdatedHashes.get(preset))) {
                return false;
            }
        }
        
        return true;
    }
    
    
    @Override
    public List<ConflictPair> getConflicts() {
        return conflictPairs;
    }
    
    /**
     * Internal thread for conflict calculation.
     * 
     * @author Jakob Jarosch
     */
    class ConflictCalculator extends Thread {
        
        private IProcessingCallback callback;
        
        
        public ConflictCalculator(IProcessingCallback callback) {
            this.callback = callback;
            
            /* Prevent from NullPointerException */
            if (this.callback == null) {
                this.callback = new NullProcessingCallback();
            }
        }
        
        
        @Override
        public void run() {
            this.callback.stepMessage("Checking for updated Presets...");
            
            /* temporary store all to be updated presets. */
            List<IPreset> presets = ModelProxy.get().getPresets();
            List<IPreset> updatedPresets = new ArrayList<IPreset>();
            int currentCount = 0;
            int totalCount = presets.size();
            
            for (IPreset preset : presets) {
                currentCount++;
                this.callback.progressUpdate(currentCount, totalCount);
                
                String presetString = preset.toString();
                if (!presetString.equals(lastUpdatedHashes.get(preset))) {
                    updatedPresets.add(preset);
                    
                    /* directly update the stored hash, following execution will update the conflicts */
                    lastUpdatedHashes.put(preset, presetString);
                    
                    /* Remove all conflict pairs where the updated preset is inside. */
                    Iterator<ConflictPair> iter = conflictPairs.iterator();
                    while (iter.hasNext()) {
                        if (iter.next().inPair(preset)) {
                            iter.remove();
                        }
                    }
                }
            }
            
            this.callback.stepMessage("Checking for possible conflicts...");
            /* now iterating over all updated presets and compare them with all others */
            currentCount = 0;
            totalCount = updatedPresets.size() * presets.size();
            
            for (IPreset preset : updatedPresets) {
                for (IPreset comparedPreset : presets) {
                    if (comparedPreset.equals(preset)) {
                        continue;
                    }
                    
                    currentCount++;
                    this.callback.progressUpdate(currentCount, totalCount);
                    
                    /* For optimization skip all already known conflicts. */
                    if (!conflictPairs.contains(new ConflictPair(preset, comparedPreset))) {
                        if (preset.getConflictingPrivacySettings(comparedPreset).size() > 0
                                || preset.getConflictingContextAnnotations(comparedPreset).size() > 0) {
                            conflictPairs.add(new ConflictPair(preset, comparedPreset));
                        }
                    }
                }
            }
            
            this.callback.finished();
        }
    }
}
