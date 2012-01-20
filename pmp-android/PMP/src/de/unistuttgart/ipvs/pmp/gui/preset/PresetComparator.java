package de.unistuttgart.ipvs.pmp.gui.preset;

import java.util.Comparator;

import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

public class PresetComparator implements Comparator<IPreset> {
    
    @Override
    public int compare(IPreset lhs, IPreset rhs) {
        if (lhs.isDeleted() && rhs.isDeleted()) {
            return lhs.getName().compareTo(rhs.getName());
        } else if (!lhs.isDeleted() && !rhs.isDeleted()) {
            return lhs.getName().compareTo(rhs.getName());
        } else if (!lhs.isDeleted() && rhs.isDeleted()) {
            return -1;
        } else if (lhs.isDeleted() && !rhs.isDeleted()) {
            return 1;
        }
        return 0;
    }
}
