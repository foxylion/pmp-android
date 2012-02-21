package de.unistuttgart.ipvs.pmp.model.xml;

import java.util.List;

import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

/**
 * Interface for the {@link Model} to the PMP-XML-UTILITIES
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IXMLInterface {
    
    /**
     * Exports the selected presets into an {@link IPreset}.
     * 
     * @param presets
     * @return
     */
    public IPresetSet exportPresets(List<IPreset> presets);
    
    
    /**
     * Imports the selected presets into the Model.
     * 
     * @param presets
     * @param override
     *            if existing presets shall be overridden or a new preset shall be generated
     */
    public void importPresets(List<de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset> presets, boolean override)
            throws InvalidPresetSetException;
    
}
