package de.unistuttgart.ipvs.pmp.gui.preset;

import java.util.Comparator;

import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * This is a comparator for Presets.
 * sort priority: Name of the Preset
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetComparator implements Comparator<IPreset> {
    
    @Override
    public int compare(IPreset lhs, IPreset rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }
}
