package de.unistuttgart.ipvs.pmp.model.xml;

import java.util.List;

import de.unistuttgart.ipvs.pmp.model.IModel;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.contextannotation.IContextAnnotation;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.Preset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

/**
 * @see IXMLInterface
 * @author Tobias Kuhn
 * 
 */
public class XMLInterface implements IXMLInterface {
    
    @Override
    public PresetSet exportPresets(List<IPreset> presets) {
        PresetSet result = new PresetSet();
        
        // each preset
        for (IPreset preset : presets) {
            Preset xmlPreset = new Preset(preset.getLocalIdentifier(), preset.getCreator().getIdentifier(),
                    preset.getName(), preset.getDescription());
            
            // each assigned app
            for (IApp app : preset.getAssignedApps()) {
                xmlPreset.addAssignedApp(new PresetAssignedApp(app.getIdentifier()));
            }
            
            // each privacy setting
            for (IPrivacySetting ps : preset.getGrantedPrivacySettings()) {
                PresetAssignedPrivacySetting paps = new PresetAssignedPrivacySetting(ps.getResourceGroup()
                        .getIdentifier(), String.valueOf(ps.getResourceGroup().getRevision()), ps.getLocalIdentifier(),
                        preset.getGrantedPrivacySettingValue(ps));
                
                // each context annotation
                for (IContextAnnotation ca : preset.getContextAnnotations(ps)) {
                    paps.addContext(new PresetPSContext(ca.getContext().getIdentifier(), ca.getContextCondition(), ca
                            .getOverridePrivacySettingValue()));
                }
                
                xmlPreset.addAssignedPrivacySetting(paps);
            }
            
            result.addPreset(xmlPreset);
        }
        
        return result;
    }
    
    
    @Override
    public void importPresets(List<Preset> presets, boolean override) throws InvalidPresetSetException {
        
        IModel m = Model.getInstance();
        
        // each preset
        for (Preset preset : presets) {
            IModelElement creator = m.getApp(preset.getCreator());
            if (creator == null) {
                creator = m.getResourceGroup(preset.getCreator());
            }
            IPreset existing = m.getPreset(creator, preset.getIdentifier());
            
            // add one to the model according to semantics
            IPreset toWrite = null;
            
            if (existing != null) {
                // already exists
                
                if (override) {
                    m.removePreset(existing.getCreator(), existing.getLocalIdentifier());
                    toWrite = m.addPreset(creator, preset.getIdentifier(), preset.getName(), preset.getDescription());
                } else {
                    toWrite = m.addUserPreset(preset.getName(), preset.getDescription());
                }
                
            } else {
                // not yet, simply add
                
                toWrite = m.addPreset(creator, preset.getIdentifier(), preset.getName(), preset.getDescription());
            }
            
            // each app
            for (PresetAssignedApp paa : preset.getAssignedApps()) {
                IApp app = m.getApp(paa.getIdentifier());
                if (app == null) {
                    throw new InvalidPresetSetException();
                }
                toWrite.assignApp(app);
            }
            
            // each ps
            for (PresetAssignedPrivacySetting pasp : preset.getAssignedPrivacySettings()) {
                IResourceGroup rg = m.getResourceGroup(pasp.getRgIdentifier());
                if (rg == null) {
                    throw new InvalidPresetSetException();
                }
                IPrivacySetting ps = rg.getPrivacySetting(pasp.getPsIdentifier());
                if (ps == null) {
                    throw new InvalidPresetSetException();
                }
                toWrite.assignPrivacySetting(ps, pasp.getValue());
                
                // each ca
                for (PresetPSContext ppsc : pasp.getContexts()) {
                    
                    IResourceGroup rgc = m.getResourceGroup(pasp.getRgIdentifier());
                    if (rgc == null) {
                        throw new InvalidPresetSetException();
                    }
                    IPrivacySetting psc = rgc.getPrivacySetting(pasp.getPsIdentifier());
                    if (psc == null) {
                        throw new InvalidPresetSetException();
                    }
                    IContext c = null;
                    for (IContext c2 : m.getContexts()) {
                        if (c2.getIdentifier().equals(ppsc.getType())) {
                            c = c2;
                        }
                    }
                    if (c == null) {
                        throw new InvalidPresetSetException();
                    }
                    
                    toWrite.assignContextAnnotation(psc, c, ppsc.getCondition(), ppsc.getOverrideValue());
                }
            }
            
        }
        
    }
}
