package de.unistuttgart.ipvs.pmp.gui.preset;

import java.io.Serializable;
import java.util.Comparator;

import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * This is a comparator for Presets.
 * sort priority: Name of the Preset
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetComparator implements Comparator<IPreset>, Serializable {
    
    private static final long serialVersionUID = -8830062958226213627L;
    
    
    @Override
    public int compare(IPreset lhs, IPreset rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }
}
