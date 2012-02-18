package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.util.List;

public interface IPresetSet {
    
    /**
     * Get all Presets
     * 
     * @return list with all Presets
     */
    public abstract List<IPreset> getPresets();
    
    
    /**
     * Add a Preset
     * 
     * @param preset
     *            Preset to add
     */
    public abstract void addPreset(IPreset preset);
    
    
    /**
     * Remove a Preset
     * 
     * @param preset
     *            Preset to remove
     */
    public abstract void removePreset(IPreset preset);
    
}
