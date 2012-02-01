package de.unistuttgart.ipvs.pmp.gui.preset;

import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

public interface DialogPresetEditCallback {
    
    public void openPreset(IPreset preset);
    
    
    public void refresh();
}
